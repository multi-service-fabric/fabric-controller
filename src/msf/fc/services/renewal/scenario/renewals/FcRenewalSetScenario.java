
package msf.fc.services.renewal.scenario.renewals;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.core.status.FcSystemStatusManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RenewalStatusType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.services.renewal.common.constant.RenewalSetAction;
import msf.mfcfc.services.renewal.scenario.renewals.data.RenewalRequest;
import msf.mfcfc.services.renewal.scenario.renewals.data.RenewalUpdateRequestBody;
import msf.mfcfc.services.renewal.scenario.renewals.data.RenewalUpdateResponseBody;
import msf.mfcfc.services.renewal.scenario.renewals.data.entity.RenewalStatusEntity;

/**
 * Implementation class for the controller renewal.
 *
 * @author NTT
 *
 */
public class FcRenewalSetScenario extends FcAbstractRenewalScenarioBase<RenewalRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcRenewalSetScenario.class);

  private RenewalUpdateRequestBody requestBody;

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
  public FcRenewalSetScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      try {

        RenewalStatusType renewalStatus = null;
        if (requestBody.getActionEnum().equals(RenewalSetAction.START)) {
          renewalStatus = RenewalStatusType.RENEWAL_IN_PROGRESS;
        } else {
          renewalStatus = RenewalStatusType.NONE;
        }

        SystemStatus updateStatus = new SystemStatus();
        updateStatus.setRenewalStatusEnum(renewalStatus);
        FcSystemStatusManager.getInstance().changeSystemStatus(updateStatus);
      } catch (MsfException exp) {
        logger.warn("failed to change renewal status.");
        if (!exp.getErrorCode().equals(ErrorCode.TRANSITION_STATUS_ERROR)) {

          throw exp;
        }
      }
      return createResponse();
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(RenewalRequest request) throws MsfException {
    try {
      logger.methodStart();

      if (request.getController() != null) {
        checkRenewalController(request.getController());
      }

      if (request.getCluster() != null) {
        ParameterCheckUtil.checkClusterForFc(request.getCluster(),
            FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId());
      }

      RenewalUpdateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          RenewalUpdateRequestBody.class);
      requestBody.validate();

      this.request = request;
      this.requestBody = requestBody;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse() {
    try {
      logger.methodStart();
      List<RenewalStatusEntity> controllerRenewalStatusList = makeRenewalStatusEntityList();

      RenewalUpdateResponseBody responseBody = new RenewalUpdateResponseBody();
      responseBody.setControllerRenewalStatuses(controllerRenewalStatusList);

      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

}
