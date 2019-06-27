
package msf.fc.services.nodeosupgrade.scenario.upgrade;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcAsyncRequest;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcEquipment;
import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.FcDbManager;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.clusters.FcNodeOperationInfoDao;
import msf.fc.rest.ec.node.recovernode.data.RecoverNodeCreateEcRequestBody;
import msf.fc.rest.ec.node.recovernode.data.entity.RecoverEquipmentEcEntity;
import msf.fc.rest.ec.node.recovernode.data.entity.RecoverInternalLinkIfListEcEntity;
import msf.fc.rest.ec.node.recovernode.data.entity.RecoverNodeEcEntity;
import msf.fc.services.nodeosupgrade.FcNodeOsUpgradeManager;
import msf.fc.services.nodeosupgrade.scenario.detour.FcNodeDetourUpdateProcess;
import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.InternalNodeType;
import msf.mfcfc.common.constant.LeafType;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.services.nodeosupgrade.common.constant.NodeDetourUpdateAction;
import msf.mfcfc.services.nodeosupgrade.common.constant.NodeOsUpgradeOperationType;
import msf.mfcfc.services.nodeosupgrade.common.constant.NodeOsUpgradeStatus;
import msf.mfcfc.services.nodeosupgrade.common.constant.OsUpgradeResultType;
import msf.mfcfc.services.nodeosupgrade.scenario.detour.data.NodeDetourUpdateRequestBody;
import msf.mfcfc.services.nodeosupgrade.scenario.detour.data.entity.NodeDetourUpdateOptionEntity;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.InternalNodeOsUpgradeRequest;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeInnerNotifyRequestBody;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.entity.NodeOsUpgradeListEntity;

/**
 * Class to implement the asynchronous processing in the node OS upgrade(for MSF
 * internal interface).
 *
 * @author NTT
 *
 */
public class FcInternalNodeOsUpgradeRunner extends FcAbstractNodeOsUpgradeRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalNodeOsUpgradeRunner.class);

  @SuppressWarnings("unused")
  private InternalNodeOsUpgradeRequest request;
  private NodeOsUpgradeInnerNotifyRequestBody requestBody;

  /**
   * Constructor.
   * <p>
   * Set the request information from scenario as arguments
   * </p>
   *
   * @param request
   *          Request
   * @param requestBody
   *          Request body
   */
  public FcInternalNodeOsUpgradeRunner(InternalNodeOsUpgradeRequest request,
      NodeOsUpgradeInnerNotifyRequestBody requestBody) {

    this.request = request;
    this.requestBody = requestBody;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      FcNodeOsUpgradeScheduler nodeOsUpgradeScheduler = FcNodeOsUpgradeManager.getInstance()
          .getNodeOsUpgradeScheduler();
      Integer targetNodeStatus = null;

      FcAsyncRequest currentAsyncRequest = checkCurrentAsyncRequest(nodeOsUpgradeScheduler);

      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();
        sessionWrapper.beginTransaction();
        boolean updateFlag = false;

        Object lockObject = OperationManager.getInstance().getOperationIdObject(currentAsyncRequest.getOperationId());
        if (lockObject != null) {
          synchronized (lockObject) {

            if (currentAsyncRequest.getStatusEnum().equals(AsyncProcessStatus.RUNNING)) {

              NodeOsUpgradeListEntity currentNodeOsUpgradeListEntity = nodeOsUpgradeScheduler
                  .getCurrentNodeOsUpgradeListEntity();

              targetNodeStatus = nodeOsUpgradeScheduler.getTargetNodeStatus(requestBody.getNodeId());
              if (targetNodeStatus == null) {

                throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
                    MessageFormat.format("target resource is not found. fabricType = {0}, ecNodeId = {1}.",
                        requestBody.getFabricType(), requestBody.getNodeId()));
              }
              checkNodeOsUpgradeNotify(targetNodeStatus);

              FcNode updateNode = getNode(sessionWrapper, new FcNodeDao(), requestBody.getNodeId());

              if (requestBody.getOperationTypeEnum().equals(NodeOsUpgradeOperationType.OS_UPGRADE)) {

                updateFlag = nodeOsUpgradeNotifyProcess(updateNode, currentNodeOsUpgradeListEntity,
                    nodeOsUpgradeScheduler, sessionWrapper);

              } else {

                recoverNodeNotifyProcess(updateNode, currentNodeOsUpgradeListEntity, nodeOsUpgradeScheduler);

              }
              if (updateFlag) {

                sessionWrapper.commit();
              }
            }
          }
        }

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        if (targetNodeStatus != null) {

          nodeOsUpgradeScheduler.runningOperationFailureProcess(false, msfException.getErrorCode(),
              msfException.getMessage());
        }

        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

  private FcAsyncRequest checkCurrentAsyncRequest(FcNodeOsUpgradeScheduler nodeOsUpgradeScheduler) throws MsfException {
    try {
      logger.methodStart();
      FcAsyncRequest currentAsyncRequest = nodeOsUpgradeScheduler.getCurrentAsyncRequest();
      if (currentAsyncRequest == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource is not found. parameters = fcAsyncRequest");
      }
      return currentAsyncRequest;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkNodeOsUpgradeNotify(Integer targetNodeStatus) throws MsfException {
    try {
      logger.methodStart();
      switch (NodeOsUpgradeStatus.getEnumFromCode(targetNodeStatus)) {
        case OS_UPGRADING:
          if (!requestBody.getOperationTypeEnum().equals(NodeOsUpgradeOperationType.OS_UPGRADE)) {

            throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR,
                MessageFormat.format("target node state error. targetNodeStatus = {0}, operationType = {1}.",
                    NodeOsUpgradeStatus.OS_UPGRADING.getMessage(), requestBody.getOperationType()));
          }
          break;

        case RECOVERING:
          if (!requestBody.getOperationTypeEnum().equals(NodeOsUpgradeOperationType.RECOVER_NODE)) {

            throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR,
                MessageFormat.format("target node state error. targetNodeStatus = {0}, operationType = {1}.",
                    NodeOsUpgradeStatus.RECOVERING.getMessage(), requestBody.getOperationType()));
          }
          break;

        default:

          throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR,
              MessageFormat.format("target node state error. targetNodeStatus = {0}.",
                  NodeOsUpgradeStatus.getEnumFromCode(targetNodeStatus).getMessage()));
      }
    } finally {
      logger.methodEnd();
    }
  }

  private boolean nodeOsUpgradeNotifyProcess(FcNode updateNode, NodeOsUpgradeListEntity currentNodeOsUpgradeListEntity,
      FcNodeOsUpgradeScheduler nodeOsUpgradeScheduler, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();

      boolean updateFlag = false;

      if (requestBody.getOsUpgradeResultEnum().equals(OsUpgradeResultType.SUCCEEDED)) {
        nodeOsUpgradeScheduler.updateRunningOperationStatus(NodeOsUpgradeStatus.RECOVERING);

        Integer requestEquipmentTypeId = Integer.valueOf(currentNodeOsUpgradeListEntity.getEquipmentTypeId());
        if (!updateNode.getEquipment().getEquipmentTypeId().equals(requestEquipmentTypeId)) {

          updateFlag = true;
          changeEquipmentTypeProcess(sessionWrapper, updateNode, requestEquipmentTypeId);
        }
      } else {

        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, MessageFormat.format(
            "node os upgrade failed. nodeType = {0}, nodeId = {1}.", updateNode.getNodeType(), updateNode.getNodeId()));
      }

      RecoverNodeCreateEcRequestBody recoverNodeCreateEcRequestBody = createRecoverNodeData(updateNode,
          currentNodeOsUpgradeListEntity);

      sendLeafRecoverNode(updateNode, recoverNodeCreateEcRequestBody);

      return updateFlag;
    } finally {
      logger.methodEnd();
    }
  }

  private void changeEquipmentTypeProcess(SessionWrapper sessionWrapper, FcNode updateNode,
      Integer requestEquipmentTypeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "updateNode", "requestEquipmentTypeId" },
          new Object[] { updateNode, requestEquipmentTypeId });
      List<FcNode> fcNodes = new ArrayList<>();
      fcNodes.add(updateNode);

      if (updateNode.getNodeTypeEnum().equals(NodeType.LEAF)) {
        logger.performance("start get leaf resources lock.");
        FcDbManager.getInstance().getLeafsLock(fcNodes, sessionWrapper);
        logger.performance("end get leaf resources lock.");
      } else {
        logger.performance("start get spine resources lock.");
        FcDbManager.getInstance().getSpinesLock(fcNodes, sessionWrapper);
        logger.performance("end get spine resources lock.");
      }

      FcEquipment fcEquipment = new FcEquipment();
      fcEquipment.setEquipmentTypeId(requestEquipmentTypeId);

      updateNode.setEquipment(fcEquipment);
      FcNodeDao fcNodeDao = new FcNodeDao();
      fcNodeDao.update(sessionWrapper, updateNode);

    } finally {
      logger.methodEnd();
    }
  }

  private void recoverNodeNotifyProcess(FcNode updateNode, NodeOsUpgradeListEntity currentNodeOsUpgradeListEntity,
      FcNodeOsUpgradeScheduler nodeOsUpgradeScheduler) throws MsfException {
    try {
      logger.methodStart();
      if (currentNodeOsUpgradeListEntity.getOperatorCheck()) {

        if (requestBody.getOsUpgradeResultEnum().equals(OsUpgradeResultType.SUCCEEDED)) {

          nodeOsUpgradeScheduler.notifyNodeOsUpgradeResult(OsUpgradeResultType.SUCCEEDED);
        } else {

          nodeOsUpgradeScheduler.notifyNodeOsUpgradeResult(OsUpgradeResultType.FAILED);
        }

        nodeOsUpgradeScheduler.updateRunningOperationStatus(NodeOsUpgradeStatus.OPERATOR_CHECKING);

        FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());

      } else {

        if (requestBody.getOsUpgradeResultEnum().equals(OsUpgradeResultType.SUCCEEDED)) {
          nodeOsUpgradeScheduler.updateRunningOperationStatus(NodeOsUpgradeStatus.RECOVERED);
        } else {

          throw new MsfException(ErrorCode.EC_CONTROL_ERROR, MessageFormat.format(
              "recover node failed. nodeType = {0}, nodeId = {1}.", updateNode.getNodeType(), updateNode.getNodeId()));
        }

        NodeDetourUpdateRequestBody nodeDetourUpdateRequestBody = new NodeDetourUpdateRequestBody();
        nodeDetourUpdateRequestBody.setActionEnum(NodeDetourUpdateAction.UPDATE);
        NodeDetourUpdateOptionEntity updateOption = new NodeDetourUpdateOptionEntity();

        updateOption.setDetoured(false);
        nodeDetourUpdateRequestBody.setUpdateOption(updateOption);
        int swClusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
        FcNodeDetourUpdateProcess fcNodeDetourUpdateProcess = new FcNodeDetourUpdateProcess(String.valueOf(swClusterId),
            updateNode.getNodeTypeEnum().getPluralMessage(), String.valueOf(updateNode.getNodeId()),
            nodeDetourUpdateRequestBody);
        fcNodeDetourUpdateProcess.nodeDetourUpdateProcess();

        nodeOsUpgradeScheduler.processingAfterRecoverNode();
      }
    } finally {
      logger.methodEnd();
    }
  }

  private RecoverNodeCreateEcRequestBody createRecoverNodeData(FcNode updateNode,
      NodeOsUpgradeListEntity currentNodeOsUpgradeListEntity) throws MsfException {
    try {
      logger.methodStart(new String[] { "updateNode", "currentNodeOsUpgradeListEntity" },
          new Object[] { updateNode, currentNodeOsUpgradeListEntity });

      RecoverNodeCreateEcRequestBody recoverNodeCreateEcRequestBody = new RecoverNodeCreateEcRequestBody();
      RecoverEquipmentEcEntity equipment = new RecoverEquipmentEcEntity();
      equipment.setEquipmentTypeId(currentNodeOsUpgradeListEntity.getEquipmentTypeId());
      recoverNodeCreateEcRequestBody.setEquipment(equipment);

      RecoverNodeEcEntity node = new RecoverNodeEcEntity();

      node.setNodeUpgrade(true);
      switch (updateNode.getNodeTypeEnum()) {
        case LEAF:

          boolean isBorderLeaf = LeafType.getEnumFromCode(updateNode.getLeafNode().getLeafType())
              .equals(LeafType.BORDER_LEAF);
          node.setNodeType(isBorderLeaf ? InternalNodeType.B_LEAF.getMessage() : InternalNodeType.LEAF.getMessage());
          break;
        case SPINE:

          node.setNodeType(InternalNodeType.SPINE.getMessage());

          List<RecoverInternalLinkIfListEcEntity> internalLinkIfList = createRecoverInternalLinkIfListData(updateNode);
          node.setInternalLinkIfList(internalLinkIfList);
          break;

        default:

          throw new MsfException(ErrorCode.UNDEFINED_ERROR,
              "Illegal parameter. nodeType = " + updateNode.getNodeType());
      }

      recoverNodeCreateEcRequestBody.setNode(node);
      return recoverNodeCreateEcRequestBody;
    } finally {
      logger.methodEnd();
    }
  }

  private List<RecoverInternalLinkIfListEcEntity> createRecoverInternalLinkIfListData(FcNode updateNode)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "updateNode" }, new Object[] { updateNode });
      List<RecoverInternalLinkIfListEcEntity> internalLinkIfList = new ArrayList<>();

      for (FcPhysicalIf fcPhysicalIf : updateNode.getPhysicalIfs()) {
        List<FcInternalLinkIf> fcInternalLinkIfList = fcPhysicalIf.getInternalLinkIfs();
        createRecoverNodeUpdateInternalLinkIf(updateNode, fcInternalLinkIfList, internalLinkIfList);
      }

      for (FcLagIf fcLagIf : updateNode.getLagIfs()) {
        List<FcInternalLinkIf> fcInternalLinkIfList = fcLagIf.getInternalLinkIfs();
        createRecoverNodeUpdateInternalLinkIf(updateNode, fcInternalLinkIfList, internalLinkIfList);
      }

      for (FcBreakoutIf fcBreakoutIf : updateNode.getBreakoutIfs()) {
        List<FcInternalLinkIf> fcInternalLinkIfList = fcBreakoutIf.getInternalLinkIfs();
        createRecoverNodeUpdateInternalLinkIf(updateNode, fcInternalLinkIfList, internalLinkIfList);
      }

      return internalLinkIfList;
    } finally {
      logger.methodEnd();
    }
  }

  private void createRecoverNodeUpdateInternalLinkIf(FcNode fcNode, List<FcInternalLinkIf> fcInternalLinkIfList,
      List<RecoverInternalLinkIfListEcEntity> internalLinkIfList) throws MsfException {
    try {
      logger.methodStart();

      if (CollectionUtils.isNotEmpty(fcInternalLinkIfList)) {
        FcInternalLinkIf fcTargetInternalLinkIf = fcInternalLinkIfList.get(0);
        FcInternalLinkIf fcOppositInternalLinkIf = fcInternalLinkIfList.get(0).getOppositeInternalLinkIfs().get(0);

        RecoverInternalLinkIfListEcEntity recoverInternalLinkIfListEntity = new RecoverInternalLinkIfListEcEntity();
        if (fcTargetInternalLinkIf.getLagIf() != null) {
          recoverInternalLinkIfListEntity.setIfTypeEnum(InterfaceType.LAG_IF);
          recoverInternalLinkIfListEntity.setIfId(String.valueOf(fcTargetInternalLinkIf.getLagIf().getLagIfId()));
        } else if (fcTargetInternalLinkIf.getPhysicalIf() != null) {
          recoverInternalLinkIfListEntity.setIfTypeEnum(InterfaceType.PHYSICAL_IF);
          recoverInternalLinkIfListEntity.setIfId(fcTargetInternalLinkIf.getPhysicalIf().getPhysicalIfId());
        } else {
          recoverInternalLinkIfListEntity.setIfTypeEnum(InterfaceType.BREAKOUT_IF);
          recoverInternalLinkIfListEntity.setIfId(fcTargetInternalLinkIf.getBreakoutIf().getBreakoutIfId());
        }

        if (fcOppositInternalLinkIf.getLagIf() != null) {
          recoverInternalLinkIfListEntity
              .setOppositeNodeId(String.valueOf(fcOppositInternalLinkIf.getLagIf().getNode().getEcNodeId()));
        } else if (fcOppositInternalLinkIf.getPhysicalIf() != null) {
          recoverInternalLinkIfListEntity
              .setOppositeNodeId(String.valueOf(fcOppositInternalLinkIf.getPhysicalIf().getNode().getEcNodeId()));
        } else {
          recoverInternalLinkIfListEntity
              .setOppositeNodeId(String.valueOf(fcOppositInternalLinkIf.getBreakoutIf().getNode().getEcNodeId()));
        }

        internalLinkIfList.add(recoverInternalLinkIfListEntity);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private FcNode getNode(SessionWrapper sessionWrapper, FcNodeDao fcNodeDao, String ecNodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "fcNodeDao", "ecNodeId" }, new Object[] { fcNodeDao, ecNodeId });
      FcNode fcNode = fcNodeDao.readByEcNodeId(sessionWrapper, Integer.valueOf(ecNodeId));

      return fcNode;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendLeafRecoverNode(FcNode updateNode,
      RecoverNodeCreateEcRequestBody recoverNodeCreateEcRequestBody) throws MsfException {
    try {
      logger.methodStart(new String[] { "updateNode", "recoverNodeCreateEcRequestBody" },
          new Object[] { updateNode, recoverNodeCreateEcRequestBody });

      RestRequestBase restRequest = new RestRequestBase();

      restRequest.setRequestBody(JsonUtil.toJson(recoverNodeCreateEcRequestBody));

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.SERVICE_RECOVERY_CREATE.getHttpMethod(),
          EcRequestUri.SERVICE_RECOVERY_CREATE.getUri(String.valueOf(updateNode.getEcNodeId())), restRequest,
          ecControlIpAddress, ecControlPort);

      String errorCode = null;
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        ErrorInternalResponseBody recoverNodeEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = recoverNodeEcResponseBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.ACCEPTED_202, errorCode,
          ErrorCode.EC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

}
