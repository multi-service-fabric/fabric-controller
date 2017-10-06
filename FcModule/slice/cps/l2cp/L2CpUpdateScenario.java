package msf.fc.slice.cps.l2cp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.ReserveStatus;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.L2Cp;
import msf.fc.common.data.L2CpPK;
import msf.fc.common.data.L2Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.L2CpDao;
import msf.fc.db.dao.slices.L2SliceDao;
import msf.fc.rest.common.JsonUtil;
import msf.fc.slice.cps.l2cp.data.L2CpRequest;
import msf.fc.slice.cps.l2cp.data.L2CpUpdateRequestBody;
import msf.fc.slice.cps.l2cp.data.L2CpUpdateResponseBody;

public class L2CpUpdateScenario extends AbstractL2CpScenarioBase<L2CpRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L2CpUpdateScenario.class);
  private L2CpUpdateRequestBody requestBody;

  public L2CpUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
      L2SliceDao l2SliceDao = new L2SliceDao();
      L2Slice l2Slice = l2SliceDao.read(sessionWrapper, request.getSliceId());
      checkL2SlicePresence(l2Slice, request.getSliceId(), false);
      sessionWrapper.beginTransaction();
      List<L2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);
      logger.performance("start get l2slice resources lock.");
      DbManager.getInstance().getL2SlicesLock(l2SliceList, sessionWrapper);
      logger.performance("end get l2slice resources lock.");
      L2Slice l2SliceAfterLock = l2SliceDao.read(sessionWrapper, request.getSliceId());
      checkL2SlicePresence(l2SliceAfterLock, request.getSliceId(), false);
      checkL2SliceStatus(l2SliceAfterLock);

      L2CpDao l2CpDao = new L2CpDao();
      L2CpPK l2CpPk = new L2CpPK();
      l2CpPk.setCpId(request.getCpId());
      l2CpPk.setSliceId(request.getSliceId());
      L2Cp l2Cp = l2CpDao.read(sessionWrapper, l2CpPk);
      checkL2CpPresence(l2Cp);
      checkL2CpStatus(l2Cp);
      updateCpStatus(l2Cp);
      l2CpDao.update(sessionWrapper, l2Cp);
      sessionWrapper.commit();
      return createResponse(l2Cp);
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
  protected void checkParameter(L2CpRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });


      L2CpUpdateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(), L2CpUpdateRequestBody.class);
      requestBody.validate();
      logger.debug("requestBody=" + request.getRequestBody());

      setRestIfTypeByAction(requestBody.getActionEnum());

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

  private void checkL2SliceStatus(L2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice" }, new Object[] { l2Slice });
      boolean isActiveStatusOk = false;
      boolean isReserveStatusOk = false;

      switch (requestBody.getActionEnum()) {
          isActiveStatusOk = ActiveStatus.ACTIVE.equals(l2Slice.getStatusEnum());
          isReserveStatusOk = ReserveStatus.NONE.equals(l2Slice.getReservationStatusEnum());
          break;
        case RESERVE_CANCEL:
          isActiveStatusOk = true;
          isReserveStatusOk = true;
          break;
        default:
          String logMsg = MessageFormat.format("action is undefined.action = {0}", requestBody.getAction());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }
      if (!isActiveStatusOk || !isReserveStatusOk) {
        String logMsg = MessageFormat.format("slice status invalid. activeStatus = {0}, reserveStatus = {1}",
            l2Slice.getStatusEnum().name(), l2Slice.getReservationStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkL2CpStatus(L2Cp l2Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp" }, new Object[] { l2Cp });
      boolean isActiveStatusOk = false;
      boolean isReserveStatusOk = false;
      switch (requestBody.getActionEnum()) {
        case ACTIVATE_RESERVE:
          isActiveStatusOk = ActiveStatus.INACTIVE.equals(l2Cp.getStatusEnum());
          isReserveStatusOk = ReserveStatus.NONE.equals(l2Cp.getReservationStatusEnum());
          break;
        case DEACTIVATE_RESERVE:
          isActiveStatusOk = ActiveStatus.ACTIVE.equals(l2Cp.getStatusEnum());
          isReserveStatusOk = ReserveStatus.NONE.equals(l2Cp.getReservationStatusEnum());
          break;
        case RESERVE_CANCEL:
          isActiveStatusOk = true;
          isReserveStatusOk = !ReserveStatus.NONE.equals(l2Cp.getReservationStatusEnum());
          break;
        default:
          String logMsg = MessageFormat.format("action is undefined.action = {0}", requestBody.getAction());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }

      if (!isActiveStatusOk || !isReserveStatusOk) {
        String logMsg = MessageFormat.format("cp status invalid. activeStatus = {0}, reserveStatus = {1}",
            l2Cp.getStatusEnum().name(), l2Cp.getReservationStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void updateCpStatus(L2Cp l2Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Cp" }, new Object[] { l2Cp });
      switch (requestBody.getActionEnum()) {
        case ACTIVATE_RESERVE:
          l2Cp.setReservationStatusEnum(ReserveStatus.ACTIVATE_RESERVE);
          break;
        case DEACTIVATE_RESERVE:
          l2Cp.setReservationStatusEnum(ReserveStatus.DEACTIVATE_RESERVE);
          break;
        case RESERVE_CANCEL:
          l2Cp.setReservationStatusEnum(ReserveStatus.NONE);
          break;
        default:
          String logMsg = MessageFormat.format("action is undefined.action = {0}", requestBody.getAction());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(L2Cp l2Cp) {
    try {
      logger.methodStart(new String[] { "l2Cp" }, new Object[] { l2Cp });
      L2CpUpdateResponseBody responseBody = new L2CpUpdateResponseBody();
      responseBody.setReservationStatusEnum(l2Cp.getReservationStatusEnum());
      responseBody.setStatusEnum(l2Cp.getStatusEnum());
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);
      return restResponse;

    } finally {
      logger.methodEnd();
    }
  }
}
