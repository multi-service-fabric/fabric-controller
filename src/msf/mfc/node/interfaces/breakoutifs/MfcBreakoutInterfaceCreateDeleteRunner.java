
package msf.mfc.node.interfaces.breakoutifs;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfCreateDeleteRequestBody;
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfRequest;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Class to implement the asynchronous processing in breakout interface
 * registration/deletion.
 *
 * @author NTT
 *
 */
public class MfcBreakoutInterfaceCreateDeleteRunner extends MfcAbstractBreakoutInterfaceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcBreakoutInterfaceCreateDeleteRunner.class);

  private BreakoutIfRequest request;
  private List<BreakoutIfCreateDeleteRequestBody> requestBody;

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
  public MfcBreakoutInterfaceCreateDeleteRunner(BreakoutIfRequest request,
      List<BreakoutIfCreateDeleteRequestBody> requestBody) {

    this.request = request;
    this.requestBody = requestBody;
    setTimerTask(MfcScenarioUtil.createTimerTaskForMfc());
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();

      try {
        sessionWrapper.openSession();

        MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();

        getSwCluster(sessionWrapper, mfcSwClusterDao, Integer.valueOf(request.getClusterId()));

        responseBase = sendBreakoutInterfaceCreateDelete();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendBreakoutInterfaceCreateDelete() throws MsfException {
    try {
      logger.methodStart();

      RestRequestBase restRequest = new RestRequestBase();
      restRequest.setRequestBody(JsonUtil.toJson(requestBody));

      SwCluster swCluster = MfcConfigManager.getInstance()
          .getSystemConfSwClusterData(Integer.valueOf(request.getClusterId())).getSwCluster();
      String fcControlAddress = swCluster.getFcControlAddress();
      int fcControlPort = swCluster.getFcControlPort();

      RestRequestData requestData = new RestRequestData(Integer.valueOf(request.getClusterId()), fcControlAddress,
          fcControlPort,
          MfcFcRequestUri.BREAKOUT_IF_CREATE_DELETE.getHttpMethod(), MfcFcRequestUri.BREAKOUT_IF_CREATE_DELETE
              .getUri(request.getClusterId(), request.getFabricType(), request.getNodeId()),
          restRequest, HttpStatus.ACCEPTED_202);
      RestResponseData restResponseData = sendRequest(requestData, RequestType.REQUEST);

      if (restResponseData.getResponse().getHttpStatusCode() != HttpStatus.ACCEPTED_202) {

        String errorMsg = MessageFormat.format("HttpStatusCode = {0}",
            restResponseData.getResponse().getHttpStatusCode());
        logger.error(errorMsg);
        return createErrorResponse(restResponseData);
      }

      return restResponseData.getResponse();
    } finally {
      logger.methodEnd();
    }
  }
}
