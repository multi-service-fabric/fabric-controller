
package msf.mfc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.google.gson.reflect.TypeToken;

import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3Slice;
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
 * notification in L3CP addition.
 *
 * @author NTT
 *
 */
public class MfcL3CpCreateNotifyRunner extends MfcAbstractL3CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3CpCreateNotifyRunner.class);

  public MfcL3CpCreateNotifyRunner(AsyncRequest asyncRequestForNotify) {
    this.asyncRequestForNotify = asyncRequestForNotify;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      MfcL3CpDao l3CpDao = new MfcL3CpDao();
      Matcher matcher = MfcFcRequestUri.CP_CREATE.getUriPattern().matcher(asyncRequestForNotify.getRequestUri());
      String sliceId = "";
      if (matcher.matches()) {
        sliceId = matcher.group(2);
      }
      sessionWrapper.openSession();

      MfcL3Slice l3Slice = getL3SliceAndCheck(sessionWrapper, sliceId);
      List<MfcL3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);

      sessionWrapper.beginTransaction();
      logger.performance("start get l3slice resources lock.");

      MfcDbManager.getInstance().getResourceLock(new ArrayList<>(), l3SliceList, sessionWrapper);
      logger.performance("end get l3slice resources lock.");

      MfcL3Slice l3SliceAfterLock = getL3SliceAndCheck(sessionWrapper, sliceId);

      AsyncRequestsForLower lower = asyncRequestForNotify.getAsyncRequestsForLowerList().get(0);
      List<L3CpCreateDeleteRequestBody> requestBody = (List<L3CpCreateDeleteRequestBody>) JsonUtil
          .fromJson(lower.getRequestBody(), new TypeToken<ArrayList<L3CpCreateDeleteRequestBody>>() {
          });
      L3CpCreateDeleteRequestBody lowerBody = requestBody.get(0);
      String createdCpId = getIdFromPath(lowerBody.getPath());
      switch (lowerBody.getOpEnum()) {
        case ADD:

          checkCpDuplicate(sessionWrapper, l3CpDao, sliceId, createdCpId);
          MfcL3Cp newL3Cp = makeNewL3Cp(l3SliceAfterLock, createdCpId, lowerBody.getValue().getClusterId());
          l3CpDao.create(sessionWrapper, newL3Cp);
          break;
        default:
          String message = logger.debug("Unexpected argument.(arg={0})", lowerBody.getOp());
          throw new IllegalArgumentException(message);
      }

      sessionWrapper.commit(MfcFcRequestUri.CP_CREATE);
      return createResponseForL3CpCreate(createdCpId);
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
