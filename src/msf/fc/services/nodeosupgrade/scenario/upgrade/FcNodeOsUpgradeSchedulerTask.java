
package msf.fc.services.nodeosupgrade.scenario.upgrade;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import msf.fc.common.data.FcAsyncRequest;
import msf.fc.common.data.FcEquipment;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.clusters.FcEquipmentDao;
import msf.fc.db.dao.clusters.FcNodeOperationInfoDao;
import msf.fc.services.nodeosupgrade.FcNodeOsUpgradeManager;
import msf.fc.services.nodeosupgrade.rest.ec.nodeosupgrade.data.NodeOsUpgradeEcRequestBody;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.constant.OperationExecutionStatus;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SpecialOperationType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.services.nodeosupgrade.common.constant.NodeOsUpgradeStatus;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeRequest;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeRequestBody;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.entity.NodeOsUpgradeEntity;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.entity.NodeOsUpgradeListEntity;

/**
 * A reservation scheduler task for the node OS upgrade.
 *
 * @author NTT
 *
 */
public class FcNodeOsUpgradeSchedulerTask extends TimerTask {

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeOsUpgradeSchedulerTask.class);

  public NodeOsUpgradeRequest request;
  public NodeOsUpgradeRequestBody requestBody;
  public String operationId;

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
   * @param operationId
   *          Operation ID
   */
  public FcNodeOsUpgradeSchedulerTask(NodeOsUpgradeRequest request, NodeOsUpgradeRequestBody requestBody,
      String operationId) {

    this.request = request;
    this.requestBody = requestBody;
    this.operationId = operationId;
  }

  @Override
  public void run() {
    try {
      logger.methodStart();

      SessionWrapper sessionWrapper = new SessionWrapper();
      FcNodeOsUpgradeScheduler nodeOsUpgradeScheduler = FcNodeOsUpgradeManager.getInstance()
          .getNodeOsUpgradeScheduler();

      OperationExecutionStatus opExeStatus = OperationManager.getInstance()
          .getOperationExecutionStatus(OperationType.NORMAL, SpecialOperationType.NORMAL);

      if (opExeStatus == OperationExecutionStatus.NOT_ALLOWED) {
        try {
          String errorMsg = "System can not perform a request.";
          nodeOsUpgradeScheduler.setOsUpgradeSubEntitiesForFailed(requestBody, ErrorCode.SYSTEM_STATUS_ERROR, errorMsg,
              true, nodeOsUpgradeScheduler.getCurrentAsyncRequest());
          nodeOsUpgradeScheduler.runningOperationFailureProcess(true, ErrorCode.SYSTEM_STATUS_ERROR, errorMsg);
        } catch (MsfException msfException) {

        }

        return;
      }

      try {

        FcAsyncRequest currentAsyncRequest = nodeOsUpgradeScheduler.getCurrentAsyncRequest();

        sessionWrapper.openSession();

        Object lockObject = OperationManager.getInstance().getOperationIdObject(currentAsyncRequest.getOperationId());
        if (lockObject != null) {
          synchronized (lockObject) {

            nodeOsUpgradeScheduler.updateAsyncRequest(requestBody.getNodeList());

            boolean hasChangeNodeOperationStatus = FcNodeOperationInfoDao
                .hasChangeNodeOperationStatus(NodeOperationStatus.RUNNING.getCode());
            if (!hasChangeNodeOperationStatus) {
              String errorMsg = "Another node related operation is currently in progress.";
              nodeOsUpgradeScheduler.setOsUpgradeSubEntitiesForFailed(requestBody, ErrorCode.UPDATE_INFORMATION_ERROR,
                  errorMsg, true, nodeOsUpgradeScheduler.getCurrentAsyncRequest());

              throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR, errorMsg);
            }

            List<NodeOsUpgradeEntity> currentOsUpgradeEntities = new ArrayList<>();
            for (NodeOsUpgradeListEntity nodeOsUpgradeListEntity : requestBody.getNodeList()) {

              NodeOsUpgradeEntity nodeOsUpgradeSubEntity = new NodeOsUpgradeEntity();
              nodeOsUpgradeSubEntity.setFabricType(nodeOsUpgradeListEntity.getFabricType());
              nodeOsUpgradeSubEntity.setNodeId(nodeOsUpgradeListEntity.getNodeId());
              nodeOsUpgradeSubEntity.setEquipmentTypeId(nodeOsUpgradeListEntity.getEquipmentTypeId());
              nodeOsUpgradeSubEntity.setStatusEnum(NodeOsUpgradeStatus.WAITING);
              try {

                nodeOsUpgradeScheduler.getNode(sessionWrapper, nodeOsUpgradeListEntity.getFabricTypeEnum(),
                    Integer.valueOf(nodeOsUpgradeListEntity.getNodeId()));

                getEquipment(sessionWrapper, Integer.valueOf(nodeOsUpgradeListEntity.getEquipmentTypeId()));

              } catch (MsfException msfException) {

                nodeOsUpgradeSubEntity.setStatusEnum(NodeOsUpgradeStatus.FAILED_NOT_OS_UPGRADED_NOT_DETOURED);
                nodeOsUpgradeSubEntity.setErrorCode(msfException.getErrorCode().getCode());
                nodeOsUpgradeSubEntity.setErrorMessage(msfException.getMessage());
              }
              currentOsUpgradeEntities.add(nodeOsUpgradeSubEntity);
            }

            nodeOsUpgradeScheduler.setCurrentOsUpgradeSubEntities(currentOsUpgradeEntities);

            nodeOsUpgradeScheduler.nodeDetourProcess();

            nodeOsUpgradeScheduler.updateRunningOperationStatus(NodeOsUpgradeStatus.DETOURED);

            nodeOsUpgradeProcess(sessionWrapper, nodeOsUpgradeScheduler);

            nodeOsUpgradeScheduler.updateRunningOperationStatus(NodeOsUpgradeStatus.OS_UPGRADING);

          }
        }
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        try {
          sessionWrapper.rollback();
        } catch (MsfException msfException2) {

        }
        try {
          nodeOsUpgradeScheduler.runningOperationFailureProcess(false, msfException.getErrorCode(),
              msfException.getMessage());
        } catch (MsfException msfException3) {

        }
      } finally {
        sessionWrapper.closeSession();
      }
    } finally {
      logger.methodEnd();
    }
  }

  private FcEquipment getEquipment(SessionWrapper sessionWrapper, Integer equipmentTypeId) throws MsfException {
    try {
      logger.methodStart();
      FcEquipmentDao fcEquipmentDao = new FcEquipmentDao();
      FcEquipment fcEquipment = fcEquipmentDao.read(sessionWrapper, equipmentTypeId);
      if (fcEquipment == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            MessageFormat.format("target resource is not found. equipmentTypeId = {0}.", equipmentTypeId));
      }
      return fcEquipment;
    } finally {
      logger.methodEnd();
    }
  }

  private void nodeOsUpgradeProcess(SessionWrapper sessionWrapper, FcNodeOsUpgradeScheduler nodeOsUpgradeScheduler)
      throws MsfException {
    try {
      logger.methodStart();

      NodeOsUpgradeListEntity currentNodeOsUpgradeListEntity = nodeOsUpgradeScheduler
          .getCurrentNodeOsUpgradeListEntity();
      FcNode updateNode = nodeOsUpgradeScheduler.getNode(sessionWrapper,
          currentNodeOsUpgradeListEntity.getFabricTypeEnum(),
          Integer.valueOf(currentNodeOsUpgradeListEntity.getNodeId()));
      NodeOsUpgradeEcRequestBody createNodeOsUpgradeData = nodeOsUpgradeScheduler.createNodeOsUpgradeData(updateNode,
          currentNodeOsUpgradeListEntity);

      nodeOsUpgradeScheduler.sendNodeOsUpgrade(updateNode, createNodeOsUpgradeData);
    } finally {
      logger.methodEnd();
    }
  }

}
