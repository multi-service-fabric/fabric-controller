
package msf.mfc.node.interfaces.clusterlinkifs;

import java.util.regex.Matcher;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.db.dao.clusters.MfcClusterLinkIfDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;

/**
 * Implementation class for the reception process of operation result
 * notification in the inter-cluster link interface deletion process.
 *
 * @author NTT
 *
 */
public class MfcClusterLinkInterfaceDeleteNotifyRunner extends MfcAbstractClusterLinkInterfaceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterLinkInterfaceDeleteNotifyRunner.class);

  public MfcClusterLinkInterfaceDeleteNotifyRunner(AsyncRequest asyncRequestForNotify) {
    this.asyncRequestForNotify = asyncRequestForNotify;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {

    try {
      logger.methodStart();

      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();

      try {
        sessionWrapper.openSession();
        sessionWrapper.beginTransaction();

        Integer clusterLinkIfId = getClusterLinkIfIdFromUri();

        MfcClusterLinkIfDao mfcClusterLinkIfDao = new MfcClusterLinkIfDao();
        mfcClusterLinkIfDao.delete(sessionWrapper, clusterLinkIfId);

        responseBase = responseClusterLinkIfDeleteData();

        sessionWrapper.commit(MfcFcRequestUri.CLUSTER_LINK_IF_DELETE);
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }
      return responseBase;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeRollbackImpl() throws MsfException {

    return null;
  }

  private Integer getClusterLinkIfIdFromUri() throws MsfException {
    try {
      logger.methodStart();
      Matcher clusterLinkIfmatcher = MfcFcRequestUri.CLUSTER_LINK_IF_DELETE.getUriPattern()
          .matcher(asyncRequestForNotify.getRequestUri());
      if (!clusterLinkIfmatcher.matches()) {

        throw new MsfException(ErrorCode.UNDEFINED_ERROR, "does not match the URI.");
      }

      return Integer.valueOf(clusterLinkIfmatcher.group(2));
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseClusterLinkIfDeleteData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    } finally {
      logger.methodEnd();
    }
  }
}
