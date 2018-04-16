
package msf.mfc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.reflect.TypeToken;

import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.MfcDbManager;
import msf.mfc.db.dao.slices.MfcL3CpDao;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.data.AsyncRequestsForLower;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateDeleteRequestBody;

/**
 * Implementation class for the reception process of operation result
 * notification in L3CP deletion.
 *
 * @author NTT
 *
 */
public class MfcL3CpDeleteNotifyRunner extends MfcAbstractL3CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3CpDeleteNotifyRunner.class);

  public MfcL3CpDeleteNotifyRunner(AsyncRequest asyncRequestForNotify) {
    this.asyncRequestForNotify = asyncRequestForNotify;
    setTimerTask(MfcScenarioUtil.createTimerTaskForMfc());
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      MfcL3CpDao l3CpDao = new MfcL3CpDao();
      Matcher matcher = MfcFcRequestUri.CP_DELETE.getUriPattern().matcher(asyncRequestForNotify.getRequestUri());
      String sliceId = "";
      String cpId = "";
      if (matcher.matches()) {
        sliceId = matcher.group(2);
        cpId = matcher.group(3);
      }
      sessionWrapper.openSession();

      MfcL3Slice l3Slice = getL3SliceAndCheck(sessionWrapper, sliceId);
      List<MfcL3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);
      getL3CpAndCheck(sessionWrapper, sliceId, cpId);

      sessionWrapper.beginTransaction();
      logger.performance("start get l3slice resources lock.");

      MfcDbManager.getInstance().getResourceLock(new ArrayList<>(), l3SliceList, sessionWrapper);
      logger.performance("end get l3slice resources lock.");

      getL3SliceAndCheck(sessionWrapper, sliceId);

      AsyncRequestsForLower lower = asyncRequestForNotify.getAsyncRequestsForLowerList().get(0);
      List<L3CpCreateDeleteRequestBody> requestBody = (List<L3CpCreateDeleteRequestBody>) JsonUtil
          .fromJson(lower.getRequestBody(), new TypeToken<ArrayList<L3CpCreateDeleteRequestBody>>() {
          });
      L3CpCreateDeleteRequestBody lowerBody = requestBody.get(0);
      String lowerCpId = getIdFromPath(lowerBody.getPath());
      switch (lowerBody.getOpEnum()) {
        case REMOVE:

          MfcL3Cp removeL3Cp = getL3CpAndCheck(sessionWrapper, sliceId, lowerCpId);
          l3CpDao.delete(sessionWrapper, removeL3Cp.getId());
          break;
        default:
          String message = logger.debug("Unexpected argument.(arg={0})", lowerBody.getOp());
          throw new IllegalArgumentException(message);
      }

      sessionWrapper.commit(MfcFcRequestUri.CP_DELETE);
      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      sessionWrapper.rollback();
      throw exp;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeRollbackImpl() throws MsfException {

    return null;
  }

}
