
package msf.fc.slice.slices;

import java.text.MessageFormat;
import java.util.List;

import msf.fc.common.config.FcConfigManager;
import msf.fc.rest.ec.core.operation.data.OperationResponseBody;
import msf.mfcfc.common.constant.EcEmControlStatus;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.EcControlStatusUtil;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.slice.slices.AbstractSliceRunnerBase;

/**
 * Abstract class to implement the common process of the asynchronous runner
 * process in the slice management.
 *
 * @author NTT
 *
 */
public abstract class FcAbstractSliceRunnerBase extends AbstractSliceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FcAbstractSliceRunnerBase.class);

  protected void checkRemarkMenuList(String remarkMenu) throws MsfException {
    try {
      logger.methodStart(new String[] { "remarkMenu" }, new Object[] { remarkMenu });
      List<String> remarkMenuList = FcConfigManager.getInstance().getQosRemarkMenuList();
      if (remarkMenu == null) {
        return;
      }
      for (String remarkMenuConf : remarkMenuList) {
        if (remarkMenu.equals(remarkMenuConf)) {
          logger.debug("remark menu matches config value.");
          return;
        }
      }
      String logMsg = MessageFormat.format("remark menu did not match config value, value = {0}", remarkMenu);
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);

    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendRequestToEc(String requestJson, EcRequestUri ecReuqestUri, String... uriParams)
      throws MsfException {
    try {
      logger.methodStart();
      RestRequestBase requestBase = new RestRequestBase();
      requestBase.setRequestBody(requestJson);
      RestResponseBase restResponse = RestClient.sendRequest(ecReuqestUri.getHttpMethod(),
          ecReuqestUri.getUri(uriParams), requestBase,
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlAddress(),
          FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort());
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  protected void commitTransaction(SessionWrapper sessionWrapper, RestResponseBase restResponse,
      int expectHttpStatusCode, boolean isCommit) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "restResponse" },
          new Object[] { sessionWrapper, restResponse });

      if (restResponse.getHttpStatusCode() == expectHttpStatusCode) {
        if (isCommit) {
          sessionWrapper.commit(EcEmControlStatus.SUCCESS);
        } else {
          sessionWrapper.rollback();
        }
      } else {

        OperationResponseBody responseBody = JsonUtil.fromJson(restResponse.getResponseBody(),
            OperationResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

        EcEmControlStatus ecEmControlStatus = EcControlStatusUtil.getStatusFromEcErrorCode(responseBody.getErrorCode());
        switch (ecEmControlStatus) {
          case EM_SUCCESS_BUT_EC_FAILED:
            if (isCommit) {

              sessionWrapper.commit(ecEmControlStatus);
            } else {
              sessionWrapper.rollback();
            }
            String logMsg1 = MessageFormat.format("ecErrorCode = {0}", responseBody.getErrorCode());
            logger.error(logMsg1);
            throw new MsfException(ErrorCode.EC_CONTROL_ERROR_EM_CONTROL_COMPLETED, logMsg1);
          case FAILED:

            String logMsg2 = MessageFormat.format("ecErrorCode = {0}", responseBody.getErrorCode());
            logger.error(logMsg2);
            throw new MsfException(ErrorCode.EC_CONTROL_ERROR, logMsg2);
          default:
            String logMsg3 = MessageFormat.format("unexpected ec status. status = {0}.", ecEmControlStatus.name());
            logger.error(logMsg3);
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg3);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }
}
