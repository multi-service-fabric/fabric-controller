
package msf.mfc.node.interfaces.clusterlinkifs;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.data.MfcClusterLinkIf;
import msf.mfc.common.data.MfcLagLink;
import msf.mfc.common.data.MfcPhysicalLink;
import msf.mfc.db.dao.clusters.MfcClusterLinkIfDao;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfc.node.clusters.MfcClusterDeleteNotifyRunner;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfCreateRequestBody;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfCreaterAsyncResponseBody;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for the reception process of operation result
 * notification in the inter-cluster link interface registration process.
 *
 * @author NTT
 *
 */
public class MfcClusterLinkInterfaceCreateNotifyRunner extends MfcAbstractClusterLinkInterfaceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterDeleteNotifyRunner.class);

  public MfcClusterLinkInterfaceCreateNotifyRunner(AsyncRequest asyncRequestForNotify) {
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

        ClusterLinkIfCreateRequestBody requestBody = JsonUtil.fromJson(
            asyncRequestForNotify.getAsyncRequestsForLowerList().get(0).getRequestBody(),
            ClusterLinkIfCreateRequestBody.class, ErrorCode.UNDEFINED_ERROR);

        MfcClusterLinkIf mfcClusterLinkIf = createClusterLinkIf(sessionWrapper, requestBody);

        responseBase = responseClusterLinkIfCreateData(mfcClusterLinkIf);

        sessionWrapper.commit(MfcFcRequestUri.CLUSTER_LINK_IF_CREATE);
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

  private MfcClusterLinkIf createClusterLinkIf(SessionWrapper sessionWrapper,
      ClusterLinkIfCreateRequestBody requestBody) throws MsfException {
    try {
      logger.methodStart(new String[] { "requestBody" }, new Object[] { requestBody });
      MfcClusterLinkIf mfcClusterLinkIf = new MfcClusterLinkIf();

      mfcClusterLinkIf.setClusterLinkIfId(Integer.valueOf(requestBody.getClusterLinkIfId()));
      mfcClusterLinkIf.setSwCluster(getSwCluster(sessionWrapper, new MfcSwClusterDao(),
          asyncRequestForNotify.getAsyncRequestsForLowerList().get(0).getId().getClusterId()));
      mfcClusterLinkIf.setOppositeSwClusterId(Integer.valueOf(requestBody.getOppositeClusterId()));
      mfcClusterLinkIf.setIgpCost(requestBody.getIgpCost());
      mfcClusterLinkIf.setPortStatus((requestBody.getPortStatus() == null) ? true : requestBody.getPortStatus());
      mfcClusterLinkIf.setIpv4Addr(requestBody.getIpv4Address());
      mfcClusterLinkIf.setTrafficThreshold(requestBody.getTrafficThreshold());
      if (requestBody.getLagLink() != null) {
        MfcLagLink mfcLagLink = new MfcLagLink();
        mfcLagLink.setClusterLinkIf(mfcClusterLinkIf);
        mfcLagLink.setClusterLinkIfId(mfcClusterLinkIf.getClusterLinkIfId());
        mfcLagLink.setNodeId(Integer.valueOf(requestBody.getLagLink().getNodeId()));
        mfcLagLink.setLagIfId(Integer.valueOf(requestBody.getLagLink().getLagIfId()));
        mfcLagLink.setOppositeNodeId(Integer.valueOf(requestBody.getLagLink().getOppositeNodeId()));
        mfcLagLink.setOppositeLagIfId(Integer.valueOf(requestBody.getLagLink().getOppositeLagIfId()));
        mfcClusterLinkIf.setLagLink(mfcLagLink);
      } else {
        MfcPhysicalLink mfcPhysicalLink = new MfcPhysicalLink();
        mfcPhysicalLink.setClusterLinkIf(mfcClusterLinkIf);
        mfcPhysicalLink.setClusterLinkIfId(mfcClusterLinkIf.getClusterLinkIfId());
        mfcPhysicalLink.setNodeId(Integer.valueOf(requestBody.getPhysicalLink().getNodeId()));
        mfcPhysicalLink.setPhysicalIfId(requestBody.getPhysicalLink().getPhysicalIfId());
        mfcPhysicalLink.setBreakoutIfId(requestBody.getPhysicalLink().getBreakoutIfId());
        mfcPhysicalLink.setOppositeNodeId(Integer.valueOf(requestBody.getPhysicalLink().getOppositeNodeId()));
        mfcPhysicalLink.setOppositePhysicalIfId(requestBody.getPhysicalLink().getOppositeIfId());
        mfcPhysicalLink.setOppositeBreakoutIfId(requestBody.getPhysicalLink().getOppositeBreakoutIfId());
        mfcClusterLinkIf.setPhysicalLink(mfcPhysicalLink);
      }

      MfcClusterLinkIfDao mfcClusterLinkIfDao = new MfcClusterLinkIfDao();
      mfcClusterLinkIfDao.create(sessionWrapper, mfcClusterLinkIf);
      return mfcClusterLinkIf;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseClusterLinkIfCreateData(MfcClusterLinkIf mfcClusterLinkIf) {
    try {
      logger.methodStart(new String[] { "mfcClusterLinkIf" }, new Object[] { mfcClusterLinkIf });
      ClusterLinkIfCreaterAsyncResponseBody body = new ClusterLinkIfCreaterAsyncResponseBody();
      body.setClusterLinkIfId(String.valueOf(mfcClusterLinkIf.getClusterLinkIfId()));
      return createRestResponse(body, HttpStatus.CREATED_201);
    } finally {
      logger.methodEnd();
    }
  }
}
