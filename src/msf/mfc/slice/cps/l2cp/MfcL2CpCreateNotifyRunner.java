
package msf.mfc.slice.cps.l2cp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.reflect.TypeToken;

import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.MfcDbManager;
import msf.mfc.db.dao.slices.MfcL2CpDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.data.AsyncRequestsForLower;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateDeleteRequestBody;

/**
 * Implementation class for the reception process of operation result
 * notification in L2CP addition.
 *
 * @author NTT
 *
 */
public class MfcL2CpCreateNotifyRunner extends MfcAbstractL2CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2CpCreateNotifyRunner.class);

  /**
   * Constructor.
   *
   * @param asyncRequestForNotify
   *          Table instance of asynchronous request information
   */
  public MfcL2CpCreateNotifyRunner(AsyncRequest asyncRequestForNotify) {
    this.asyncRequestForNotify = asyncRequestForNotify;
    setTimerTask(MfcScenarioUtil.createTimerTaskForMfc());
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      MfcL2CpDao l2CpDao = new MfcL2CpDao();
      Matcher matcher = MfcFcRequestUri.CP_CREATE.getUriPattern().matcher(asyncRequestForNotify.getRequestUri());
      String sliceId = "";
      if (matcher.matches()) {
        sliceId = matcher.group(2);
      }
      sessionWrapper.openSession();

      MfcL2Slice l2Slice = getL2SliceAndCheck(sessionWrapper, sliceId);
      List<MfcL2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);

      sessionWrapper.beginTransaction();
      logger.performance("start get l2slice resources lock.");

      MfcDbManager.getInstance().getResourceLock(l2SliceList, new ArrayList<>(), sessionWrapper);
      logger.performance("end get l2slice resources lock.");

      MfcL2Slice l2SliceAfterLock = getL2SliceAndCheck(sessionWrapper, sliceId);

      String createdCpId = null;

      for (AsyncRequestsForLower lower : asyncRequestForNotify.getAsyncRequestsForLowerList()) {
        List<L2CpCreateDeleteRequestBody> lowerBodyList = (List<L2CpCreateDeleteRequestBody>) JsonUtil
            .fromJson(lower.getRequestBody(), new TypeToken<ArrayList<L2CpCreateDeleteRequestBody>>() {
            }, ErrorCode.UNDEFINED_ERROR);
        for (L2CpCreateDeleteRequestBody lowerBody : lowerBodyList) {
          String lowerCpId = getIdFromPath(lowerBody.getPath());
          switch (lowerBody.getOpEnum()) {
            case ADD:
              createdCpId = lowerCpId;

              checkCpDuplicate(sessionWrapper, l2CpDao, sliceId, lowerCpId);
              MfcL2Cp newL2Cp = makeNewL2Cp(sessionWrapper, l2SliceAfterLock.getSliceId(), lowerCpId,
                  Integer.valueOf(lowerBody.getValue().getClusterId()));
              newL2Cp.setEsi(lowerBody.getValue().getEsi());
              l2CpDao.create(sessionWrapper, newL2Cp);
              break;
            case REPLACE:
              MfcL2Cp updateL2Cp = getL2CpAndCheck(sessionWrapper, sliceId, lowerCpId);
              updateL2Cp.setEsi(lowerBody.getValue().getEsi());
              l2CpDao.update(sessionWrapper, updateL2Cp);
              break;
            default:
              String message = logger.debug("Unexpected argument.(arg={0})", lowerBody.getOp());
              throw new IllegalArgumentException(message);
          }
        }
      }

      sessionWrapper.commit(MfcFcRequestUri.CP_CREATE);
      return createResponseForL2CpCreate(createdCpId);
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
    try {
      logger.methodStart();
      Matcher matcher = MfcFcRequestUri.CP_CREATE.getUriPattern().matcher(asyncRequestForNotify.getRequestUri());
      String sliceId = "";
      if (matcher.matches()) {
        sliceId = matcher.group(2);
      }

      for (AsyncRequestsForLower lower : asyncRequestForNotify.getAsyncRequestsForLowerList()) {
        List<L2CpCreateDeleteRequestBody> lowerBodyList = (List<L2CpCreateDeleteRequestBody>) JsonUtil
            .fromJson(lower.getRequestBody(), new TypeToken<ArrayList<L2CpCreateDeleteRequestBody>>() {
            }, ErrorCode.UNDEFINED_ERROR);
        if (HttpStatus.isSuccess(lower.getResponseStatusCode())) {
          for (L2CpCreateDeleteRequestBody lowerBody : lowerBodyList) {
            switch (lowerBody.getOpEnum()) {
              case ADD:

                L2CpCreateDeleteRequestBody deleteBody = makeL2CpCreateDeleteRequestBodyForDelete(
                    getIdFromPath(lowerBody.getPath()));
                putL2CpCreateDeleteRequestBodyForMap(lower.getId().getClusterId(), deleteBody);
                break;
              case REPLACE:

                L2CpCreateDeleteRequestBody updateBody = makeL2CpCreateDeleteRequestBodyForUpdate(
                    getIdFromPath(lowerBody.getPath()), "0");
                putL2CpCreateDeleteRequestBodyForMap(lower.getId().getClusterId(), updateBody);
                break;
              default:
                String message = logger.debug("Unexpected argument.(arg={0})", lowerBody.getOp());
                throw new IllegalArgumentException(message);
            }
          }
        }
      }

      List<RestRequestData> requestDataList = makeSendRequestDataList(sliceId,
          getLowerOperationIdMap(asyncRequestForNotify.getAsyncRequestsForLowerList()));

      List<RestResponseData> responseDataList = sendRequest(requestDataList, RequestType.ROLLBACK);
      if (checkResponseAllSuccess(responseDataList)) {
        return new RestResponseBase(HttpStatus.OK_200, (String) null);
      } else {
        return createErrorResponse(responseDataList, null);
      }
    } catch (

    MsfException exp) {
      logger.error(exp.getMessage(), exp);
      throw exp;
    } finally {
      logger.methodEnd();
    }
  }
}
