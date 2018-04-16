
package msf.mfc.slice.cps.l2cp;

import java.util.ArrayList;
import java.util.List;

import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.MfcDbManager;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;

/**
 * Class to implement the asynchronous processing in L2CP deletion.
 *
 * @author NTT
 *
 */
public class MfcL2CpDeleteRunner extends MfcAbstractL2CpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2CpDeleteRunner.class);

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for L2CP control
   */
  public MfcL2CpDeleteRunner(L2CpRequest request) {
    this.request = request;
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

      MfcDbManager.getInstance().getResourceLock(l2SliceList, new ArrayList<>(), sessionWrapper);
      logger.performance("end get l2slice resources lock.");

      MfcL2Cp l2CpAfterLock = getL2CpAndCheck(sessionWrapper, request.getSliceId(), request.getCpId());

      processDeleteL2Cp(sessionWrapper, l2CpAfterLock);

      OperationManager.getInstance().setOperationData(getOperationId(), rollbackData);

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
