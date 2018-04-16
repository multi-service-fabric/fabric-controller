
package msf.mfc.node.nodes.spines;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.spines.data.SpineNodeRequest;

/**
 * Class to implement the asynchronous processing in Spine node deletion.
 *
 * @author NTT
 *
 */
public class MfcSpineNodeDeleteRunner extends MfcAbstractSpineNodeRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcSpineNodeDeleteRunner.class);

  private SpineNodeRequest request;

  /**
   * Constructor.
   * <p>
   * Set the request information from scenario as argument
   * </p>
   *
   * @param request
   *          Request
   */
  public MfcSpineNodeDeleteRunner(SpineNodeRequest request) {

    this.request = request;
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

        responseBase = sendSpineNodeDelete();
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

  protected RestResponseBase sendSpineNodeDelete() throws MsfException {
    try {
      logger.methodStart();

      SwCluster swCluster = MfcConfigManager.getInstance()
          .getSystemConfSwClusterData(Integer.valueOf(request.getClusterId())).getSwCluster();
      String fcControlAddress = swCluster.getFcControlAddress();
      int fcControlPort = swCluster.getFcControlPort();

      RestRequestData requestData = new RestRequestData(Integer.valueOf(request.getClusterId()), fcControlAddress,
          fcControlPort, MfcFcRequestUri.SPINE_NODE_DELETE.getHttpMethod(),
          MfcFcRequestUri.SPINE_NODE_DELETE.getUri(request.getClusterId(), request.getNodeId()), null,
          HttpStatus.ACCEPTED_202);
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
