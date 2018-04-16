
package msf.mfc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3Slice;
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
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;
import msf.mfcfc.slice.cps.l3cp.data.L3CpStaticRouteCreateDeleteRequestBody;

/**
 * Class to implement the asynchronous processing in L3CP static route
 * addition/deletion.
 *
 * @author NTT
 *
 */
public class MfcL3CpStaticRouteCreateDeleteRunner extends MfcAbstractL3CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3CpStaticRouteCreateDeleteRunner.class);

  private List<L3CpStaticRouteCreateDeleteRequestBody> requestBody;

  protected List<L3CpStaticRouteCreateDeleteRequestBody> staticRouteUpdateEntityList = new ArrayList<>();

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for L3CP control
   * @param requestBody
   *          Request body for L3CP static route addition/deletion
   */
  public MfcL3CpStaticRouteCreateDeleteRunner(L3CpRequest request,
      List<L3CpStaticRouteCreateDeleteRequestBody> requestBody) {
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

      MfcL3Slice l3Slice = getL3SliceAndCheck(sessionWrapper, request.getSliceId());
      List<MfcL3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);

      getL3CpAndCheck(sessionWrapper, request.getSliceId(), request.getCpId());

      sessionWrapper.beginTransaction();

      logger.performance("start get l3slice resources lock.");
      MfcDbManager.getInstance().getResourceLock(new ArrayList<>(), l3SliceList, sessionWrapper);
      logger.performance("end get l3slice resources lock.");

      getL3SliceAndCheck(sessionWrapper, request.getSliceId());

      MfcL3Cp l3CpAfterLock = getL3CpAndCheck(sessionWrapper, request.getSliceId(), request.getCpId());

      return sendAsyncRequestAndCreateResponse(sessionWrapper, true, request.getSliceId(), makeSendRequestDataList(
          l3CpAfterLock.getSwCluster().getSwClusterId(), request.getSliceId(), request.getCpId()), RequestType.REQUEST);
    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      sessionWrapper.rollback();
      throw exp;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  protected List<RestRequestData> makeSendRequestDataList(int clusterId, String sliceId, String cpId) {
    try {
      logger.methodStart();

      RestRequestData requestData = new RestRequestData();
      requestData.setClusterId(clusterId);
      requestData.setExpectHttpStatusCode(HttpStatus.ACCEPTED_202);
      requestData.setHttpMethod(HttpMethod.PATCH);
      requestData.setIpAddress(
          MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlAddress());
      requestData.setPort(
          MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlPort());
      requestData.setTargetUri(
          MfcFcRequestUri.STATIC_ROUTE_CREATE_DELETE.getUri(SliceType.L3_SLICE.getMessage(), sliceId, cpId));
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
