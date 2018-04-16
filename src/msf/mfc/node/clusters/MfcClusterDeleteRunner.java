
package msf.mfc.node.clusters;

import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.ClusterProvisioningStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.clusters.data.SwClusterRequest;

/**
 * Implementation class for the asynchronous process in the SW cluster reduction
 * process.
 *
 * @author NTT
 *
 */
public class MfcClusterDeleteRunner extends MfcAbstractClusterRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterDeleteRunner.class);

  private SwClusterRequest request;

  /**
   * Constructor.
   * <p>
   * Set the request information from scenario as argument
   * </p>
   *
   * @param request
   *          Request
   */
  public MfcClusterDeleteRunner(SwClusterRequest request) {

    this.request = request;
    setTimerTask(MfcScenarioUtil.createTimerTaskForMfc());
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {

    try {
      logger.methodStart();

      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();

      MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();
      MfcSwCluster swCluster;
      try {
        sessionWrapper.openSession();

        swCluster = getSwClusterForDelete(sessionWrapper, mfcSwClusterDao, Integer.valueOf(request.getClusterId()));

        sessionWrapper.beginTransaction();
        swCluster.setClusterStatus(ClusterProvisioningStatus.UNSETTING.getCode());
        mfcSwClusterDao.update(sessionWrapper, swCluster);
        sessionWrapper.commit();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      sessionWrapper = new SessionWrapper();

      try {

        sessionWrapper.openSession();
        sessionWrapper.beginTransaction();

        swCluster = getSwClusterForDelete(sessionWrapper, mfcSwClusterDao, Integer.valueOf(request.getClusterId()));

        if (null != (responseBase = deleteL2Cps(sessionWrapper, swCluster))) {
          return responseBase;
        }

        if (null != (responseBase = deleteClusterLinkIf(sessionWrapper, swCluster))) {
          return responseBase;
        }

        mfcSwClusterDao.delete(sessionWrapper, swCluster.getSwClusterId());

        checkSwClusters(sessionWrapper, mfcSwClusterDao);

        responseBase = responseSwClusterDeleteData();

        setOperationEndFlag(true);

        sessionWrapper.commit();
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
}
