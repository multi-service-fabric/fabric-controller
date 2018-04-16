
package msf.mfc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.List;

import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.MfcDbManager;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.cps.l3cp.data.L3CpCreateRequestBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;

/**
 * Implementation class for the asynchronous process in the L3CP
 * addition/deletion(/modification) process.
 *
 * @author NTT
 *
 */
public class MfcL3CpCreateRunner extends MfcAbstractL3CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3CpCreateRunner.class);

  private L3CpCreateRequestBody requestBody;

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for L3CP control
   * @param requestBody
   *          Request Body part for L3CP addition/deletion(/modification)
   */
  public MfcL3CpCreateRunner(L3CpRequest request, L3CpCreateRequestBody requestBody) {
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

      sessionWrapper.beginTransaction();
      logger.performance("start get l3slice resources lock.");

      MfcDbManager.getInstance().getResourceLock(new ArrayList<>(), l3SliceList, sessionWrapper);
      logger.performance("end get l3slice resources lock.");

      MfcL3Slice l3SliceAfterLock = getL3SliceAndCheck(sessionWrapper, request.getSliceId());

      String cpId = getCpIdAndCheck(sessionWrapper, request.getSliceId(), requestBody.getCpId(), null);

      processCreateL3Cp(sessionWrapper, l3SliceAfterLock, cpId, requestBody.getClusterId(),
          requestBody.getEdgePointId(), requestBody.getVlanId(), requestBody.getMtu(), requestBody.getIpv4Address(),
          requestBody.getIpv6Address(), requestBody.getIpv4Prefix(), requestBody.getIpv6Prefix(), requestBody.getBgp(),
          requestBody.getStaticRouteList(), requestBody.getVrrp(), requestBody.getTrafficThreshold(),
          requestBody.getQos());

      return sendAsyncRequestAndCreateResponse(sessionWrapper, true, request.getSliceId(),
          makeSendRequestDataList(request.getSliceId()), RequestType.REQUEST);
    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      sessionWrapper.rollback();
      throw exp;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

}
