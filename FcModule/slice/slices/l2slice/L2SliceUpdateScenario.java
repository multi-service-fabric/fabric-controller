package msf.fc.slice.slices.l2slice;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.ReserveStatus;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.L2Cp;
import msf.fc.common.data.L2Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.slices.L2SliceDao;
import msf.fc.rest.common.JsonUtil;
import msf.fc.slice.slices.l2slice.data.L2SliceRequest;
import msf.fc.slice.slices.l2slice.data.L2SliceUpdateRequestBody;
import msf.fc.slice.slices.l2slice.data.L2SliceUpdateResponseBody;

public class L2SliceUpdateScenario extends AbstractL2SliceScenarioBase<L2SliceRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(L2SliceUpdateScenario.class);
  private L2SliceUpdateRequestBody requestBody;

  public L2SliceUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
  protected void checkParameter(L2SliceRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });


      L2SliceUpdateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          L2SliceUpdateRequestBody.class);
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
      L2SliceDao l2SliceDao = new L2SliceDao();
      L2Slice l2Slice = l2SliceDao.read(sessionWrapper, request.getSliceId());
      checkL2SlicePresence(l2Slice, request.getSliceId(), true);
      sessionWrapper.beginTransaction();
      List<L2Slice> l2SliceList = new ArrayList<>();
      l2SliceList.add(l2Slice);
      logger.performance("start get l2slice resources lock.");
      DbManager.getInstance().getL2SlicesLock(l2SliceList, sessionWrapper);
      logger.performance("end get l2slice resources lock.");
      L2Slice l2SliceAfterLock = l2SliceDao.read(sessionWrapper, request.getSliceId());
      checkL2SlicePresence(l2SliceAfterLock, request.getSliceId(), true);
      checkL2SliceStatus(l2SliceAfterLock);
      updateSliceStatus(l2SliceAfterLock);
      l2SliceDao.update(sessionWrapper, l2SliceAfterLock);
      sessionWrapper.commit();
      return createResponse(l2SliceAfterLock, HttpStatus.OK_200);
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
      L2SliceUpdateRunner runner = new L2SliceUpdateRunner(request, requestBody);
      execAsyncRunner(runner);

      return createResponse(null, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }

  private void checkL2SliceStatus(L2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice" }, new Object[] { l2Slice });
      boolean isActiveStatusOk = false;
      boolean isReserveStatusOk = false;
      boolean isL2CpStatusOk = false;

      switch (requestBody.getActionEnum()) {
        case ACTIVATE_RESERVE:
          isActiveStatusOk = ActiveStatus.INACTIVE.equals(l2Slice.getStatusEnum());
          isReserveStatusOk = ReserveStatus.NONE.equals(l2Slice.getReservationStatusEnum());
          isL2CpStatusOk = true;
          break;
        case ACTIVATE:
          isActiveStatusOk = ActiveStatus.INACTIVE.equals(l2Slice.getStatusEnum());
          isReserveStatusOk = ReserveStatus.ACTIVATE_RESERVE.equals(l2Slice.getReservationStatusEnum());
          isL2CpStatusOk = true;
          break;
        case RESERVE_CANCEL:
          isActiveStatusOk = true;
          isReserveStatusOk = !ReserveStatus.NONE.equals(l2Slice.getReservationStatusEnum());
          isL2CpStatusOk = true;
          break;
        case DEACTIVATE_RESERVE:
          isActiveStatusOk = ActiveStatus.ACTIVE.equals(l2Slice.getStatusEnum());
          isReserveStatusOk = ReserveStatus.NONE.equals(l2Slice.getReservationStatusEnum());
          isL2CpStatusOk = !checkL2CpStatus(l2Slice);
          break;
        case DEACTIVATE:
          isActiveStatusOk = ActiveStatus.ACTIVE.equals(l2Slice.getStatusEnum());
          isReserveStatusOk = ReserveStatus.DEACTIVATE_RESERVE.equals(l2Slice.getReservationStatusEnum());
          isL2CpStatusOk = true;
          break;
        default:
          String logMsg = MessageFormat.format("action = {0}", requestBody.getAction());
          logger.error(logMsg);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, logMsg);
      }
      if (!isActiveStatusOk || !isReserveStatusOk) {
        String logMsg = MessageFormat.format("slice status invalid. activeStatus = {0}, reserveStatus = {1}",
            l2Slice.getStatusEnum().name(), l2Slice.getReservationStatusEnum().name());
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }
      if (!isL2CpStatusOk) {
        String logMsg = "remains invalid status cp related to slice.";
        logger.error(logMsg);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, logMsg);
      }

    } finally {
      logger.methodEnd();
    }
  }

  private void updateSliceStatus(L2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice" }, new Object[] { l2Slice });
      switch (requestBody.getActionEnum()) {
        case ACTIVATE_RESERVE:
          l2Slice.setReservationStatusEnum(ReserveStatus.ACTIVATE_RESERVE);
          break;
        case ACTIVATE:
          l2Slice.setStatusEnum(ActiveStatus.ACTIVE);
          l2Slice.setReservationStatusEnum(ReserveStatus.NONE);
          break;
        case RESERVE_CANCEL:
          l2Slice.setReservationStatusEnum(ReserveStatus.NONE);
          break;
        case DEACTIVATE_RESERVE:
          l2Slice.setReservationStatusEnum(ReserveStatus.DEACTIVATE_RESERVE);
          break;
        case DEACTIVATE:
          l2Slice.setStatusEnum(ActiveStatus.INACTIVE);
          l2Slice.setReservationStatusEnum(ReserveStatus.NONE);
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

  private boolean checkL2CpStatus(L2Slice l2Slice) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2Slice" }, new Object[] { l2Slice });
      for (L2Cp l2Cp : l2Slice.getL2Cps()) {
        if (ActiveStatus.ACTIVE.equals(l2Cp.getStatusEnum())) {
          logger.debug("active status cp found. sliceId = {0},cpId = {1}.", l2Slice.getSliceId(),
              l2Cp.getId().getCpId());
          return true;
        } else if (!ReserveStatus.NONE.equals(l2Cp.getReservationStatusEnum())) {
          logger.debug("reserved status cp found. sliceId = {0},cpId = {1}.", l2Slice.getSliceId(),
              l2Cp.getId().getCpId());
          return true;
        }
      }
      return false;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(L2Slice l2Slice, int httpStatusCode) {
    try {
      logger.methodStart(new String[] { "l2Slice", "httpStatusCode" }, new Object[] { l2Slice, httpStatusCode });
      L2SliceUpdateResponseBody responseBody = new L2SliceUpdateResponseBody();
      if (l2Slice != null) {
        responseBody.setReservationStatusEnum(l2Slice.getReservationStatusEnum());
        responseBody.setStatusEnum(l2Slice.getStatusEnum());
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
