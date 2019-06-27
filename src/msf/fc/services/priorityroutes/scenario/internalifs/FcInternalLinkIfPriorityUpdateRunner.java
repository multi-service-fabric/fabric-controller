
package msf.fc.services.priorityroutes.scenario.internalifs;

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
import msf.mfcfc.services.priorityroutes.common.constant.EcRequestUri;
import msf.mfcfc.services.priorityroutes.scenario.internalifs.data.InternalLinkIfPriorityRequest;
import msf.mfcfc.services.priorityroutes.scenario.internalifs.data.InternalLinkIfPriorityUpdateRequestBody;

/**
 * Class to implement the asynchronous processing in the internal-link interface
 * priority modification.
 *
 * @author NTT
 *
 */
public class FcInternalLinkIfPriorityUpdateRunner extends FcAbstractInternalLinkIfPriorityRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalLinkIfPriorityUpdateRunner.class);

  private InternalLinkIfPriorityRequest request;
  private InternalLinkIfPriorityUpdateRequestBody requestBody;

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   */
  public FcInternalLinkIfPriorityUpdateRunner(InternalLinkIfPriorityRequest request,
      InternalLinkIfPriorityUpdateRequestBody requestBody) {

    this.request = request;
    this.requestBody = requestBody;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();

      try {
        sessionWrapper.openSession();

        FcNodeDao fcNodeDao = new FcNodeDao();

        List<FcNode> fcNodeList = fcNodeDao.readList(sessionWrapper);

        checkNodePriority(fcNodeList);

        FcNode targetNode = getTargetNode(fcNodeList, request.getFabricTypeEnum(),
            Integer.valueOf(request.getNodeId()));

        FcInternalLinkIf targetInternalLinkIf = getTargetInternalLinkIf(targetNode,
            Integer.valueOf(request.getInternalLinkIfId()));

        logger.performance("start get leaf/spine resources lock.");
        sessionWrapper.beginTransaction();
        List<FcNode> fcNodes = new ArrayList<>();
        fcNodes.add(targetNode);
        if (NodeType.LEAF.equals(request.getFabricTypeEnum())) {
          FcDbManager.getInstance().getLeafsLock(fcNodes, sessionWrapper);
        } else {
          FcDbManager.getInstance().getSpinesLock(fcNodes, sessionWrapper);
        }
        logger.performance("end get leaf/spine resources lock.");

        updateInternalLinkIfPriority(sessionWrapper, targetInternalLinkIf);

        UpdateInternalLinkIfsEcEntity internalLinkIfPriorityUpdateData = createInternalLinkIfPriorityUpdateData(
            targetNode, targetInternalLinkIf);

        sendInternalLinkIfPriorityUpdate(internalLinkIfPriorityUpdateData);

        responseBase = responseInternalLinkIfPriorityUpdateData();

        sessionWrapper.commit();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();

        FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkNodePriority(List<FcNode> fcNodeList) throws MsfException {

    try {
      logger.methodStart();
      for (FcNode fcNode : fcNodeList) {
        if (fcNode.getIsPriorityNodeGroupMember()) {

          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR,
              MessageFormat.format("There already have a priority node. nodeType = {0}, nodeId = {1}.",
                  fcNode.getNodeType(), fcNode.getNodeId()));
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private FcNode getTargetNode(List<FcNode> fcNodeList, NodeType nodeType, Integer nodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNodeList", "nodeType", "nodeId" },
          new Object[] { fcNodeList, nodeType, nodeId });
      FcNode targetFcNode = null;
      for (FcNode fcNode : fcNodeList) {
        if ((fcNode.getNodeTypeEnum().equals(nodeType)) && (fcNode.getNodeId().equals(nodeId))) {

          targetFcNode = fcNode;
          break;
        }
      }
      if (targetFcNode == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            MessageFormat.format("target resource is not found. nodeType = {0}, nodeId = {1}.", nodeType, nodeId));
      }
      return targetFcNode;
    } finally {
      logger.methodEnd();
    }
  }

  private FcInternalLinkIf getTargetInternalLinkIf(FcNode targetNode, Integer internalLinkIfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "targetNode", "internalLinkIfId" },
          new Object[] { targetNode, internalLinkIfId });

      FcInternalLinkIf targetFcInternalLinkIf = null;
      for (FcPhysicalIf fcPhysicalIf : targetNode.getPhysicalIfs()) {
        List<FcInternalLinkIf> fcInternalLinkIfList = fcPhysicalIf.getInternalLinkIfs();
        targetFcInternalLinkIf = checkInternalLinkIf(fcInternalLinkIfList, internalLinkIfId);
        if (targetFcInternalLinkIf != null) {

          return targetFcInternalLinkIf;
        }
      }

      for (FcLagIf fcLagIf : targetNode.getLagIfs()) {
        List<FcInternalLinkIf> fcInternalLinkIfList = fcLagIf.getInternalLinkIfs();
        targetFcInternalLinkIf = checkInternalLinkIf(fcInternalLinkIfList, internalLinkIfId);
        if (targetFcInternalLinkIf != null) {

          return targetFcInternalLinkIf;
        }
      }

      for (FcBreakoutIf fcBreakoutIf : targetNode.getBreakoutIfs()) {
        List<FcInternalLinkIf> fcInternalLinkIfList = fcBreakoutIf.getInternalLinkIfs();
        targetFcInternalLinkIf = checkInternalLinkIf(fcInternalLinkIfList, internalLinkIfId);
        if (targetFcInternalLinkIf != null) {

          return targetFcInternalLinkIf;
        }
      }

      throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
          MessageFormat.format("target resource is not found. internalLinkIfId = {0}.", internalLinkIfId));

    } finally {
      logger.methodEnd();
    }
  }

  private FcInternalLinkIf checkInternalLinkIf(List<FcInternalLinkIf> fcInternalLinkIfList, Integer internalLinkIfId) {
    try {
      logger.methodStart();
      if (CollectionUtils.isNotEmpty(fcInternalLinkIfList)) {
        if (internalLinkIfId.equals(fcInternalLinkIfList.get(0).getInternalLinkIfId())) {
          return fcInternalLinkIfList.get(0);
        }
      }
      return null;
    } finally {
      logger.methodEnd();
    }
  }

  private void updateInternalLinkIfPriority(SessionWrapper sessionWrapper, FcInternalLinkIf targetInternalLinkIf)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "targetInternalLinkIf" }, new Object[] { targetInternalLinkIf });
      targetInternalLinkIf.setIgpCost(requestBody.getUpdateOption().getIgpCost());
      FcInternalLinkIfDao fcInternalLinkIfDao = new FcInternalLinkIfDao();
      fcInternalLinkIfDao.update(sessionWrapper, targetInternalLinkIf);
    } finally {
      logger.methodEnd();
    }
  }

  private UpdateInternalLinkIfsEcEntity createInternalLinkIfPriorityUpdateData(FcNode targetNode,
      FcInternalLinkIf targetInternalLinkIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "targetNode", "targetInternalLinkIf" },
          new Object[] { targetNode, targetInternalLinkIf });
      UpdateInternalLinkIfsEcEntity updateInternalLinkIfsEntity = new UpdateInternalLinkIfsEcEntity();

      updateInternalLinkIfsEntity.setNodeId(String.valueOf(targetNode.getEcNodeId()));

      InternalLinkIfEcEntity internalLinkIf = new InternalLinkIfEcEntity();

      if (targetInternalLinkIf.getPhysicalIf() != null) {
        internalLinkIf.setIfTypeEnum(InterfaceType.PHYSICAL_IF);
        internalLinkIf.setIfId(targetInternalLinkIf.getPhysicalIf().getPhysicalIfId());
      } else if (targetInternalLinkIf.getLagIf() != null) {
        internalLinkIf.setIfTypeEnum(InterfaceType.LAG_IF);
        internalLinkIf.setIfId(String.valueOf(targetInternalLinkIf.getLagIf().getLagIfId()));
      } else {
        internalLinkIf.setIfTypeEnum(InterfaceType.BREAKOUT_IF);
        internalLinkIf.setIfId(targetInternalLinkIf.getBreakoutIf().getBreakoutIfId());
      }

      internalLinkIf.setCost(targetInternalLinkIf.getIgpCost());

      updateInternalLinkIfsEntity.setInternalLinkIf(internalLinkIf);
      return updateInternalLinkIfsEntity;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendInternalLinkIfPriorityUpdate(
      UpdateInternalLinkIfsEcEntity internalLinkIfPriorityUpdateData) throws MsfException {
    try {
      logger.methodStart(new String[] { "internalLinkIfPriorityUpdateData" },
          new Object[] { internalLinkIfPriorityUpdateData });

      InternalLinkIfUpdateEcRequestBody internalLinkIfUpdateEcRequestBody = new InternalLinkIfUpdateEcRequestBody();
      List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfsEcEntities = new ArrayList<>();
      updateInternalLinkIfsEcEntities.add(internalLinkIfPriorityUpdateData);
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

  private RestResponseBase responseInternalLinkIfPriorityUpdateData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }
}
