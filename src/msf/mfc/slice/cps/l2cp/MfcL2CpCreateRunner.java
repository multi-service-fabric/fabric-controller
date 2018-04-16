
package msf.mfc.slice.cps.l2cp;

import java.util.ArrayList;
import java.util.List;

import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.MfcDbManager;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.cps.l2cp.data.L2CpCreateRequestBody;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;

/**
 * Class to implement the asynchronous processing in L2CP addition.
 *
 * @author NTT
 *
 */
public class MfcL2CpCreateRunner extends MfcAbstractL2CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2CpCreateRunner.class);

  private L2CpCreateRequestBody requestBody;

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for L2CP control
   * @param requestBody
   *          Request body for L2CP addition
   */
  public MfcL2CpCreateRunner(L2CpRequest request, L2CpCreateRequestBody requestBody) {
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

      sessionWrapper.beginTransaction();
      logger.performance("start get l2slice resources lock.");

      MfcDbManager.getInstance().getResourceLock(l2SliceList, new ArrayList<>(), sessionWrapper);
      logger.performance("end get l2slice resources lock.");

      MfcL2Slice l2SliceAfterLock = getL2SliceAndCheck(sessionWrapper, request.getSliceId());

      String cpId = getCpIdAndCheck(sessionWrapper, request.getSliceId(), requestBody.getCpId(), null);

      processCreateL2Cp(sessionWrapper, l2SliceAfterLock, cpId, Integer.valueOf(requestBody.getEdgePointId()),
          requestBody.getPortMode(), requestBody.getVlanId(), requestBody.getPairCpId(),
          Integer.valueOf(requestBody.getClusterId()), requestBody.getQos());

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
