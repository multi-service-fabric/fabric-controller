
package msf.mfc.node.interfaces.clusterlinkifs;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.data.MfcClusterLinkIf;
import msf.mfc.db.dao.clusters.MfcClusterLinkIfDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfReadResponseBody;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfRequest;

/**
 * Implementation class for the acquisition process of inter-cluster link
 * interface information.
 *
 * @author NTT
 *
 */
public class MfcClusterLinkInterfaceReadScenario
    extends MfcAbstractClusterLinkInterfaceScenarioBase<ClusterLinkIfRequest> {

  private ClusterLinkIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterLinkInterfaceReadScenario.class);

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
  public MfcClusterLinkInterfaceReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(ClusterLinkIfRequest request) throws MsfException {
    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });
      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNumericId(request.getClusterLinkIfId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);

      this.request = request;
    } finally {
      logger.methodEnd();
    }
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

        MfcClusterLinkIf clusterLinkIf = getClusterLinkIf(sessionWrapper, mfcClusterLinkIfDao,
            Integer.valueOf(request.getClusterLinkIfId()));

        responseBase = responseClusterLinkIfReadData(clusterLinkIf);

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

  private RestResponseBase responseClusterLinkIfReadData(MfcClusterLinkIf clusterLinkIf) throws MsfException {
    try {
      logger.methodStart(new String[] { "clusterLinkIf" }, new Object[] { clusterLinkIf });

      ClusterLinkIfReadResponseBody body = new ClusterLinkIfReadResponseBody();

      body.setClusterLinkIf(getClusterLinkIfData(clusterLinkIf));
      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }

}
