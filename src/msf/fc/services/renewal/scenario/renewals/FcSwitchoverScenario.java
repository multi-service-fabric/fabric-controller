
package msf.fc.services.renewal.scenario.renewals;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.CommandUtil;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.services.renewal.common.constant.EcRequestUri;
import msf.mfcfc.services.renewal.common.constant.SwitchoverControllerType;
import msf.mfcfc.services.renewal.scenario.renewals.data.RenewalRequest;

/**
 * Implementation class for the controller switchover instruction.
 *
 * @author NTT
 *
 */
public class FcSwitchoverScenario extends FcAbstractRenewalScenarioBase<RenewalRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcSwitchoverScenario.class);

  private static final String SWITCH_OVER_SCRIPT_RELATIVE_DIR = "../bin/services/renewal/";

  private static final String SWITCH_OVER_SCRIPT_FILE_NAME = "fc_switch_over.sh";

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
  public FcSwitchoverScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      List<SwitchoverControllerType> controllerList = getSwitchoverControllers(request.getController());

      String controllerForEcEm = makeControllerForEcEm(controllerList);
      if (controllerForEcEm != null) {
        sendControllerSwitch(controllerForEcEm);
      }

      if (controllerList.contains(SwitchoverControllerType.FC)) {
        CommandUtil.execCommand(
            new ProcessBuilder("env", "LANG=C", SWITCH_OVER_SCRIPT_RELATIVE_DIR + SWITCH_OVER_SCRIPT_FILE_NAME));
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
        checkSwitchoverController(request.getController());
      }

      if (request.getCluster() != null) {
        ParameterCheckUtil.checkClusterForFc(request.getCluster(),
            FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId());
      }

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private String makeControllerForEcEm(List<SwitchoverControllerType> controllerList) {
    String controllerForEcEm = null;
    if (controllerList.contains(SwitchoverControllerType.EC) && controllerList.contains(SwitchoverControllerType.EM)) {
      controllerForEcEm = SwitchoverControllerType.EC.getMessage() + "+" + SwitchoverControllerType.EM.getMessage();
    } else if (controllerList.contains(SwitchoverControllerType.EC)) {
      controllerForEcEm = SwitchoverControllerType.EC.getMessage();
    } else if (controllerList.contains(SwitchoverControllerType.EM)) {
      controllerForEcEm = SwitchoverControllerType.EM.getMessage();
    }
    return controllerForEcEm;
  }

  private void sendControllerSwitch(String controller) throws MsfException {
    try {
      logger.methodStart(new String[] { "controller" }, new Object[] { controller });

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();
      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.SWITCH_SYSTEM_TYPE.getHttpMethod(),
          EcRequestUri.SWITCH_SYSTEM_TYPE.getUri("controller=" + controller), null, ecControlIpAddress, ecControlPort);

      ErrorInternalResponseBody responseBody = new ErrorInternalResponseBody();
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(), ErrorInternalResponseBody.class,
            ErrorCode.EC_CONTROL_ERROR);
      }

      checkResponseFromEc(restResponseBase, responseBody, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkResponseFromEc(RestResponseBase restResponseBase, AbstractInternalResponseBody responseBody,
      int httpStatusCode) throws MsfException {
    try {
      logger.methodStart(new String[] { "restResponseBase", "responseBody", "httpStatusCode" },
          new Object[] { restResponseBase, responseBody, httpStatusCode });

      if (httpStatusCode != restResponseBase.getHttpStatusCode()) {

        String errorMsg = MessageFormat.format("HttpStatusCode={0}, ErrorCode={1}",
            restResponseBase.getHttpStatusCode(), responseBody.getErrorCode());
        logger.error(errorMsg);
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse() {
    try {
      logger.methodStart();
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, (String) null);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

}
