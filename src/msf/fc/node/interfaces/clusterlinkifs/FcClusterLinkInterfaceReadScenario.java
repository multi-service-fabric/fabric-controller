
package msf.fc.node.interfaces.clusterlinkifs;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcClusterLinkIf;
import msf.fc.db.dao.clusters.FcClusterLinkIfDao;
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
 * Implementation class for the inter-cluster link interface information
 * acquisition.
 *
 * @author NTT
 */
public class FcClusterLinkInterfaceReadScenario
    extends FcAbstractClusterLinkInterfaceScenarioBase<ClusterLinkIfRequest> {
  private ClusterLinkIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcClusterLinkInterfaceReadScenario.class);

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments.
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public FcClusterLinkInterfaceReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(ClusterLinkIfRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

      ParameterCheckUtil.checkNotNullAndLength(request.getClusterLinkIfId());
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
        FcClusterLinkIfDao fcClusterLinkIfDao = new FcClusterLinkIfDao();

        FcClusterLinkIf fcClusterLinkIf = getClusterLinkInterface(sessionWrapper, fcClusterLinkIfDao,
            Long.parseLong(request.getClusterLinkIfId()));

        responseBase = responseClusterLinkInterfaceReadData(fcClusterLinkIf);
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

  private FcClusterLinkIf getClusterLinkInterface(SessionWrapper sessionWrapper, FcClusterLinkIfDao fcClusterLinkIfDao,
      Long clusterLinkIfId) throws MsfException {
    try {
      logger.methodStart();
      FcClusterLinkIf fcClusterLinkIf = fcClusterLinkIfDao.read(sessionWrapper, clusterLinkIfId);
      if (fcClusterLinkIf == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource is not found. parameters = fcClusterLinkIf");
      }
      return fcClusterLinkIf;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseClusterLinkInterfaceReadData(FcClusterLinkIf fcClusterLinkIf) throws MsfException {
    try {
      logger.methodStart();
      ClusterLinkIfReadResponseBody body = new ClusterLinkIfReadResponseBody();
      body.setClusterLinkIf(getClusterLinkInterfaceEntity(fcClusterLinkIf));
      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }
}
