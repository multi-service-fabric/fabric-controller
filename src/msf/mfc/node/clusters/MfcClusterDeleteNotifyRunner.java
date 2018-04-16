
package msf.mfc.node.clusters;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.google.gson.reflect.TypeToken;

import msf.mfc.common.data.MfcAsyncRequestsForLowerPK;
import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.MfcDbManager;
import msf.mfc.db.dao.clusters.MfcClusterLinkIfDao;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfc.db.dao.common.MfcAsyncRequestsForLowerDao;
import msf.mfc.db.dao.slices.MfcL2CpDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.data.AsyncRequestsForLower;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateDeleteRequestBody;

/**
 * Implementation class for the reception process of operation result
 * notification in the SW cluster reduction process.
 *
 * @author NTT
 *
 */
public class MfcClusterDeleteNotifyRunner extends MfcAbstractClusterRunnerBase {
  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterDeleteNotifyRunner.class);

  public MfcClusterDeleteNotifyRunner(AsyncRequest asyncRequestForNotify) {
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

        boolean isSliceLock = false;

        for (AsyncRequestsForLower asyncRequestsForLower : asyncRequestForNotify.getAsyncRequestsForLowerList()) {
          String requestUri = asyncRequestsForLower.getRequestUri();

          Matcher l2CpDeleteMatcher = MfcFcRequestUri.CP_CREATE_DELETE.getUriPattern().matcher(requestUri);
          Matcher clusterLinkIfMatcher = MfcFcRequestUri.CLUSTER_LINK_IF_DELETE.getUriPattern().matcher(requestUri);
          if (l2CpDeleteMatcher.matches()) {

            String sliceId = l2CpDeleteMatcher.group(2);
            if (!isSliceLock) {

              MfcL2Slice l2Slice = getL2SliceAndCheck(sessionWrapper, sliceId);
              List<MfcL2Slice> l2SliceList = new ArrayList<>();
              l2SliceList.add(l2Slice);
              logger.performance("start get l2slice resources lock.");

              MfcDbManager.getInstance().getResourceLock(l2SliceList, new ArrayList<>(), sessionWrapper);
              logger.performance("end get l2slice resources lock.");
              isSliceLock = true;
            }
            deleteL2CpsFromDb(sessionWrapper, sliceId, asyncRequestsForLower);
          } else if (clusterLinkIfMatcher.matches()) {

            deleteClusterLinkIfsFromDb(sessionWrapper, Integer.valueOf(clusterLinkIfMatcher.group(2)));
          } else {

            throw new MsfException(ErrorCode.UNDEFINED_ERROR, "does not match the URI.");
          }

          deletAsyncRequestsForLowerFromDb(sessionWrapper, asyncRequestsForLower);
        }

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

        Matcher clusterDeleteMatcher = MfcFcRequestUri.SW_CLUSTER_DELETE.getUriPattern()
            .matcher(asyncRequestForNotify.getRequestUri());

        if (!clusterDeleteMatcher.matches()) {

          throw new MsfException(ErrorCode.UNDEFINED_ERROR, "does not match the URI.");
        }

        MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();
        MfcSwCluster swCluster = getSwClusterForDelete(sessionWrapper, mfcSwClusterDao,
            Integer.valueOf(clusterDeleteMatcher.group(1)));

        sessionWrapper.beginTransaction();

        if (null != (responseBase = deleteL2Cps(sessionWrapper, swCluster))) {
          return responseBase;
        }

        if (null != (responseBase = deleteClusterLinkIf(sessionWrapper, swCluster))) {
          return responseBase;
        }

        mfcSwClusterDao.delete(sessionWrapper, swCluster.getSwClusterId());

        checkSwClusters(sessionWrapper, mfcSwClusterDao);

        responseBase = responseSwClusterDeleteData();

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

  @Override
  protected RestResponseBase executeRollbackImpl() throws MsfException {

    return null;
  }

  private void deleteL2CpsFromDb(SessionWrapper sessionWrapper, String sliceId,
      AsyncRequestsForLower asyncRequestsForLower) throws MsfException {
    try {
      logger.methodStart();
      MfcL2CpDao l2CpDao = new MfcL2CpDao();

      List<L2CpCreateDeleteRequestBody> lowerBodyList = (List<L2CpCreateDeleteRequestBody>) JsonUtil
          .fromJson(asyncRequestsForLower.getRequestBody(), new TypeToken<ArrayList<L2CpCreateDeleteRequestBody>>() {
          }, ErrorCode.UNDEFINED_ERROR);
      for (L2CpCreateDeleteRequestBody lowerBody : lowerBodyList) {
        String lowerCpId = getIdFromPath(lowerBody.getPath());
        switch (lowerBody.getOpEnum()) {
          case REPLACE:
            MfcL2Cp updateL2Cp = getL2CpAndCheck(sessionWrapper, sliceId, lowerCpId);

            updateL2Cp.setEsi(null);
            l2CpDao.update(sessionWrapper, updateL2Cp);
            break;
          case REMOVE:
            MfcL2Cp removeL2Cp = getL2CpAndCheck(sessionWrapper, sliceId, lowerCpId);
            l2CpDao.delete(sessionWrapper, removeL2Cp.getId());
            break;
          default:
            String message = logger.debug("Unexpected argument.(arg={0})", lowerBody.getOp());
            throw new IllegalArgumentException(message);
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void deleteClusterLinkIfsFromDb(SessionWrapper sessionWrapper, Integer clusterLinkIfId) throws MsfException {
    try {
      logger.methodStart();
      MfcClusterLinkIfDao mfcClusterLinkIfDao = new MfcClusterLinkIfDao();
      mfcClusterLinkIfDao.delete(sessionWrapper, clusterLinkIfId);
    } finally {
      logger.methodEnd();
    }
  }

  private void deletAsyncRequestsForLowerFromDb(SessionWrapper sessionWrapper,
      AsyncRequestsForLower asyncRequestsForLower) throws MsfException {
    try {
      logger.methodStart();
      MfcAsyncRequestsForLowerDao mfcAsyncRequestsForLowerDao = new MfcAsyncRequestsForLowerDao();
      MfcAsyncRequestsForLowerPK pk = new MfcAsyncRequestsForLowerPK();
      pk.setClusterId(asyncRequestsForLower.getId().getClusterId());
      pk.setRequestOperationId(asyncRequestsForLower.getId().getRequestOperationId());
      mfcAsyncRequestsForLowerDao.delete(sessionWrapper, pk);
    } finally {
      logger.methodEnd();
    }
  }
}
