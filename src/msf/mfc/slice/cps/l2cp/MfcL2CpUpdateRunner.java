
package msf.mfc.slice.cps.l2cp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.MfcDbManager;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;
import msf.mfcfc.slice.cps.l2cp.data.L2CpUpdateRequestBody;

/**
 * Implementation class for the asynchronous process in the L2CP modification
 * process.
 *
 * @author NTT
 *
 */
public class MfcL2CpUpdateRunner extends MfcAbstractL2CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2CpUpdateRunner.class);

  private L2CpUpdateRequestBody requestBody;

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for L2CP control
   * @param requestBody
   *          Request Body part for L2CP modification
   */
  public MfcL2CpUpdateRunner(L2CpRequest request, L2CpUpdateRequestBody requestBody) {
    this.request = request;
    this.requestBody = requestBody;
    setTimerTask(MfcScenarioUtil.createTimerTaskForMfc());
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();

      MfcL2Slice l2Slice = getL2SliceAndCheck(sessionWrapper, request.getSliceId());
      List<MfcL2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);
      getL2CpAndCheck(sessionWrapper, request.getSliceId(), request.getCpId());

      sessionWrapper.beginTransaction();
      logger.performance("start get l2slice resources lock.");

      MfcDbManager.getInstance().getL2SlicesLock(l2SliceList, sessionWrapper);
      logger.performance("end get l2slice resources lock.");

      getL2SliceAndCheck(sessionWrapper, request.getSliceId());
      MfcL2Cp l2CpAfterLock = getL2CpAndCheck(sessionWrapper, request.getSliceId(), request.getCpId());

      return sendAsyncRequestAndCreateResponse(sessionWrapper, false, request.getSliceId(), makeSendRequestDataList(
          l2CpAfterLock.getSwCluster().getSwClusterId(), request.getSliceId(), request.getCpId()), RequestType.REQUEST);

    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      sessionWrapper.rollback();
      throw exp;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  private List<RestRequestData> makeSendRequestDataList(int clusterId, String sliceId, String cpId) {
    try {
      logger.methodStart();
      RestRequestData requestData = new RestRequestData();
      requestData.setClusterId(clusterId);
      requestData.setExpectHttpStatusCode(HttpStatus.ACCEPTED_202);
      requestData.setHttpMethod(HttpMethod.PUT);
      requestData.setIpAddress(
          MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlAddress());
      requestData.setPort(
          MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlPort());
      requestData.setTargetUri(MfcFcRequestUri.CP_UPDATE.getUri(SliceType.L2_SLICE.getMessage(), sliceId, cpId));
      RestRequestBase requestBase = new RestRequestBase();

      requestBase.setRequestBody(JsonUtil.toJson(requestBody));
      requestData.setRequest(requestBase);
      List<RestRequestData> requestDataList = new ArrayList<>();
      requestDataList.add(requestData);
      return requestDataList;
    } finally {
      logger.methodEnd();
    }
  }
}
