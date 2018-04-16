
package msf.mfc.node.interfaces.clusterlinkifs;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.common.data.MfcClusterLinkIf;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.dao.clusters.MfcClusterLinkIfDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfRequest;

/**
 * Class to implement the asynchronous processing in inter-cluster link
 * interface deletion.
 *
 * @author NTT
 *
 */
public class MfcClusterLinkInterfaceDeleteRunner extends MfcAbstractClusterLinkInterfaceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterLinkInterfaceDeleteRunner.class);

  private ClusterLinkIfRequest request;

  /**
   * Constructor.
   * <p>
   * Set the request information from scenario as argument
   * </p>
   *
   * @param request
   *          Request
   */
  public MfcClusterLinkInterfaceDeleteRunner(ClusterLinkIfRequest request) {

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
        MfcClusterLinkIfDao mfcClusterLinkIfDao = new MfcClusterLinkIfDao();

        MfcClusterLinkIf mfcClusterLinkIf = getClusterLinkIf(sessionWrapper, mfcClusterLinkIfDao,
            Integer.valueOf(request.getClusterLinkIfId()));

        responseBase = sendClusterLinkInterfaceDelete(mfcClusterLinkIf);

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

  private MfcClusterLinkIf getClusterLinkIf(SessionWrapper sessionWrapper, MfcClusterLinkIfDao mfcClusterLinkIfDao,
      Integer clusterLinkIfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcClusterLinkIfDao", "clusterLinkIfId" },
          new Object[] { mfcClusterLinkIfDao, clusterLinkIfId });
      MfcClusterLinkIf mfcClusterLinkIf = mfcClusterLinkIfDao.read(sessionWrapper, clusterLinkIfId);
      if (mfcClusterLinkIf == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = clusterLinkIf");
      }
      return mfcClusterLinkIf;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendClusterLinkInterfaceDelete(MfcClusterLinkIf mfcClusterLinkIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcClusterLinkIf" }, new Object[] { mfcClusterLinkIf });

      Integer swClusterId = mfcClusterLinkIf.getSwCluster().getSwClusterId();

      SwCluster swCluster = MfcConfigManager.getInstance().getSystemConfSwClusterData(swClusterId).getSwCluster();
      String fcControlAddress = swCluster.getFcControlAddress();
      int fcControlPort = swCluster.getFcControlPort();

      RestRequestData requestData = new RestRequestData(swClusterId, fcControlAddress, fcControlPort,
          MfcFcRequestUri.CLUSTER_LINK_IF_DELETE.getHttpMethod(), MfcFcRequestUri.CLUSTER_LINK_IF_DELETE
              .getUri(String.valueOf(swClusterId), String.valueOf(mfcClusterLinkIf.getClusterLinkIfId())),
          null, HttpStatus.ACCEPTED_202);
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
