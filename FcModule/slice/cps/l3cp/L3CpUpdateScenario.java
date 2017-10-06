package msf.fc.slice.cps.l3cp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.ReserveStatus;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.L3Cp;
import msf.fc.common.data.L3CpPK;
import msf.fc.common.data.L3Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.L3CpDao;
import msf.fc.db.dao.slices.L3SliceDao;
import msf.fc.rest.common.JsonUtil;
import msf.fc.slice.cps.l3cp.data.L3CpRequest;
import msf.fc.slice.cps.l3cp.data.L3CpUpdateRequestBody;
import msf.fc.slice.cps.l3cp.data.L3CpUpdateResponseBody;

public class L3CpUpdateScenario extends AbstractL3CpScenarioBase<L3CpRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(L3CpUpdateScenario.class);
  private L3CpUpdateRequestBody requestBody;

  public L3CpUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      switch (getSyncType()) {
        case SYNC:
          return executeImplSync();
        default:
          return executeImplAsync();
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected void checkParameter(L3CpRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });


      L3CpUpdateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(), L3CpUpdateRequestBody.class);
      requestBody.validate();
      logger.debug("requestBody=" + request.getRequestBody());

      setRestIfTypeByAction(requestBody.getActionEnum());

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase executeImplSync() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();
      L3SliceDao l3SliceDao = new L3SliceDao();
      L3Slice l3Slice = l3SliceDao.read(sessionWrapper, request.getSliceId());
      checkL3SlicePresence(l3Slice, request.getSliceId(), false);
      sessionWrapper.beginTransaction();
      List<L3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);
      logger.performance("start get l3slice resources lock.");
      DbManager.getInstance().getL3SlicesLock(l3SliceList, sessionWrapper);
      logger.performance("end get l3slice resources lock.");
      L3Slice l3SliceAfterLock = l3SliceDao.read(sessionWrapper, request.getSliceId());
      checkL3SlicePresence(l3SliceAfterLock, request.getSliceId(), false);
      checkL3SliceStatus(l3SliceAfterLock);

      L3CpDao l3CpDao = new L3CpDao();
      L3CpPK l3CpPk = new L3CpPK();
      l3CpPk.setCpId(request.getCpId());
      l3CpPk.setSliceId(request.getSliceId());
      L3Cp l3Cp = l3CpDao.read(sessionWrapper, l3CpPk);
      checkL3CpPresence(l3Cp);
      checkL3CpStatus(l3Cp);
      updateCpStatus(l3Cp);
      l3CpDao.update(sessionWrapper, l3Cp);
      sessionWrapper.commit();
      return createResponse(l3Cp, HttpStatus.OK_200);
    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      sessionWrapper.rollback();
      throw exp;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  private RestResponseBase executeImplAsync() throws MsfException {
    try {
      logger.methodStart();
      L3CpUpdateRunner runner = new L3CpUpdateRunner(request, requestBody);
      execAsyncRunner(runner);

      return createResponse(null, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }

  private void checkL3SliceStatus(L3Slice l3Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });
      boolean isActiveStatusOk = false;
      boolean isReserveStatusOk = false;

      switch (requestBody.getActionEnum()) {
          isActiveStatusOk = ActiveStatus.ACTIVE.equals(l3Slice.getStatusEnum());
          isReserveStatusOk = ReserveStatus.NONE.equals(l3Slice.getReservationStatusEnum());
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
            l3Slice.getStatusEnum().name(), l3Slice.getReservationStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private void checkL3CpStatus(L3Cp l3Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Cp" }, new Object[] { l3Cp });
      boolean isActiveStatusOk = false;
      boolean isReserveStatusOk = false;
      switch (requestBody.getActionEnum()) {
        case ACTIVATE_RESERVE:
          isActiveStatusOk = ActiveStatus.INACTIVE.equals(l3Cp.getStatusEnum());
          isReserveStatusOk = ReserveStatus.NONE.equals(l3Cp.getReservationStatusEnum());
          break;
        case DEACTIVATE_RESERVE:
          isActiveStatusOk = ActiveStatus.ACTIVE.equals(l3Cp.getStatusEnum());
          isReserveStatusOk = ReserveStatus.NONE.equals(l3Cp.getReservationStatusEnum());
          break;
        case RESERVE_CANCEL:
          isActiveStatusOk = true;
          isReserveStatusOk = !ReserveStatus.NONE.equals(l3Cp.getReservationStatusEnum());
          break;
        default:
          String logMsg = MessageFormat.format("action = {0}", requestBody.getAction());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }

      if (!isActiveStatusOk || !isReserveStatusOk) {
        String logMsg = MessageFormat.format("cp status invalid. activeStatus = {0}, reserveStatus = {1}",
            l3Cp.getStatusEnum().name(), l3Cp.getReservationStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void updateCpStatus(L3Cp l3Cp) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Cp" }, new Object[] { l3Cp });
      switch (requestBody.getActionEnum()) {
        case ACTIVATE_RESERVE:
          l3Cp.setReservationStatusEnum(ReserveStatus.ACTIVATE_RESERVE);
          break;
        case DEACTIVATE_RESERVE:
          l3Cp.setReservationStatusEnum(ReserveStatus.DEACTIVATE_RESERVE);
          break;
        case RESERVE_CANCEL:
          l3Cp.setReservationStatusEnum(ReserveStatus.NONE);
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

  private RestResponseBase createResponse(L3Cp l3Cp, int httpStatusCode) {
    try {
      logger.methodStart(new String[] { "l3Cp", "httpStatusCode" }, new Object[] { l3Cp, httpStatusCode });
      L3CpUpdateResponseBody responseBody = new L3CpUpdateResponseBody();
      if (l3Cp != null) {
        responseBody.setReservationStatusEnum(l3Cp.getReservationStatusEnum());
        responseBody.setStatusEnum(l3Cp.getStatusEnum());

      } else {
        responseBody.setOperationId(getOperationId());
      }
      RestResponseBase restResponse = new RestResponseBase(httpStatusCode, responseBody);
      return restResponse;

    } finally {
      logger.methodEnd();
    }
  }
}
