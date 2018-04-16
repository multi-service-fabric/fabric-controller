
package msf.mfc.slice.slices.l3slice;

import java.util.ArrayList;
import java.util.List;

import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.MfcDbManager;
import msf.mfc.db.dao.slices.MfcL3SliceDao;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceRequest;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceUpdateRequestBody;

/**
 * Implementation class for the asynchronous process in the L3 slice
 * modification process.
 *
 * @author NTT
 *
 */
public class MfcL3SliceUpdateRunner extends MfcAbstractL3SliceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3SliceUpdateRunner.class);

  private L3SliceUpdateRequestBody requestBody;

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for L3 slice control
   * @param requestBody
   *          Request Body part for L3 slice modification
   */
  public MfcL3SliceUpdateRunner(L3SliceRequest request, L3SliceUpdateRequestBody requestBody) {
    this.request = request;
    this.requestBody = requestBody;
    setTimerTask(MfcScenarioUtil.createTimerTaskForMfc());
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      try {
        sessionWrapper.openSession();
        MfcL3SliceDao l3SliceDao = new MfcL3SliceDao();
        MfcL3Slice l3Slice = l3SliceDao.read(sessionWrapper, request.getSliceId());

        checkL3SlicePresence(l3Slice, request.getSliceId());
        List<MfcL3Slice> l3SliceList = new ArrayList<>();
        l3SliceList.add(l3Slice);

        sessionWrapper.beginTransaction();
        logger.performance("start get l3slice resources lock.");

        MfcDbManager.getInstance().getL3SlicesLock(l3SliceList, sessionWrapper);
        logger.performance("end get l3slice resources lock.");

        MfcL3Slice l3SliceAfterLock = l3SliceDao.read(sessionWrapper, request.getSliceId());

        checkL3SlicePresence(l3SliceAfterLock, request.getSliceId());

        List<Integer> clusterIdList = getAllSwClusterIdListFromDb(sessionWrapper);
        return sendSliceUpdateRequest(sessionWrapper, SliceType.L3_SLICE, JsonUtil.toJson(requestBody),
            request.getSliceId(), clusterIdList, null, RequestType.REQUEST);
      } catch (MsfException exp) {
        logger.error(exp.getMessage(), exp);
        sessionWrapper.rollback();
        throw exp;
      } finally {
        sessionWrapper.closeSession();
      }
    } finally {
      logger.methodEnd();
    }
  }

}
