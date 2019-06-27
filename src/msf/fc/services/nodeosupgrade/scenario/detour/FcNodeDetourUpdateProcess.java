
package msf.fc.services.nodeosupgrade.scenario.detour;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcInternalLinkIfDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.clusters.FcNodeOperationInfoDao;
import msf.fc.rest.ec.node.interfaces.internallink.data.InternalLinkIfUpdateEcRequestBody;
import msf.fc.rest.ec.node.interfaces.internallink.data.entity.InternalLinkIfEcEntity;
import msf.fc.rest.ec.node.interfaces.internallink.data.entity.UpdateInternalLinkIfsEcEntity;
import msf.fc.services.nodeosupgrade.FcNodeOsUpgradeManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.services.nodeosupgrade.common.constant.EcRequestUri;
import msf.mfcfc.services.nodeosupgrade.scenario.detour.data.NodeDetourUpdateRequestBody;

/**
 * Implementation class for the node detour internal processing.
 *
 * @author NTT
 *
 */
public class FcNodeDetourUpdateProcess {

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeDetourUpdateProcess.class);

  @SuppressWarnings("unused")
  private String clusterId;
  private String fabricType;
  private String nodeId;
  private NodeDetourUpdateRequestBody requestBody;

  /**
   * Constructor.
   *
   * @param clusterId
   *          Cluster ID
   * @param fabricType
   *          Fabric type
   * @param nodeId
   *          Node ID
   * @param requestBody
   *          Request message
   *
   */
  public FcNodeDetourUpdateProcess(String clusterId, String fabricType, String nodeId,
      NodeDetourUpdateRequestBody requestBody) {

    this.clusterId = clusterId;
    this.fabricType = fabricType;
    this.nodeId = nodeId;
    this.requestBody = requestBody;
  }

  /**
   * Node detour internal processing.
   *
   * @return true: success /false: failure
   * @throws MsfException
   *           When an error occurs in checking information of the node to be
   *           upgraded, or EC control error occurs.
   */
  public boolean nodeDetourUpdateProcess() throws MsfException {
    try {
      logger.methodStart();
      SessionWrapper sessionWrapper = new SessionWrapper();

      try {
        sessionWrapper.openSession();
        FcNodeDao fcNodeDao = new FcNodeDao();

        List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfList = new ArrayList<>();

        List<FcNode> fcNodeList = fcNodeDao.readList(sessionWrapper);

        FcNode targetDetourNode = getTargetNode(fcNodeList);
        if (!checkDetourUpdateNodes(fcNodeList, targetDetourNode)) {

          return false;
        }

        NodeType oppositeNodeType;
        if (NodeType.LEAF.equals(NodeType.getEnumFromPluralMessage(fabricType))) {
          oppositeNodeType = NodeType.SPINE;
        } else {
          oppositeNodeType = NodeType.LEAF;
        }
        List<FcNode> oppositeNodeList = getOppositeNodeList(fcNodeList, oppositeNodeType);

        sessionWrapper.beginTransaction();
        logger.performance("start get leaf/spine resources lock.");
        List<FcNode> detourNodes = new ArrayList<>();
        detourNodes.add(targetDetourNode);
        if (NodeType.LEAF.equals(oppositeNodeType)) {
          FcDbManager.getInstance().getResourceLock(null, null, oppositeNodeList, detourNodes, sessionWrapper);
        } else {
          FcDbManager.getInstance().getResourceLock(null, null, detourNodes, oppositeNodeList, sessionWrapper);
        }
        logger.performance("end get leaf/spine resources lock.");

        if (requestBody.getUpdateOption().getDetoured()) {

          updateInternalLinkIfList = detourNodeUpdateProcess(targetDetourNode, fcNodeDao, sessionWrapper);
        } else {

          updateInternalLinkIfList = detourReturnNodeUpdateProcess(targetDetourNode, fcNodeDao, sessionWrapper);
        }

        if (!updateInternalLinkIfList.isEmpty()) {

          sendInternalLinkIfPriorityUpdateForNodeDetour(updateInternalLinkIfList);
        }

        sessionWrapper.commit();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();

        FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  private FcNode getTargetNode(List<FcNode> fcNodeList) throws MsfException {
    try {
      logger.methodStart();
      FcNode targetNode = null;
      for (FcNode fcNode : fcNodeList) {
        if ((fcNode.getNodeTypeEnum().equals(NodeType.getEnumFromPluralMessage(fabricType))
            && (fcNode.getNodeId().equals(Integer.valueOf(nodeId))))) {
          targetNode = fcNode;
          break;
        }
      }
      if (targetNode == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            MessageFormat.format("target resource is not found. fabricType = {0}, nodeId = {1}.", fabricType, nodeId));
      }
      return targetNode;
    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkDetourUpdateNodes(List<FcNode> fcNodeList, FcNode targetDetourNode) throws MsfException {
    try {
      logger.methodStart();
      for (FcNode fcNode : fcNodeList) {
        if (fcNode.getNodeInfoId().equals(targetDetourNode.getNodeInfoId())) {
          if (requestBody.getUpdateOption().getDetoured()) {

            if (fcNode.getDetoured()) {

              return false;
            }
          } else {

            if (!fcNode.getDetoured()) {

              throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR, MessageFormat.format(
                  "The target resource is not detour node. fabricType = {0}, nodeId = {1}.", fabricType, nodeId));
            }
          }
        } else {
          if (fcNode.getDetoured()) {
            if (requestBody.getUpdateOption().getDetoured()) {

              logger.warn(MessageFormat.format("Other detour node already exists. fabricType = {0}, nodeId = {1}.",
                  fcNode.getNodeType(), fcNode.getNodeId()));

            } else {

              throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR,
                  MessageFormat.format("Already there are other detour node. fabricType = {0}, nodeId = {1}.",
                      fcNode.getNodeType(), fcNode.getNodeId()));
            }
          }
        }
      }
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  private List<FcNode> getOppositeNodeList(List<FcNode> fcNodeList, NodeType oppositeNodeType) throws MsfException {
    try {
      logger.methodStart();
      List<FcNode> oppositeNodeList = new ArrayList<>();

      for (FcNode fcNode : fcNodeList) {
        if (fcNode.getNodeTypeEnum().equals(oppositeNodeType)) {
          oppositeNodeList.add(fcNode);
        }
      }
      return oppositeNodeList;
    } finally {
      logger.methodEnd();
    }
  }

  private List<UpdateInternalLinkIfsEcEntity> detourNodeUpdateProcess(FcNode targetDetourNode, FcNodeDao fcNodeDao,
      SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();

      List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfList = new ArrayList<>();

      targetDetourNode.setDetoured(true);
      fcNodeDao.update(sessionWrapper, targetDetourNode);

      for (FcPhysicalIf fcPhysicalIf : targetDetourNode.getPhysicalIfs()) {
        List<FcInternalLinkIf> fcInternalLinkIfList = fcPhysicalIf.getInternalLinkIfs();
        detourNodeUpdateInternalLinkIf(targetDetourNode, fcInternalLinkIfList, updateInternalLinkIfList,
            sessionWrapper);
      }

      for (FcLagIf fcLagIf : targetDetourNode.getLagIfs()) {
        List<FcInternalLinkIf> fcInternalLinkIfList = fcLagIf.getInternalLinkIfs();
        detourNodeUpdateInternalLinkIf(targetDetourNode, fcInternalLinkIfList, updateInternalLinkIfList,
            sessionWrapper);
      }

      for (FcBreakoutIf fcBreakoutIf : targetDetourNode.getBreakoutIfs()) {
        List<FcInternalLinkIf> fcInternalLinkIfList = fcBreakoutIf.getInternalLinkIfs();
        detourNodeUpdateInternalLinkIf(targetDetourNode, fcInternalLinkIfList, updateInternalLinkIfList,
            sessionWrapper);
      }
      return updateInternalLinkIfList;
    } finally {
      logger.methodEnd();
    }
  }

  private void detourNodeUpdateInternalLinkIf(FcNode fcNode, List<FcInternalLinkIf> fcInternalLinkIfList,
      List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfList, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();

      if (CollectionUtils.isNotEmpty(fcInternalLinkIfList)) {
        FcInternalLinkIf fcTargetInternalLinkIf = fcInternalLinkIfList.get(0);
        FcInternalLinkIf fcOppositInternalLinkIf = fcInternalLinkIfList.get(0).getOppositeInternalLinkIfs().get(0);

        FcNode oppositNode = null;
        if (fcOppositInternalLinkIf.getLagIf() != null) {
          oppositNode = fcOppositInternalLinkIf.getLagIf().getNode();
        } else if (fcOppositInternalLinkIf.getPhysicalIf() != null) {
          oppositNode = fcOppositInternalLinkIf.getPhysicalIf().getNode();
        } else {
          oppositNode = fcOppositInternalLinkIf.getBreakoutIf().getNode();
        }

        Integer internalLinkDetourIgpCost = FcNodeOsUpgradeManager.getInstance().getSystemConfData().getNodeOsUpgrade()
            .getInternalLinkDetourIgpCost();

        updateDetourInternalLinkIfProcess(fcNode, oppositNode, fcTargetInternalLinkIf, fcOppositInternalLinkIf,
            updateInternalLinkIfList, sessionWrapper, internalLinkDetourIgpCost);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void updateDetourInternalLinkIfProcess(FcNode fcNode, FcNode oppositNode,
      FcInternalLinkIf fcTargetInternalLinkIf, FcInternalLinkIf fcOppositInternalLinkIf,
      List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfList, SessionWrapper sessionWrapper,
      Integer internalLinkIgpCost) throws MsfException {
    try {
      logger.methodStart();

      Integer oldIgpCost = fcTargetInternalLinkIf.getIgpCost();
      fcTargetInternalLinkIf.setOldIgpCost(oldIgpCost);
      fcTargetInternalLinkIf.setIgpCost(internalLinkIgpCost);

      Integer oppositeOldIgpCost = fcOppositInternalLinkIf.getIgpCost();
      fcOppositInternalLinkIf.setOldIgpCost(oppositeOldIgpCost);
      fcOppositInternalLinkIf.setIgpCost(internalLinkIgpCost);

      updateInternalLinkIfList.add(internalLinkIfDetourUpdateProcess(fcNode, fcTargetInternalLinkIf, sessionWrapper));

      updateInternalLinkIfList
          .add(internalLinkIfDetourUpdateProcess(oppositNode, fcOppositInternalLinkIf, sessionWrapper));
    } finally {
      logger.methodEnd();
    }
  }

  private List<UpdateInternalLinkIfsEcEntity> detourReturnNodeUpdateProcess(FcNode targetDetourNode,
      FcNodeDao fcNodeDao, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();

      List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfList = new ArrayList<>();

      targetDetourNode.setDetoured(false);
      fcNodeDao.update(sessionWrapper, targetDetourNode);

      FcInternalLinkIfDao fcInternalLinkIfDao = new FcInternalLinkIfDao();
      List<FcInternalLinkIf> fcInternalLinkIfs = fcInternalLinkIfDao.readList(sessionWrapper);

      for (FcInternalLinkIf fcInternalLinkIf : fcInternalLinkIfs) {
        if (fcInternalLinkIf.getOldIgpCost() != null) {

          FcNode fcNode = null;
          if (fcInternalLinkIf.getLagIf() != null) {
            fcNode = fcInternalLinkIf.getLagIf().getNode();
          } else if (fcInternalLinkIf.getPhysicalIf() != null) {
            fcNode = fcInternalLinkIf.getPhysicalIf().getNode();
          } else {
            fcNode = fcInternalLinkIf.getBreakoutIf().getNode();
          }

          Integer baseIgpCost = fcInternalLinkIf.getOldIgpCost();
          fcInternalLinkIf.setOldIgpCost(null);
          fcInternalLinkIf.setIgpCost(baseIgpCost);

          updateInternalLinkIfList.add(internalLinkIfDetourUpdateProcess(fcNode, fcInternalLinkIf, sessionWrapper));
        }
      }

      return updateInternalLinkIfList;
    } finally {
      logger.methodEnd();
    }
  }

  private UpdateInternalLinkIfsEcEntity internalLinkIfDetourUpdateProcess(FcNode fcNode,
      FcInternalLinkIf fcInternalLinkIf, SessionWrapper sessionWrapper) throws MsfException {

    try {
      logger.methodStart();

      FcInternalLinkIfDao fcInternalLinkIfDao = new FcInternalLinkIfDao();
      fcInternalLinkIfDao.update(sessionWrapper, fcInternalLinkIf);

      UpdateInternalLinkIfsEcEntity updateInternalLinkIfsEcEntity = new UpdateInternalLinkIfsEcEntity();
      updateInternalLinkIfsEcEntity.setNodeId(String.valueOf(fcNode.getEcNodeId()));

      InternalLinkIfEcEntity internalLinkIf = new InternalLinkIfEcEntity();

      if (fcInternalLinkIf.getPhysicalIf() != null) {
        internalLinkIf.setIfTypeEnum(InterfaceType.PHYSICAL_IF);
        internalLinkIf.setIfId(fcInternalLinkIf.getPhysicalIf().getPhysicalIfId());
      } else if (fcInternalLinkIf.getLagIf() != null) {
        internalLinkIf.setIfTypeEnum(InterfaceType.LAG_IF);
        internalLinkIf.setIfId(String.valueOf(fcInternalLinkIf.getLagIf().getLagIfId()));
      } else {
        internalLinkIf.setIfTypeEnum(InterfaceType.BREAKOUT_IF);
        internalLinkIf.setIfId(fcInternalLinkIf.getBreakoutIf().getBreakoutIfId());
      }

      internalLinkIf.setCost(fcInternalLinkIf.getIgpCost());

      updateInternalLinkIfsEcEntity.setInternalLinkIf(internalLinkIf);

      return updateInternalLinkIfsEcEntity;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendInternalLinkIfPriorityUpdateForNodeDetour(
      List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfList) throws MsfException {
    try {
      logger.methodStart(new String[] { "updateInternalLinkIfList" }, new Object[] { updateInternalLinkIfList });

      InternalLinkIfUpdateEcRequestBody internalLinkIfUpdateEcRequestBody = new InternalLinkIfUpdateEcRequestBody();
      List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfsEcEntities = new ArrayList<>();
      updateInternalLinkIfsEcEntities.addAll(updateInternalLinkIfList);
      internalLinkIfUpdateEcRequestBody.setUpdateInternalLinkIfList(updateInternalLinkIfsEcEntities);

      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(internalLinkIfUpdateEcRequestBody));

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.INTERNAL_LINK_IF_UPDATE.getHttpMethod(),
          EcRequestUri.INTERNAL_LINK_IF_UPDATE.getUri(), restRequest, ecControlIpAddress, ecControlPort);

      String errorCode = null;
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        ErrorInternalResponseBody nodeCreateDeleteEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = nodeCreateDeleteEcResponseBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, errorCode,
          ErrorCode.EC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkRestResponseHttpStatusCode(int httpStatusCode, int expectHttpStatusCode, String errorCode,
      ErrorCode throwErrorCode) throws MsfException {
    try {
      logger.methodStart(new String[] { "httpStatusCode", "expectHttpStatusCode", "errorCode", "throwErrorCode" },
          new Object[] { httpStatusCode, expectHttpStatusCode, errorCode, throwErrorCode });

      if (httpStatusCode != expectHttpStatusCode) {

        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}", httpStatusCode, errorCode);
        logger.error(errorMsg);
        throw new MsfException(throwErrorCode, errorMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

}
