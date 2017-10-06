package msf.fc.core.scenario;

import java.sql.Timestamp;
import java.text.MessageFormat;

import msf.fc.common.constant.AsyncProcessStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationExecutionStatus;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.AsyncRequest;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.async.AsyncExecutor;
import msf.fc.core.operation.OperationManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.common.AsyncRequestsDao;

public abstract class AbstractScenario<T extends RestRequestBase> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractScenario.class);

  protected SynchronousType syncType = null;

  protected OperationType operationType = null;

  protected SystemInterfaceType systemIfType = null;

  private String operationId = null;

  private boolean isExecAsyncRunner = false;

  private boolean isRegistAsyncRecord = false;

  protected void setRestIfType(SynchronousType syncType) {
    if (logger.isDebugEnabled()) {
      logger.methodStart(new String[] { "syncType" }, new Object[] { syncType });
    }

    this.syncType = syncType;

    logger.methodEnd();
  }

  private void initializeRestIf() throws MsfException {
    logger.methodStart();

    if (this.syncType == null || this.operationType == null || systemIfType == null) {
      String errorMessage = "REST-IF setting is null. (syncType={0}, operationType={1}, systemIfType={2})";
      errorMessage = MessageFormat.format(errorMessage, syncType, operationType, systemIfType);

      logger.debug(errorMessage);
      throw new NullPointerException(errorMessage);
    }

    OperationManager opManager = OperationManager.getInstance();
    OperationExecutionStatus opExeStatus = opManager.getOperationExecutionStatus(operationType);

    if (opExeStatus == OperationExecutionStatus.ALLOWED) {
      operationId = opManager.assignOperationId();
      logger.info("Assign operationId={0} .", operationId);

    } else if (opExeStatus == OperationExecutionStatus.NOT_ALLOWED) {
      throw new MsfException(ErrorCode.SYSTEM_STATUS_ERROR, "System can not accept a request.");
    }

    if (logger.isDebugEnabled()) {
      logger.debug("[ope_id={0}]:REST-IF setting.(syncType={1}, operationType={2}, systemIfType={3})", operationId,
          syncType, operationType, systemIfType);
    }

    logger.methodEnd();
  }

  private void finalizeRestIf() {
    logger.methodStart();

    OperationManager opManager = OperationManager.getInstance();
    opManager.releaseOperationId(operationId);
    logger.info("[ope_id={0}]:Release operationId={0} .", operationId);

    operationId = null;

    logger.methodEnd();
  }

  protected String getOperationId() {
    return this.operationId;
  }

  protected void execAsyncRunner(AbstractAsyncRunner runner) throws MsfException {
    if (logger.isDebugEnabled()) {
      logger.methodStart(new String[] { "runner" }, new Object[] { runner });
    }

    if (this.syncType != SynchronousType.ASYNC) {
      String errorMessage = "Sync type is not ASYNC.";
      logger.debug("[ope_id={0}]:" + errorMessage, operationId);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, errorMessage);
    }

    runner.setScenarioType(this.syncType, this.operationType, this.systemIfType, this.operationId);

    AsyncExecutor executor = AsyncExecutor.getInstance();

    logger.info("[ope_id={0}]:Start async runner.", operationId);
    executor.submit(runner);

    this.isExecAsyncRunner = true;

    logger.methodEnd();
  }

  public RestResponseBase execute(T request) {
    if (logger.isDebugEnabled()) {
      logger.methodStart(new String[] { "request" }, new Object[] { request });
    }
    logger.info("Start scenario.");

    RestResponseBase response = null;
    try {
      this.checkParameter(request);
      this.initializeRestIf();

      if (this.syncType == SynchronousType.ASYNC) {
        this.registAsyncRecord(request);
      }

      logger.info("[ope_id={0}]:Start executeImpl.", operationId);
      response = this.executeImpl();
      logger.info("[ope_id={0}]:End executeImpl.", operationId);

    } catch (MsfException ex) {
      logger.error("[ope_id={0}]:Scenario execute failed.", ex, operationId);
      response = new ErrorResponseData(ex.getErrorCode(), systemIfType);

    } finally {
      if (operationId == null) {
        logger.debug("operationId is null. skip finalizeRestIf().");

      } else {

        if (this.isExecAsyncRunner == false && this.isRegistAsyncRecord) {
          try {
            this.deleteAsyncRecord();
          } catch (MsfException ex) {
            logger.warn("[ope_id={0}]:Delete async request faild.", ex, operationId);
          }
        }

        if (this.syncType == SynchronousType.SYNC || this.isExecAsyncRunner == false) {

          this.finalizeRestIf();
        }
      }
    }

    logger.info("End scenario.(SynchronousType={0})", syncType);
    logger.methodEnd(new String[] { "response" }, new Object[] { response });
    return response;

  }

  protected SynchronousType getSyncType() {
    return syncType;
  }

  protected abstract RestResponseBase executeImpl() throws MsfException;

  protected abstract void checkParameter(T request) throws MsfException;

  private void registAsyncRecord(T request) throws MsfException {
    if (logger.isDebugEnabled()) {
      logger.methodStart(new String[] { "request" }, new Object[] { request });
    }

    SessionWrapper sessionWrapper = new SessionWrapper();
    AsyncRequestsDao arDao = new AsyncRequestsDao();
    AsyncRequest asyncRequest = getAsyncRequest(request);

    logger.debug("[ope_id={0}]:Regist AsyncRequest={1}", operationId, asyncRequest);

    try {
      sessionWrapper.openSession();
      sessionWrapper.beginTransaction();

      arDao.create(sessionWrapper, asyncRequest);

      sessionWrapper.commit();

      this.isRegistAsyncRecord = true;
    } catch (MsfException ex) {
      logger.debug("[ope_id={0}]:DB control failed.", ex, operationId);
      sessionWrapper.rollback();
      throw ex;
    } finally {
      sessionWrapper.closeSession();
    }

    logger.methodEnd();
  }

  private void deleteAsyncRecord() throws MsfException {
    logger.methodStart();

    SessionWrapper session = new SessionWrapper();
    AsyncRequestsDao arDao = new AsyncRequestsDao();

    try {
      session.openSession();
      session.beginTransaction();

      arDao.delete(session, operationId);

      session.commit();
    } catch (MsfException ex) {
      logger.debug("[ope_id={0}]:DB control failed.", ex, operationId);
      session.rollback();
      throw ex;
    } finally {
      session.closeSession();
    }

    logger.methodEnd();
  }

  private AsyncRequest getAsyncRequest(T request) throws MsfException {
    AsyncRequest ret = new AsyncRequest();
    if (logger.isDebugEnabled()) {
      logger.methodStart(new String[] { "request" }, new Object[] { request });
    }

    if (request.getRequestUri() == null || request.getRequestMethod() == null) {
      String message = "Request parameter is incorrect.(URI={0}, HttpMethod={1})";
      message = MessageFormat.format(message, request.getRequestUri(), request.getRequestMethod());

      logger.debug(message);
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, message);
    }

    ret.setOperationId(operationId);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    ret.setOccurredTime(timestamp);
    ret.setLastUpdateTime(timestamp);
    ret.setStatusEnum(AsyncProcessStatus.WAITING);
    ret.setRequestUri(request.getRequestUri());
    ret.setRequestMethod(request.getRequestMethod());
    ret.setRequestBody(request.getRequestBody());

    logger.methodEnd(new String[] { "result" }, new Object[] { ret });
    return ret;
  }

}
