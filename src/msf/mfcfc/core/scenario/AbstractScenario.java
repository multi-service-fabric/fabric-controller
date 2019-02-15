
package msf.mfcfc.core.scenario;

import java.sql.Timestamp;
import java.text.MessageFormat;

import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.common.constant.BlockadeStatus;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.OperationExecutionStatus;
import msf.mfcfc.common.constant.RestRequestType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.async.AsyncExecutor;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.db.DbManager;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.db.dao.common.AsyncRequestsDao;

/**
 * Base class for scenario.
 *
 * @author NTT
 *
 * @param <T>
 *          REST request
 */
public abstract class AbstractScenario<T extends RestRequestBase> extends AbstractScenarioBase {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractScenario.class);

  private boolean isExecAsyncRunner = false;

  private boolean isRegistAsyncRecord = false;

  protected void setRestIfType(SynchronousType syncType) {
    if (logger.isDebugEnabled()) {
      logger.methodStart(new String[] { "syncType" }, new Object[] { syncType });
    }

    this.syncType = syncType;

    logger.methodEnd();
  }

  private void initializeRestIf(T request) throws MsfException {
    try {
      logger.methodStart();

      if (this.syncType == null || this.operationType == null || systemIfType == null) {
        String errorMessage = "REST-IF setting is null. (syncType={0}, operationType={1}, systemIfType={2})";
        errorMessage = MessageFormat.format(errorMessage, syncType, operationType, systemIfType);

        logger.debug(errorMessage);
        throw new NullPointerException(errorMessage);
      }

      OperationManager opManager = OperationManager.getInstance();

      boolean isGetRequest = HttpMethod.GET.equals(request.getRequestMethodEnum());
      OperationExecutionStatus opExeStatus = opManager.getOperationExecutionStatus(operationType, isGetRequest);

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

    } finally {
      logger.methodEnd();
    }
  }

  private void finalizeRestIf() {
    try {
      logger.methodStart();

      OperationManager opManager = OperationManager.getInstance();
      opManager.releaseOperationId(operationId);
      logger.info("[ope_id={0}]:Release operationId={0} .", operationId);

      operationId = null;

    } finally {
      logger.methodEnd();
    }
  }

  protected void execAsyncRunner(AbstractAsyncRunner runner) throws MsfException {
    try {
      logger.methodStart(new String[] { "runner" }, new Object[] { runner });
      execAsyncRunner(runner, getOperationId());
    } finally {
      logger.methodEnd();
    }
  }

  protected void execAsyncRunner(AbstractAsyncRunner runner, String operationId) throws MsfException {
    try {
      logger.methodStart(new String[] { "runner", "operationId" }, new Object[] { runner, operationId });

      if (this.syncType != SynchronousType.ASYNC) {
        String errorMessage = "Sync type is not ASYNC.";
        logger.debug("[ope_id={0}]:" + errorMessage, operationId);
        throw new MsfException(ErrorCode.UNDEFINED_ERROR, errorMessage);
      }

      runner.setScenarioType(this.syncType, this.operationType, this.systemIfType, this.lowerSystemSyncType,
          this.restRequestType, operationId);

      AsyncExecutor executor = AsyncExecutor.getInstance();

      logger.info("[ope_id={0}]:Start async runner.", operationId);
      executor.submit(runner);

      this.isExecAsyncRunner = true;

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Execute the scenario.
   *
   * @param request
   *          Request information
   * @return execution result information
   */
  public RestResponseBase execute(T request) {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });
      logger.info("Start scenario.");

      RestResponseBase response = null;
      try {
        this.checkParameter(request);
        this.initializeRestIf(request);

        if (this.syncType == SynchronousType.ASYNC && this.restRequestType == RestRequestType.NORMAL) {
          this.registAsyncRecord(request);
        }

        logger.info("[ope_id={0}]:Start executeImpl.", operationId);
        response = this.executeImpl();
        logger.info("[ope_id={0}]:End executeImpl.", operationId);

        if (this.syncType == SynchronousType.SYNC) {

          if (response.isFailedRollback()) {
            changeBlockadeStatus(BlockadeStatus.BLOCKADE);
          }
        }

      } catch (MsfException ex) {
        logger.error("[ope_id={0}]:Scenario execute failed.", ex, operationId);

        response = new ErrorResponse(ex.getErrorCode(), systemIfType);

      } finally {
        if (operationId == null) {

          logger.debug("operationId is null. skip finalizeRestIf().");

        } else {

          if (!this.isExecAsyncRunner && this.isRegistAsyncRecord) {

            try {
              this.deleteAsyncRecord();
            } catch (MsfException ex) {

              logger.warn("[ope_id={0}]:Delete async request faild.", ex, operationId);
            }
          }

          if (this.syncType == SynchronousType.SYNC || !this.isExecAsyncRunner
              || this.restRequestType == RestRequestType.NOTIFICATION) {

            this.finalizeRestIf();
          }
        }
      }

      logger.info("End scenario.(SynchronousType={0})", syncType);
      logger.debug("response={0}", response);
      return response;
    } finally {
      logger.methodEnd();
    }

  }

  protected SynchronousType getSyncType() {
    return syncType;
  }

  protected abstract RestResponseBase executeImpl() throws MsfException;

  protected abstract void checkParameter(T request) throws MsfException;

  protected void registAsyncRecord(T request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      SessionWrapper sessionWrapper = new SessionWrapper();
      AsyncRequestsDao arDao = DbManager.getInstance().createAsyncRequestsDao();
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

    } finally {
      logger.methodEnd();
    }
  }

  private void deleteAsyncRecord() throws MsfException {
    try {
      logger.methodStart();

      SessionWrapper session = new SessionWrapper();
      AsyncRequestsDao arDao = DbManager.getInstance().createAsyncRequestsDao();

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

    } finally {
      logger.methodEnd();
    }
  }

  private AsyncRequest getAsyncRequest(T request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });
      AsyncRequest ret = new AsyncRequest();

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
      ret.setNotificationIpAddress(request.getNotificationAddress());
      if (request.getNotificationPort() != null) {
        ret.setNotificationPortNumber(Integer.valueOf(request.getNotificationPort()));
      }

      logger.debug("result={0}", ret);
      return ret;
    } finally {
      logger.methodEnd();
    }
  }
}
