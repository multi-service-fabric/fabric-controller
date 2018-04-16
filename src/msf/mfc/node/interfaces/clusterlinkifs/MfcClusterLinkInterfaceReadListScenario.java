
package msf.mfc.node.interfaces.clusterlinkifs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.data.MfcClusterLinkIf;
import msf.mfc.db.dao.clusters.MfcClusterLinkIfDao;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfReadDetailListResponseBody;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfReadListResponseBody;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfRequest;

/**
 * Implementation class for the acquisition process of inter-cluster link
 * interface information list.
 *
 * @author NTT
 *
 */
public class MfcClusterLinkInterfaceReadListScenario
    extends MfcAbstractClusterLinkInterfaceScenarioBase<ClusterLinkIfRequest> {

  private ClusterLinkIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterLinkInterfaceReadListScenario.class);

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
  public MfcClusterLinkInterfaceReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(ClusterLinkIfRequest request) throws MsfException {

    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      if (request.getFormat() != null) {

        ParameterCheckUtil.checkNotNull(request.getFormatEnum());

      }
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

        List<MfcClusterLinkIf> clusterLinkIfs = mfcClusterLinkIfDao.readListBySwClusterId(sessionWrapper,
            request.getClusterId());

        if (clusterLinkIfs.isEmpty()) {
          MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();
          getSwCluster(sessionWrapper, mfcSwClusterDao, Integer.valueOf(request.getClusterId()));
        }

        responseBase = responseClusterLinkIfReadListData(clusterLinkIfs, request.getFormat());

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

  private RestResponseBase responseClusterLinkIfReadListData(List<MfcClusterLinkIf> clusterLinkIfs, String format)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "clusterLinkIfs" }, new Object[] { clusterLinkIfs });
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        ClusterLinkIfReadDetailListResponseBody body = new ClusterLinkIfReadDetailListResponseBody();
        if (!clusterLinkIfs.isEmpty()) {
          body.setClusterLinkIfIdList(getClusterLinkIfEntities(clusterLinkIfs));
        } else {

          body.setClusterLinkIfIdList(new ArrayList<>());
        }
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        ClusterLinkIfReadListResponseBody body = new ClusterLinkIfReadListResponseBody();
        body.setClusterLinkIfIdList(getClusterLinkIfIdList(clusterLinkIfs));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

}
