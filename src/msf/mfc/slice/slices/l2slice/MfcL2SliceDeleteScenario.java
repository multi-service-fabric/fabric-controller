
package msf.mfc.slice.slices.l2slice;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.db.MfcDbManager;
import msf.mfc.db.dao.slices.MfcL2SliceDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceCreateRequestBody;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceRequest;

/**
 * Implementation class for L2 slice deletion.
 *
 * @author NTT
 *
 */
public class MfcL2SliceDeleteScenario extends MfcAbstractL2SliceScenarioBase<L2SliceRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2SliceDeleteScenario.class);

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public MfcL2SliceDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();
      MfcL2SliceDao l2SliceDao = new MfcL2SliceDao();
      MfcL2Slice l2Slice = l2SliceDao.read(sessionWrapper, request.getSliceId());

      checkL2SlicePresence(l2Slice, request.getSliceId(), true);

      sessionWrapper.beginTransaction();
      List<MfcL2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);
      logger.performance("start get l2slice resources lock.");

      MfcDbManager.getInstance().getL2SlicesLock(l2SliceList, sessionWrapper);
      logger.performance("end get l2slice resources lock.");

      MfcL2Slice l2SliceAfterLock = l2SliceDao.read(sessionWrapper, request.getSliceId());

      checkL2SlicePresence(l2SliceAfterLock, request.getSliceId(), true);

      checkL2SliceStatus(l2SliceAfterLock);

      l2SliceDao.delete(sessionWrapper, l2Slice.getSliceId());

      return sendSliceCreateDeleteRequest(sessionWrapper, SliceType.L2_SLICE, false,
          makeRequestBodyFromDb(l2SliceAfterLock), l2SliceAfterLock.getSliceId());

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
  protected void checkParameter(L2SliceRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkL2SliceStatus(MfcL2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice" }, new Object[] { l2Slice });

      if (l2Slice.getL2Cps() != null && l2Slice.getL2Cps().size() != 0) {
        String logMsg = MessageFormat.format("remains cp related to slice.remain cp num = {0}",
            l2Slice.getL2Cps().size());
        logger.error(logMsg);
        if (logger.isDebugEnabled()) {
          int ite = 0;
          for (MfcL2Cp l2Cp : l2Slice.getL2Cps()) {
            logger.trace("related cpId[{0}] = {1}", ite++, l2Cp.getId().getCpId());
          }
        }
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private String makeRequestBodyFromDb(MfcL2Slice l2Slice) {
    try {
      logger.methodStart();
      L2SliceCreateRequestBody requestBody = new L2SliceCreateRequestBody();
      requestBody.setSliceId(l2Slice.getSliceId());
      requestBody.setVrfId(l2Slice.getVrfId());
      return JsonUtil.toJson(requestBody);
    } finally {
      logger.methodEnd();
    }
  }
}
