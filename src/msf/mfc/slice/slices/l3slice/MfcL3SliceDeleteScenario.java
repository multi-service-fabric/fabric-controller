
package msf.mfc.slice.slices.l3slice;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.db.MfcDbManager;
import msf.mfc.db.dao.slices.MfcL3SliceDao;
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
import msf.mfcfc.slice.slices.l3slice.data.L3SliceCreateRequestBody;
import msf.mfcfc.slice.slices.l3slice.data.L3SliceRequest;

/**
 * Implementation class for L3 slice deletion.
 *
 * @author NTT
 *
 */
public class MfcL3SliceDeleteScenario extends MfcAbstractL3SliceScenarioBase<L3SliceRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3SliceDeleteScenario.class);

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
  public MfcL3SliceDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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
      MfcL3SliceDao l3SliceDao = new MfcL3SliceDao();
      MfcL3Slice l3Slice = l3SliceDao.read(sessionWrapper, request.getSliceId());

      checkL3SlicePresence(l3Slice, request.getSliceId(), true);

      sessionWrapper.beginTransaction();
      List<MfcL3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);
      logger.performance("start get l3slice resources lock.");

      MfcDbManager.getInstance().getL3SlicesLock(l3SliceList, sessionWrapper);
      logger.performance("end get l3slice resources lock.");

      MfcL3Slice l3SliceAfterLock = l3SliceDao.read(sessionWrapper, request.getSliceId());

      checkL3SlicePresence(l3SliceAfterLock, request.getSliceId(), true);

      checkL3SliceStatus(l3SliceAfterLock);

      l3SliceDao.delete(sessionWrapper, l3Slice.getSliceId());

      return sendSliceCreateDeleteRequest(sessionWrapper, SliceType.L3_SLICE, false,
          makeRequestBodyFromDb(l3SliceAfterLock), l3SliceAfterLock.getSliceId());

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
  protected void checkParameter(L3SliceRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkL3SliceStatus(MfcL3Slice l3Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });

      if (l3Slice.getL3Cps() != null && l3Slice.getL3Cps().size() != 0) {
        String logMsg = MessageFormat.format("remains cp related to slice.remain cp num = {0}",
            l3Slice.getL3Cps().size());
        logger.error(logMsg);
        if (logger.isDebugEnabled()) {
          int ite = 0;
          for (MfcL3Cp l3Cp : l3Slice.getL3Cps()) {
            logger.trace("related cpId[{0}] = {1}", ite++, l3Cp.getId().getCpId());
          }
        }
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private String makeRequestBodyFromDb(MfcL3Slice l3Slice) {
    try {
      logger.methodStart();
      L3SliceCreateRequestBody requestBody = new L3SliceCreateRequestBody();
      requestBody.setSliceId(l3Slice.getSliceId());
      requestBody.setVrfId(l3Slice.getVrfId());
      requestBody.setPlane(l3Slice.getPlane());
      return JsonUtil.toJson(requestBody);
    } finally {
      logger.methodEnd();
    }
  }
}
