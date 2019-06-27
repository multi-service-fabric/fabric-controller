
package msf.fc.services.nodeosupgrade.scenario.upgrade;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcAsyncRequest;
import msf.fc.services.nodeosupgrade.FcNodeOsUpgradeManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SpecialOperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeCancelRequestBody;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeRequest;

/**
 * Implementation class for the node OS upgrade cancellation.
 *
 * @author NTT
 *
 */
public class FcNodeOsUpgradeCancelScenario extends FcAbstractNodeOsUpgradeScenarioBase<NodeOsUpgradeRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeOsUpgradeCancelScenario.class);

  private NodeOsUpgradeRequest request;

  @SuppressWarnings("unused")
  private NodeOsUpgradeCancelRequestBody requestBody;

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
   *
   */
  public FcNodeOsUpgradeCancelScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
    this.specialOperationType = SpecialOperationType.SPECIALOPERATION;

  }

  @Override
  protected void checkParameter(NodeOsUpgradeRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotNullAndLength(request.getOperationId());

      NodeOsUpgradeCancelRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          NodeOsUpgradeCancelRequestBody.class);
      requestBody.validate();

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      logger.performance("start wait for node os upgrade process.");
      synchronized (FcNodeOsUpgradeManager.getInstance().getFcNodeOsUpgradeLockObject()) {
        logger.performance("end wait for node os upgrade process.");

        FcNodeOsUpgradeScheduler nodeOsUpgradeScheduler = FcNodeOsUpgradeManager.getInstance()
            .getNodeOsUpgradeScheduler();

        FcAsyncRequest cancelOsUpgradeRequest = getTargetAsyncRequest(nodeOsUpgradeScheduler);

        try {

          Object lockObject = OperationManager.getInstance()
              .getOperationIdObject(cancelOsUpgradeRequest.getOperationId());
          if (lockObject != null) {
            synchronized (lockObject) {

              switch (cancelOsUpgradeRequest.getStatusEnum()) {
                case WAITING:

                  nodeOsUpgradeScheduler.cancelReservationRequest(cancelOsUpgradeRequest);
                  break;

                case RUNNING:

                  nodeOsUpgradeScheduler.runningOperationFailureProcess(true, ErrorCode.OPERATION_CANCELED,
                      "running operation canceled.");
                  break;

                default:

                  throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR,
                      MessageFormat.format("target os upgrade request status error. status = {0}.",
                          cancelOsUpgradeRequest.getStatusEnum().getMessage()));
              }
            }
          }
        } catch (MsfException msfException) {
          logger.error(msfException.getMessage(), msfException);
          throw msfException;
        }

        return responseNodeOsUpgradeCancelData();
      }
    } finally {
      logger.methodEnd();
    }
  }

  private FcAsyncRequest getTargetAsyncRequest(FcNodeOsUpgradeScheduler nodeOsUpgradeScheduler) throws MsfException {
    try {
      logger.methodStart();
      FcAsyncRequest cancelOsUpgradeRequest = nodeOsUpgradeScheduler.getTargetAsyncRequest(request.getOperationId());
      if (cancelOsUpgradeRequest == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            MessageFormat.format("target resource is not found. operationId = {0}.", request.getOperationId()));
      }
      return cancelOsUpgradeRequest;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseNodeOsUpgradeCancelData() {
    try {
      logger.methodStart();

      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

}
