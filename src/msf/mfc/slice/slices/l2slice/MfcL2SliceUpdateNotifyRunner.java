
package msf.mfc.slice.slices.l2slice;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2Slice;
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
import msf.mfcfc.slice.slices.l2slice.data.L2SliceUpdateAsyncResponseBody;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceUpdateRequestBody;

/**
 * Implementation class for the reception process of operation result
 * notification in L2 slice modification.
 *
 */
public class MfcL2SliceUpdateNotifyRunner extends MfcAbstractL2SliceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2SliceUpdateNotifyRunner.class);

  /**
   * Constructor.
   *
   * @param asyncRequestForNotify
   *          Table instance of asynchronous request information
   */
  public MfcL2SliceUpdateNotifyRunner(AsyncRequest asyncRequestForNotify) {
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

      MfcL2Slice l2Slice = getL2SliceAndCheck(sessionWrapper, sliceId);
      List<MfcL2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);

      sessionWrapper.beginTransaction();

      logger.performance("start get l2slice resources lock.");
      MfcDbManager.getInstance().getL2SlicesLock(l2SliceList, sessionWrapper);
      logger.performance("end get l2slice resources lock.");

      MfcL2Slice l2SliceAfterLock = getL2SliceAndCheck(sessionWrapper, sliceId);

      L2SliceUpdateRequestBody requestBody = JsonUtil.fromJson(asyncRequestForNotify.getRequestBody(),
          L2SliceUpdateRequestBody.class, ErrorCode.UNDEFINED_ERROR);

      l2SliceAfterLock.setRemarkMenu(requestBody.getRemarkMenu());

      sessionWrapper.commit(MfcFcRequestUri.SLICE_UPDATE);
      return createResponse(l2SliceAfterLock);
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

      MfcL2Slice l2Slice = getL2SliceAndCheck(sessionWrapper, sliceId);

      L2SliceUpdateRequestBody requestBody = JsonUtil.fromJson(asyncRequestForNotify.getRequestBody(),
          L2SliceUpdateRequestBody.class, ErrorCode.UNDEFINED_ERROR);

      requestBody.setRemarkMenu(l2Slice.getRemarkMenu());

      List<Integer> clusterIdList = new ArrayList<>();
      for (AsyncRequestsForLower lower : asyncRequestForNotify.getAsyncRequestsForLowerList()) {

        if (HttpStatus.isSuccess(lower.getResponseStatusCode())) {
          clusterIdList.add(lower.getId().getClusterId());
        }
      }

      return sendSliceUpdateRequest(sessionWrapper, SliceType.L2_SLICE, JsonUtil.toJson(requestBody), sliceId,
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

  protected RestResponseBase createResponse(MfcL2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice" }, new Object[] { l2Slice });

      L2SliceUpdateAsyncResponseBody responseBody = new L2SliceUpdateAsyncResponseBody();
      List<String> updatedCpIdList = new ArrayList<>();
      for (MfcL2Cp l2Cp : l2Slice.getL2Cps()) {
        updatedCpIdList.add(l2Cp.getId().getCpId());
      }
      responseBody.setUpdatedCpIdList(updatedCpIdList);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

}
