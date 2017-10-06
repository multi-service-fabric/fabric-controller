package msf.fc.slice.slices.l3slice;

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
import msf.fc.common.data.L3Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.L3SliceDao;
import msf.fc.rest.common.JsonUtil;
import msf.fc.slice.slices.l3slice.data.L3SliceRequest;
import msf.fc.slice.slices.l3slice.data.L3SliceUpdateRequestBody;
import msf.fc.slice.slices.l3slice.data.L3SliceUpdateResponseBody;

public class L3SliceUpdateScenario extends AbstractL3SliceScenarioBase<L3SliceRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(L3SliceUpdateScenario.class);
  private L3SliceUpdateRequestBody requestBody;

  public L3SliceUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
  protected void checkParameter(L3SliceRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });


      L3SliceUpdateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          L3SliceUpdateRequestBody.class);
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
      checkL3SlicePresence(l3Slice, request.getSliceId(), true);
      sessionWrapper.beginTransaction();
      List<L3Slice> l3SliceList = new ArrayList<>();
      l3SliceList.add(l3Slice);
      logger.performance("start get l3slice resources lock.");
      DbManager.getInstance().getL3SlicesLock(l3SliceList, sessionWrapper);
      logger.performance("end get l3slice resources lock.");
      L3Slice l3SliceAfterLock = l3SliceDao.read(sessionWrapper, request.getSliceId());
      checkL3SlicePresence(l3SliceAfterLock, request.getSliceId(), true);
      checkL3SliceStatus(l3SliceAfterLock);
      updateSliceStatus(l3SliceAfterLock);
      l3SliceDao.update(sessionWrapper, l3SliceAfterLock);
      sessionWrapper.commit();
      return createResponse(l3SliceAfterLock, HttpStatus.OK_200);
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
      L3SliceUpdateRunner runner = new L3SliceUpdateRunner(request, requestBody);
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
      boolean isL3CpStatusOk = false;

      switch (requestBody.getActionEnum()) {
        case ACTIVATE_RESERVE:
          isActiveStatusOk = ActiveStatus.INACTIVE.equals(l3Slice.getStatusEnum());
          isReserveStatusOk = ReserveStatus.NONE.equals(l3Slice.getReservationStatusEnum());
          isL3CpStatusOk = true;
          break;
        case ACTIVATE:
          isActiveStatusOk = ActiveStatus.INACTIVE.equals(l3Slice.getStatusEnum());
          isReserveStatusOk = ReserveStatus.ACTIVATE_RESERVE.equals(l3Slice.getReservationStatusEnum());
          isL3CpStatusOk = true;
          break;
        case RESERVE_CANCEL:
          isActiveStatusOk = true;
          isReserveStatusOk = !ReserveStatus.NONE.equals(l3Slice.getReservationStatusEnum());
          isL3CpStatusOk = true;
          break;
        case DEACTIVATE_RESERVE:
          isActiveStatusOk = ActiveStatus.ACTIVE.equals(l3Slice.getStatusEnum());
          isReserveStatusOk = ReserveStatus.NONE.equals(l3Slice.getReservationStatusEnum());
          isL3CpStatusOk = !checkL3CpStatus(l3Slice);
          break;
        case DEACTIVATE:
          isActiveStatusOk = ActiveStatus.ACTIVE.equals(l3Slice.getStatusEnum());
          isReserveStatusOk = ReserveStatus.DEACTIVATE_RESERVE.equals(l3Slice.getReservationStatusEnum());
          isL3CpStatusOk = true;
          break;
        default:
          String logMsg = MessageFormat.format("action = {0}", requestBody.getAction());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }
      if (!isActiveStatusOk || !isReserveStatusOk) {
        String logMsg = MessageFormat.format("slice status invalid. activeStatus = {0}, reserveStatus = {1}",
            l3Slice.getStatusEnum().name(), l3Slice.getReservationStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }
      if (!isL3CpStatusOk) {
        String logMsg = "remains invalid status cp related to slice.";
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private void updateSliceStatus(L3Slice l3Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });
      switch (requestBody.getActionEnum()) {
        case ACTIVATE_RESERVE:
          l3Slice.setReservationStatusEnum(ReserveStatus.ACTIVATE_RESERVE);
          break;
        case ACTIVATE:
          l3Slice.setStatusEnum(ActiveStatus.ACTIVE);
          l3Slice.setReservationStatusEnum(ReserveStatus.NONE);
          break;
        case RESERVE_CANCEL:
          l3Slice.setReservationStatusEnum(ReserveStatus.NONE);
          break;
        case DEACTIVATE_RESERVE:
          l3Slice.setReservationStatusEnum(ReserveStatus.DEACTIVATE_RESERVE);
          break;
        case DEACTIVATE:
          l3Slice.setStatusEnum(ActiveStatus.INACTIVE);
          l3Slice.setReservationStatusEnum(ReserveStatus.NONE);
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

  private boolean checkL3CpStatus(L3Slice l3Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3Slice" }, new Object[] { l3Slice });
      for (L3Cp l3Cp : l3Slice.getL3Cps()) {
        if (ActiveStatus.ACTIVE.equals(l3Cp.getStatusEnum())) {
          logger.debug("active status cp found. sliceId = {0},cpId = {1}.", l3Slice.getSliceId(),
              l3Cp.getId().getCpId());
          return true;
        } else if (!ReserveStatus.NONE.equals(l3Cp.getReservationStatusEnum())) {
          logger.debug("reserved status cp found. sliceId = {0},cpId = {1}.", l3Slice.getSliceId(),
              l3Cp.getId().getCpId());
          return true;
        }
      }
      return false;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(L3Slice l3Slice, int httpStatusCode) {
    try {
      logger.methodStart(new String[] { "l3Slice", "httpStatusCode" }, new Object[] { l3Slice, httpStatusCode });
      L3SliceUpdateResponseBody responseBody = new L3SliceUpdateResponseBody();
      if (l3Slice != null) {
        responseBody.setReservationStatusEnum(l3Slice.getReservationStatusEnum());
        responseBody.setStatusEnum(l3Slice.getStatusEnum());
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
