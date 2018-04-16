
package msf.mfc.slice.slices.l2slice;

import java.util.ArrayList;
import java.util.List;

import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.common.util.MfcScenarioUtil;
import msf.mfc.db.MfcDbManager;
import msf.mfc.db.dao.slices.MfcL2SliceDao;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceRequest;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceUpdateRequestBody;

/**
 * Implementation class for the asynchronous process in the L2 slice
 * modification process.
 *
 * @author NTT
 *
 */
public class MfcL2SliceUpdateRunner extends MfcAbstractL2SliceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2SliceUpdateRunner.class);

  private L2SliceUpdateRequestBody requestBody;

  /**
   * Constructor. <br>
   * Take over the necessary information from scenario
   *
   * @param request
   *          Request for L2 slice control
   * @param requestBody
   *          Request Body part for L2 slice modification
   */
  public MfcL2SliceUpdateRunner(L2SliceRequest request, L2SliceUpdateRequestBody requestBody) {
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
        MfcL2SliceDao l2SliceDao = new MfcL2SliceDao();
        MfcL2Slice l2Slice = l2SliceDao.read(sessionWrapper, request.getSliceId());

        checkL2SlicePresence(l2Slice, request.getSliceId());
        List<MfcL2Slice> l2SliceList = new ArrayList<>();
        l2SliceList.add(l2Slice);

        sessionWrapper.beginTransaction();
        logger.performance("start get l2slice resources lock.");

        MfcDbManager.getInstance().getL2SlicesLock(l2SliceList, sessionWrapper);
        logger.performance("end get l2slice resources lock.");

        MfcL2Slice l2SliceAfterLock = l2SliceDao.read(sessionWrapper, request.getSliceId());

        checkL2SlicePresence(l2SliceAfterLock, request.getSliceId());

        List<Integer> clusterIdList = getAllSwClusterIdListFromDb(sessionWrapper);
        return sendSliceUpdateRequest(sessionWrapper, SliceType.L2_SLICE, JsonUtil.toJson(requestBody),
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
