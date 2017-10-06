package msf.fc.core.scenario;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.concurrent.Callable;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.AsyncProcessStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationExecutionStatus;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.AsyncRequest;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.operation.OperationManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.common.AsyncRequestsDao;

public abstract class AbstractAsyncRunner implements Callable<Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractAsyncRunner.class);

  private SynchronousType syncType = null;

  private OperationType operationType = null;

  private SystemInterfaceType systemIfType = null;
  private String operationId = null;

  @Override
  public Integer call() {

    logger.methodStart();

    boolean isScenarioFailed = false;

    try {
      this.initializeAsyncRunner();
      isScenarioFailed = false;
    } catch (Exception ex) {
      logger.error("[ope_id={0}]:Initializae async runner failed.", ex, operationId);
      isScenarioFailed = true;
    }

    AsyncProcessStatus iniStatus = null;

    if (!isScenarioFailed) {
    } else {
    }

    logger.debug("[ope_id={0}]:AsyncProcessStatus={1}", operationId, iniStatus);

    AsyncRequest updateRecord = new AsyncRequest();

    try {
      updateRecord = this.updateAsyncRequest(iniStatus);
    } catch (Exception ex) {
      logger.error("[ope_id={0}]:Update async_requests table failed.", ex, operationId);
      isScenarioFailed = true;
    }

    if (isScenarioFailed) {
      this.finalizeAsyncRunner();

      logger.info("End async runner.");
      logger.methodEnd();
      return updateRecord.getStatus();
    }


    updateRecord = new AsyncRequest();

    AsyncProcessStatus exeStatus = null;

    RestResponseBase res = null;

    try {
      logger.info("[ope_id={0}]:Start executeImpl.(Async)", operationId);
      res = this.executeImpl();
      logger.info("[ope_id={0}]:End executeImpl.(Async)", operationId);

      if (HttpStatus.OK_200 <= res.getHttpStatusCode() && res.getHttpStatusCode() < HttpStatus.MULTIPLE_CHOICES_300) {
        logger.debug("[ope_id={0}]:Async scenario execute success.(HTTP Status Code={1})", operationId,
            res.getHttpStatusCode());
      } else {
        logger.warn("[ope_id={0}]:Async scenario execute failed.(HTTP Status Code={1})", operationId,
            res.getHttpStatusCode());
      }
    } catch (MsfException ex) {
      logger.error("[ope_id={0}]:Async scenario execute failed.", ex, operationId);

      res = new ErrorResponseData(ex.getErrorCode(), systemIfType);
    } catch (Exception ex) {
      logger.error("[ope_id={0}]:Async scenario execute failed.", ex, operationId);

      res = new ErrorResponseData(ErrorCode.UNDEFINED_ERROR, systemIfType);
    }

    try {
      updateRecord = this.updateAsyncRequest(exeStatus, res);

    } catch (Exception ex) {
      logger.error("[ope_id={0}]:Update async_requests table failed.", ex, operationId);
      logger.info("End async runner.");
      logger.methodEnd();

      return updateRecord.getStatus();

    } finally {
      this.finalizeAsyncRunner();
    }

    logger.info("End async runner.");
    logger.methodEnd();
    return updateRecord.getStatus();
  }

  protected void setScenarioType(SynchronousType syncType, OperationType operationType,
      SystemInterfaceType systemIfType, String operationId) {
    if (logger.isDebugEnabled()) {
      logger.methodStart(new String[] { "syncType", "operationType", "systemIfType", "operationId" },
          new Object[] { syncType, operationType, systemIfType, operationId });
    }

    this.syncType = syncType;
    this.operationType = operationType;
    this.systemIfType = systemIfType;
    this.operationId = operationId;

    logger.methodEnd();
  }

  private void initializeAsyncRunner() throws MsfException {
    logger.methodStart();

    if (this.syncType == null || this.operationType == null || this.operationId == null || this.systemIfType == null) {
      String errorMessage = "REST-IF setting is null. "
          + "(syncType={0}, operationType={1}, systemIfType={2}, operationId={3})";
      errorMessage = MessageFormat.format(errorMessage, syncType, operationType, systemIfType, operationId);

      logger.debug(errorMessage);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, errorMessage);
    }

    OperationExecutionStatus oeStatus = OperationManager.getInstance().getOperationExecutionStatus(operationType);

    if (oeStatus == OperationExecutionStatus.NOT_ALLOWED) {
      throw new MsfException(ErrorCode.SYSTEM_STATUS_ERROR, "System can not accept a request.");
    }

    logger.methodEnd();
  }

  private void finalizeAsyncRunner() {
    logger.methodStart();

    OperationManager.getInstance().releaseOperationId(operationId);
    logger.info("[ope_id={0}]:Release operationId={0} .", operationId);

    logger.methodEnd();
  }

  protected abstract RestResponseBase executeImpl() throws MsfException;

  private AsyncRequest updateAsyncRequest(AsyncProcessStatus status, RestResponseBase restResBase) throws MsfException {
    if (logger.isDebugEnabled()) {
      logger.methodStart(new String[] { "status", "restResBase" }, new Object[] { status, restResBase });
    }

    AsyncRequest ret = new AsyncRequest();
    SessionWrapper sessionWrapper = new SessionWrapper();
    AsyncRequestsDao arDao = new AsyncRequestsDao();

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    try {
      sessionWrapper.openSession();
      sessionWrapper.beginTransaction();

      ret = arDao.read(sessionWrapper, operationId);
      if (ret == null) {
        String errorMessage = "Record not found in async_requests table.(operation_id={0})";
        errorMessage = MessageFormat.format(errorMessage, operationId);

        logger.debug(errorMessage);
        throw new MsfException(ErrorCode.UNDEFINED_ERROR, errorMessage);
      }

      ret.setStatusEnum(status);
      ret.setLastUpdateTime(timestamp);

      if (restResBase != null) {
        ret.setResponseStatusCode(restResBase.getHttpStatusCode());
        ret.setResponseBody(restResBase.getResponseBody());
      }

      arDao.update(sessionWrapper, ret);

      sessionWrapper.commit();
    } catch (MsfException ex) {
      sessionWrapper.rollback();
      logger.debug("DB control failed.", ex);
      throw ex;
    } finally {
      sessionWrapper.closeSession();
    }

    logger.methodEnd(new String[] { "result" }, new Object[] { ret });
    return ret;
  }

  private AsyncRequest updateAsyncRequest(AsyncProcessStatus status) throws MsfException {
    return updateAsyncRequest(status, null);
  }

}