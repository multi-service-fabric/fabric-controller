
package msf.mfc.slice.slices.l3slice;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.MfcDbManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.data.AsyncRequestsForLower;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceUpdateAsyncResponseBody;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceUpdateRequestBody;

/**
 * Implementation class for the reception process of operation result
 * notification in L3 slice modification.
 *
 */
public class MfcL3SliceUpdateNotifyRunner extends MfcAbstractL3SliceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3SliceUpdateNotifyRunner.class);

  /**
   * Constructor.
   *
   * @param asyncRequestForNotify
   *          Table instance of asynchronous request information
   */
  public MfcL3SliceUpdateNotifyRunner(AsyncRequest asyncRequestForNotify) {
    this.asyncRequestForNotify = asyncRequestForNotify;
    setTimerTask(MfcScenarioUtil.createTimerTaskForMfc());
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();

      Matcher matcher = MfcFcRequestUri.SLICE_UPDATE.getUriPattern().matcher(asyncRequestForNotify.getRequestUri());
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
      MfcDbManager.getInstance().getL3SlicesLock(l3SliceList, sessionWrapper);
      logger.performance("end get l3slice resources lock.");

      MfcL3Slice l3SliceAfterLock = getL3SliceAndCheck(sessionWrapper, sliceId);

      L3SliceUpdateRequestBody requestBody = JsonUtil.fromJson(asyncRequestForNotify.getRequestBody(),
          L3SliceUpdateRequestBody.class, ErrorCode.UNDEFINED_ERROR);

      l3SliceAfterLock.setRemarkMenu(requestBody.getRemarkMenu());

      sessionWrapper.commit(MfcFcRequestUri.SLICE_UPDATE);
      return createResponse(l3SliceAfterLock);
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
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();

      Matcher matcher = MfcFcRequestUri.SLICE_UPDATE.getUriPattern().matcher(asyncRequestForNotify.getRequestUri());
      String sliceId = "";
      if (matcher.matches()) {
        sliceId = matcher.group(2);
      }
      sessionWrapper.openSession();

      MfcL3Slice l3Slice = getL3SliceAndCheck(sessionWrapper, sliceId);

      L3SliceUpdateRequestBody requestBody = JsonUtil.fromJson(asyncRequestForNotify.getRequestBody(),
          L3SliceUpdateRequestBody.class, ErrorCode.UNDEFINED_ERROR);

      requestBody.setRemarkMenu(l3Slice.getRemarkMenu());

      List<Integer> clusterIdList = new ArrayList<>();
      for (AsyncRequestsForLower lower : asyncRequestForNotify.getAsyncRequestsForLowerList()) {

        if (HttpStatus.isSuccess(lower.getResponseStatusCode())) {
          clusterIdList.add(lower.getId().getClusterId());
        }
      }

      return sendSliceUpdateRequest(sessionWrapper, SliceType.L3_SLICE, JsonUtil.toJson(requestBody), sliceId,
          clusterIdList, getLowerOperationIdMap(asyncRequestForNotify.getAsyncRequestsForLowerList()),
          RequestType.ROLLBACK);
    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      throw exp;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  protected RestResponseBase createResponse(MfcL3Slice l3Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });

      L3SliceUpdateAsyncResponseBody responseBody = new L3SliceUpdateAsyncResponseBody();
      List<String> updatedCpIdList = new ArrayList<>();
      for (MfcL3Cp l3Cp : l3Slice.getL3Cps()) {
        updatedCpIdList.add(l3Cp.getId().getCpId());
      }
      responseBody.setUpdatedCpIdList(updatedCpIdList);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }
}
