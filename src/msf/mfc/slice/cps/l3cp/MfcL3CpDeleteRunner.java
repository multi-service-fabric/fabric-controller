
package msf.mfc.slice.cps.l3cp;

import java.util.ArrayList;
import java.util.List;

import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.MfcDbManager;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;

/**
 * Class to implement the asynchronous processing in L3CP deletion.
 *
 * @author NTT
 *
 */
public class MfcL3CpDeleteRunner extends MfcAbstractL3CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3CpDeleteRunner.class);

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for L3CP control
   */
  public MfcL3CpDeleteRunner(L3CpRequest request) {
    this.request = request;
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
      logger.performance("start get l3slice  resources lock.");

      MfcDbManager.getInstance().getResourceLock(new ArrayList<>(), l3SliceList, sessionWrapper);
      logger.performance("end get l3slice resources lock.");

      MfcL3Cp l3CpAfterLock = getL3CpAndCheck(sessionWrapper, request.getSliceId(), request.getCpId());

      processDeleteL3Cp(l3CpAfterLock, false);

      return sendAsyncRequestAndCreateResponse(sessionWrapper, false, request.getSliceId(),
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
