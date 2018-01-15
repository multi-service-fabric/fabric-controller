
package msf.mfcfc.core.scenario;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.common.constant.BlockadeStatus;
import msf.mfcfc.common.constant.ClusterRequestResult;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.LowerOperationStatus;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.OperationExecutionStatus;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestRequestType;
import msf.mfcfc.common.constant.RollbackResult;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.data.AsyncRequestsForLower;
import msf.mfcfc.common.data.AsyncRequestsForLowerRollback;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.core.operation.scenario.data.OperationNotifyRequestBody;
import msf.mfcfc.core.operation.scenario.data.entity.OperationRequestEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationResponseEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationRollbackEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationTargetClusterEntity;
import msf.mfcfc.core.scenario.ErrorResponse.ErrorResponseBody;
import msf.mfcfc.db.DbManager;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.db.dao.common.AsyncRequestsDao;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Base class for asynchronous runner.
 *
 * @author NTT
 *
 */
public abstract class AbstractAsyncRunner extends AbstractScenarioBase implements Callable<Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractAsyncRunner.class);

  private boolean isOperationEnd = true;

  protected final SimpleDateFormat writeDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

  protected AsyncRequest asyncRequestForNotify = null;

  public static final String NOT_NOTIFY_IP_ADDRESS = "-";

  public static final String NOT_NOTIFY_PORT_NUMBER = "-1";

  /**
   * Execution of asynchronous request.
   *
   * @return execution status of asynchronous request (there are null cases if
   *         normal termination fails)
   */
  @Override
  public Integer call() {
    try {
      logger.methodStart();

      if (restRequestType.equals(RestRequestType.NORMAL)) {
        return executeAsyncProc();
      } else {
        return executeNotifyProc();
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void setScenarioType(SynchronousType syncType, OperationType operationType,
      SystemInterfaceType systemIfType, SynchronousType lowerSystemSyncType, RestRequestType restRequestType,
      String operationId) {
    try {
      logger.methodStart(
          new String[] { "syncType", "operationType", "systemIfType", "lowerSystemSyncType", "restRequestType",
              "operationId" },
          new Object[] { syncType, operationType, systemIfType, lowerSystemSyncType, restRequestType, operationId });
      this.syncType = syncType;
      this.operationType = operationType;
      this.systemIfType = systemIfType;
      this.lowerSystemSyncType = lowerSystemSyncType;
      this.restRequestType = restRequestType;
      this.operationId = operationId;
    } finally {
      logger.methodEnd();
    }
  }

  protected void initializeAsyncRunner() throws MsfException {
    try {
      logger.methodStart();

      if (this.syncType == null || this.operationType == null || this.operationId == null
          || this.systemIfType == null) {
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

    } finally {
      logger.methodEnd();
    }
  }

  protected void finalizeAsyncRunner() {
    try {
      logger.methodStart();

      OperationManager.getInstance().releaseOperationId(operationId);
      logger.info("[ope_id={0}]:Release operationId={0} .", operationId);

    } finally {
      logger.methodEnd();
    }
  }

  protected void notifyOperationResult(RestRequestBase request) {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });
      if (request.getNotificationAddress().equals(NOT_NOTIFY_IP_ADDRESS)) {
        logger.info("do not notify operation result.");
        return;
      }

      int noticeRetryNum = ConfigManager.getInstance().getNoticeRetryNum();
      logger.debug("noticeRetryNum = " + noticeRetryNum);
      int retryNum = 0;
      while (true) {
        try {
          HttpMethod httpMethod = MfcFcRequestUri.OPERATION_RESULT_NOTIFY.getHttpMethod();
          String targetUriPath = MfcFcRequestUri.OPERATION_RESULT_NOTIFY.getUri(operationId);

          RestResponseBase response = RestClient.sendRequest(httpMethod, targetUriPath, request,
              request.getNotificationAddress(), Integer.valueOf(request.getNotificationPort()));

          if (HttpStatus.isSuccess(response.getHttpStatusCode())) {
            break;
          } else {
            logger.debug("failed to send operation result notification. retry number =" + retryNum + " response code = "
                + response.getHttpStatusCode());
            retryNum++;
          }
        } catch (MsfException exp) {
          logger.debug("failed to send operation result notification. retry number =" + retryNum, exp);
          retryNum++;
        }

        if (retryNum > noticeRetryNum) {
          logger.warn("retry count to send reaches {0}.", noticeRetryNum);
          break;
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void setOperationEndFlag(boolean isOperationEnd) {
    this.isOperationEnd = isOperationEnd;
  }

  protected abstract RestResponseBase executeImpl() throws MsfException;

  protected RestResponseBase executeRollbackImpl() throws MsfException {
    try {
      logger.methodStart();
      return null;
    } finally {
      logger.methodEnd();
    }
  }

  protected Integer executeAsyncProc() {

    boolean isScenarioFailed = false;
    AsyncRequest updateRecord = new AsyncRequest();
    AsyncProcessStatus exeStatus = AsyncProcessStatus.WAITING;
    try {
      logger.methodStart();

      exeStatus = initialize(RestRequestType.NORMAL);

      if (!exeStatus.equals(AsyncProcessStatus.RUNNING)) {
        isScenarioFailed = true;
        return exeStatus.getCode();
      }

      RestResponseBase res = null;

      if (lowerSystemSyncType.equals(SynchronousType.SYNC)) {
        setOperationEndFlag(true);
      } else {
        setOperationEndFlag(false);
      }

      try {
        logger.info("[ope_id={0}]:Start executeImpl.(Async)", operationId);
        res = executeImpl();
        logger.info("[ope_id={0}]:End executeImpl.(Async)", operationId);

        switch (checkLowerOperationResultFromResponse(res)) {
          case ALL_SUCCESS:
            logger.debug("[ope_id={0}]:Async scenario execute success.(HTTP Status Code={1})", operationId,
                res.getHttpStatusCode());
            exeStatus = AsyncProcessStatus.COMPLETED;
            break;
          default:

            logger.warn("[ope_id={0}]:Async scenario execute failed.(HTTP Status Code={1})", operationId,
                res.getHttpStatusCode());
            if (checkOperationEndStatus(res)) {
              setOperationEndFlag(true);
              exeStatus = AsyncProcessStatus.FAILED;
            } else {
              setOperationEndFlag(false);
            }
            break;
        }
      } catch (Exception ex) {
        logger.error("[ope_id={0}]:Async scenario execute failed.", ex, operationId);
        exeStatus = AsyncProcessStatus.FAILED;

        if (ex instanceof MsfException) {
          res = new ErrorResponse(((MsfException) ex).getErrorCode(), systemIfType,
              checkConsistencyNotClear((MsfException) ex));
        } else {
          res = new ErrorResponse(ErrorCode.UNDEFINED_ERROR, systemIfType);
        }

        setOperationEndFlag(true);
      }

      if (isOperationEnd) {

        updateRecord = this.updateAsyncRequest(exeStatus, res);

        notifyOperationResult(createOperationNotifyBody(updateRecord));

      }
      return exeStatus.getCode();
    } catch (Exception exp) {

      logger.error("unknown error occurred.", exp);
      isScenarioFailed = true;
      return exeStatus.getCode();
    } finally {

      if (isScenarioFailed || isOperationEnd) {
        finalizeAsyncRunner();
      }
      logger.methodEnd();
    }
  }

  protected Integer executeNotifyProc() {

    boolean isScenarioFailed = false;
    AsyncRequest updateRecord = new AsyncRequest();
    AsyncProcessStatus exeStatus = AsyncProcessStatus.RUNNING;
    try {
      logger.methodStart();

      Object lockObject = OperationManager.getInstance().getOperationIdObject(operationId);

      if (lockObject == null) {
        logger.error("failed process get lock object(operation id = {0}", operationId);

        isScenarioFailed = true;
        return AsyncProcessStatus.FAILED.getCode();
      }
      synchronized (lockObject) {

        exeStatus = initialize(RestRequestType.NOTIFICATION);

        if (!exeStatus.equals(AsyncProcessStatus.RUNNING)) {
          isScenarioFailed = true;
          return exeStatus.getCode();
        }

        RestResponseBase res = null;

        switch (checkLowerOperationResultFromAsyncRequest(asyncRequestForNotify)) {
          case ALL_SUCCESS:

            setOperationEndFlag(true);

            try {
              logger.info("[ope_id={0}]:Start executeImpl.(Notify)", operationId);
              res = executeImpl();
              logger.info("[ope_id={0}]:End executeImpl.(Notify)", operationId);

              switch (checkLowerOperationResultFromResponse(res)) {
                case ALL_SUCCESS:
                  logger.debug("[ope_id={0}]:Notify scenario execute success.(HTTP Status Code={1})", operationId,
                      res.getHttpStatusCode());

                  if (!isOperationEnd) {
                    logger.info("operation is continued.");
                  } else {
                    exeStatus = AsyncProcessStatus.COMPLETED;
                  }

                  break;
                default:
                  logger.warn("[ope_id={0}]:Notify scenario execute failed.(HTTP Status Code={1})", operationId,
                      res.getHttpStatusCode());

                  if (checkOperationEndStatus(res)) {
                    setOperationEndFlag(true);
                    exeStatus = AsyncProcessStatus.FAILED;
                  } else {
                    setOperationEndFlag(false);
                  }
                  break;
              }

            } catch (Exception ex) {
              logger.error("[ope_id={0}]:Notify scenario execute failed.", ex, operationId);
              exeStatus = AsyncProcessStatus.FAILED;

              if (ex instanceof MsfException) {
                res = new ErrorResponse(((MsfException) ex).getErrorCode(), systemIfType,
                    checkConsistencyNotClear((MsfException) ex));
              } else {
                res = new ErrorResponse(ErrorCode.UNDEFINED_ERROR, systemIfType);
              }

              setOperationEndFlag(true);
            }

            if (isOperationEnd) {

              updateRecord = this.updateAsyncRequest(exeStatus, res);

              notifyOperationResult(createOperationNotifyBody(updateRecord));
            }
            break;
          case ALL_FAILED:
            exeStatus = AsyncProcessStatus.FAILED;

            res = createErrorMessageFromAsyncRequest(asyncRequestForNotify);

            updateRecord = this.updateAsyncRequest(exeStatus, res);

            notifyOperationResult(createOperationNotifyBody(updateRecord));
            break;
          case MIXED:
          default:

            List<AsyncRequestsForLowerRollback> rollbackList = getRollbackExecute(asyncRequestForNotify);
            if (rollbackList.size() == 0) {

              exeStatus = executeRollback();
            } else {

              if (checkRollbackCompleted(rollbackList, false)) {

                res = createErrorMessageFromAsyncRequest(asyncRequestForNotify);
                exeStatus = AsyncProcessStatus.FAILED;
                updateRecord = this.updateAsyncRequest(exeStatus, res);

                notifyOperationResult(createOperationNotifyBody(updateRecord));

                if (!checkRollbackCompleted(rollbackList, true)) {

                  changeBlockadeStatus(BlockadeStatus.BLOCKADE);
                } else {
                  logger.debug("all rollback requests succeeded .");
                }
              } else {
                logger.error("rollback process not completed. operation id = {0}", operationId);
              }
            }
            break;
        }

        return exeStatus.getCode();
      }
    } catch (Exception exp) {

      logger.error("unknown error occurred.", exp);
      isScenarioFailed = true;
      return exeStatus.getCode();
    } finally {

      if (isScenarioFailed || isOperationEnd) {
        finalizeAsyncRunner();
      }
      logger.methodEnd();
    }
  }

  private AsyncRequest updateAsyncRequest(AsyncProcessStatus status, RestResponseBase restResBase) throws MsfException {
    try {
      logger.methodStart(new String[] { "status", "restResBase" }, new Object[] { status, restResBase });

      AsyncRequest ret = new AsyncRequest();
      SessionWrapper sessionWrapper = new SessionWrapper();
      AsyncRequestsDao arDao = DbManager.getInstance().createAsyncRequestsDao();

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
      logger.debug("result=" + ret);
      return ret;
    } finally {
      logger.methodEnd();
    }
  }

  private AsyncRequest updateAsyncRequest(AsyncProcessStatus status) throws MsfException {
    return updateAsyncRequest(status, null);
  }

  protected RestRequestBase createOperationNotifyBody(AsyncRequest asyncRequest) throws MsfException {
    try {
      logger.methodStart();
      OperationNotifyRequestBody body = new OperationNotifyRequestBody();
      body.setLastUpdateTime(writeDateFormat.format(asyncRequest.getLastUpdateTime()));
      body.setOccurredTime(writeDateFormat.format(asyncRequest.getOccurredTime()));
      body.setOperationId(asyncRequest.getOperationId());
      body.setStatusEnum(asyncRequest.getStatusEnum());
      body.setSubStatus(asyncRequest.getSubStatus());

      String requestBody = asyncRequest.getRequestBody() == null ? "" : asyncRequest.getRequestBody();
      OperationRequestEntity request = new OperationRequestEntity(asyncRequest.getRequestUri(),
          asyncRequest.getRequestMethod(), requestBody);
      body.setRequest(request);

      String responseBody = asyncRequest.getResponseBody() == null ? "" : asyncRequest.getResponseBody();
      OperationResponseEntity response = new OperationResponseEntity(asyncRequest.getResponseStatusCode(),
          responseBody);
      body.setResponse(response);

      List<AsyncRequestsForLower> lowerList = asyncRequest.getAsyncRequestsForLowerList();
      if (lowerList != null && asyncRequest.getAsyncRequestsForLowerList().size() != 0) {
        List<OperationTargetClusterEntity> lowerRequestList = getLowerRequestData(lowerList);
        body.setTargetClusterList(lowerRequestList);

        OperationRollbackEntity rollbackEntity = getRollbackRequestData(lowerList);
        body.setRollbacks(rollbackEntity);

      }
      RestRequestBase restRequestBase = new RestRequestBase();
      restRequestBase.setRequestBody(JsonUtil.toJson(body));
      restRequestBase.setNotificationAddress(asyncRequest.getNotificationIpAddress());
      restRequestBase.setNotificationPort(String.valueOf(asyncRequest.getNotificationPortNumber()));
      restRequestBase.setRequestMethodEnum(MfcFcRequestUri.OPERATION_RESULT_NOTIFY.getHttpMethod());
      restRequestBase.setRequestUri(MfcFcRequestUri.OPERATION_RESULT_NOTIFY.getUri(asyncRequest.getOperationId()));

      return restRequestBase;
    } finally {
      logger.methodEnd();
    }
  }

  private List<OperationTargetClusterEntity> getLowerRequestData(List<AsyncRequestsForLower> lowerList) {
    try {
      logger.methodStart();
      List<OperationTargetClusterEntity> entityList = new ArrayList<>();
      for (AsyncRequestsForLower record : lowerList) {
        OperationTargetClusterEntity entity = new OperationTargetClusterEntity();
        entity.setClusterId(String.valueOf(record.getId().getClusterId()));

        String requestBody = record.getRequestBody() == null ? "" : record.getRequestBody();
        OperationRequestEntity request = new OperationRequestEntity(record.getRequestUri(), record.getRequestMethod(),
            requestBody);
        entity.setRequest(request);

        OperationResponseEntity response = new OperationResponseEntity(record.getResponseStatusCode(),
            record.getResponseBody());
        entity.setResponse(response);
        entityList.add(entity);
      }
      return entityList;
    } finally {
      logger.methodEnd();
    }
  }

  private OperationRollbackEntity getRollbackRequestData(List<AsyncRequestsForLower> lowerList) throws MsfException {
    try {
      logger.methodStart();

      List<AsyncRequestsForLowerRollback> rollbackList = new ArrayList<>();
      for (AsyncRequestsForLower lowerRecord : lowerList) {
        if (lowerRecord.getAsyncRequestsForLowerRollback() != null) {
          rollbackList.add(lowerRecord.getAsyncRequestsForLowerRollback());
        }
      }

      if (rollbackList.size() == 0) {
        return null;
      }

      OperationRollbackEntity rollbackEntity = new OperationRollbackEntity();
      rollbackEntity.setResult(RollbackResult.SUCCESS.getMessage());
      rollbackEntity.setOccurredTime(writeDateFormat.format(rollbackList.get(0).getOccurredTime()));
      List<OperationTargetClusterEntity> entityList = new ArrayList<>();
      for (AsyncRequestsForLowerRollback record : rollbackList) {
        OperationTargetClusterEntity entity = new OperationTargetClusterEntity();
        entity.setClusterId(String.valueOf(record.getId().getClusterId()));

        OperationRequestEntity request = new OperationRequestEntity(record.getRequestUri(), record.getRequestMethod(),
            record.getRequestBody());
        entity.setRequest(request);

        OperationResponseEntity response = new OperationResponseEntity(record.getResponseStatusCode(),
            record.getResponseBody());
        entity.setResponse(response);
        entityList.add(entity);

        if (!HttpStatus.isSuccess(record.getResponseStatusCode())) {
          rollbackEntity.setResult(RollbackResult.FAILED.getMessage());
        }
      }
      rollbackEntity.setTargetClusterList(entityList);
      return rollbackEntity;
    } finally {
      logger.methodEnd();
    }
  }

  private LowerOperationStatus checkLowerOperationResultFromAsyncRequest(AsyncRequest asyncRequest) {
    try {
      logger.methodStart(new String[] { "asyncRequest" }, new Object[] { asyncRequest });
      int successNum = 0;
      int failedNum = 0;
      for (AsyncRequestsForLower lower : asyncRequest.getAsyncRequestsForLowerList()) {
        if (HttpStatus.isSuccess(lower.getResponseStatusCode())) {
          successNum++;
        } else {
          failedNum++;
        }
      }
      if (successNum == asyncRequest.getAsyncRequestsForLowerList().size()) {
        return LowerOperationStatus.ALL_SUCCESS;
      } else if (failedNum == asyncRequest.getAsyncRequestsForLowerList().size()) {
        return LowerOperationStatus.ALL_FAILED;
      } else {
        return LowerOperationStatus.MIXED;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private LowerOperationStatus checkLowerOperationResultFromResponse(RestResponseBase restResponseBase)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "restResponseBase" }, new Object[] { restResponseBase });

      if (HttpStatus.isSuccess(restResponseBase.getHttpStatusCode())) {
        return LowerOperationStatus.ALL_SUCCESS;
      } else {

        ErrorResponseBody body = JsonUtil.fromJson(restResponseBase.getResponseBody(), ErrorResponseBody.class,
            ErrorCode.UNDEFINED_ERROR);
        int failedNum = 0;
        if (body.getRequestTargetClusterList() != null) {
          for (TargetClusterEntity entity : body.getRequestTargetClusterList()) {
            if (!entity.getRequestResultsEnum().equals(ClusterRequestResult.SUCCESS)) {
              failedNum++;
            }
          }

          if (failedNum == body.getRequestTargetClusterList().size()) {
            return LowerOperationStatus.ALL_FAILED;
          } else {
            return LowerOperationStatus.MIXED;
          }
        } else {
          return LowerOperationStatus.ALL_FAILED;
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkOperationEndStatus(RestResponseBase restResponseBase) throws MsfException {
    try {
      logger.methodStart(new String[] { "restResponseBase" }, new Object[] { restResponseBase });

      if (restResponseBase.isFailedRegistRecord()) {

        return true;
      }

      switch (checkLowerOperationResultFromResponse(restResponseBase)) {
        case ALL_FAILED:
          return true;
        default:
          return false;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createErrorMessageFromAsyncRequest(AsyncRequest asyncRequest) throws MsfException {
    try {
      logger.methodStart(new String[] { "asyncRequest" }, new Object[] { asyncRequest });
      List<RestResponseData> responseDataList = new ArrayList<>();
      List<RestResponseData> rollbackResponseDataList = new ArrayList<>();
      for (AsyncRequestsForLower lower : asyncRequest.getAsyncRequestsForLowerList()) {

        RestRequestData request = new RestRequestData();
        request.setClusterId(lower.getId().getClusterId());
        RestResponseBase response = new RestResponseBase(lower.getResponseStatusCode(), lower.getResponseBody());
        RestResponseData responseData = new RestResponseData(request, response);
        responseDataList.add(responseData);

        if (lower.getAsyncRequestsForLowerRollback() != null) {
          AsyncRequestsForLowerRollback rollback = lower.getAsyncRequestsForLowerRollback();

          RestRequestData requestRollback = new RestRequestData();
          requestRollback.setClusterId(rollback.getId().getClusterId());
          RestResponseBase responseRollback = new RestResponseBase(rollback.getResponseStatusCode(),
              rollback.getResponseBody());
          RestResponseData responseDataRollback = new RestResponseData(requestRollback, responseRollback);
          rollbackResponseDataList.add(responseDataRollback);
        }
      }
      return createErrorResponse(responseDataList, rollbackResponseDataList);
    } finally {
      logger.methodEnd();
    }
  }

  private List<AsyncRequestsForLowerRollback> getRollbackExecute(AsyncRequest asyncRequest) throws MsfException {
    try {
      logger.methodStart(new String[] { "asyncRequest" }, new Object[] { asyncRequest });
      List<AsyncRequestsForLowerRollback> rollbackList = new ArrayList<>();
      if (asyncRequest.getAsyncRequestsForLowerList() != null) {
        for (AsyncRequestsForLower lower : asyncRequest.getAsyncRequestsForLowerList()) {
          if (lower.getAsyncRequestsForLowerRollback() != null) {
            AsyncRequestsForLowerRollback rollback = lower.getAsyncRequestsForLowerRollback();
            rollbackList.add(rollback);
          }
        }
      }
      return rollbackList;
    } finally {
      logger.methodEnd();
    }
  }

  private boolean checkRollbackCompleted(List<AsyncRequestsForLowerRollback> rollbackList, boolean successCheck) {
    try {
      logger.methodStart(new String[] { "rollbackList" }, new Object[] { rollbackList });
      for (AsyncRequestsForLowerRollback rollback : rollbackList) {

        if (rollback.getResponseStatusCode() == null) {
          return false;
        } else {

          if (successCheck) {
            if (!HttpStatus.isSuccess(rollback.getResponseStatusCode())) {
              return false;
            }
          }
        }
      }
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  private AsyncProcessStatus initialize(RestRequestType restRequestType) {
    try {
      logger.methodStart();
      AsyncRequest updateRecord = new AsyncRequest();

      try {
        initializeAsyncRunner();
      } catch (Exception ex) {
        logger.error("[ope_id={0}]:initializeAsyncRunner failed.", ex, operationId);

        if (restRequestType.equals(RestRequestType.NORMAL)) {

          updateRecord = updateAsyncRequest(AsyncProcessStatus.CANCELED);
        } else {

          updateRecord = updateAsyncRequest(AsyncProcessStatus.FAILED);
        }

        notifyOperationResult(createOperationNotifyBody(updateRecord));
        return updateRecord.getStatusEnum();
      }

      if (restRequestType.equals(RestRequestType.NORMAL)) {

        logger.debug("scenario initialize success.");
        updateRecord = updateAsyncRequest(AsyncProcessStatus.RUNNING);
      }
      return AsyncProcessStatus.RUNNING;
    } catch (Exception ex) {

      logger.error("[ope_id={0}]:initialize failed.", ex, operationId);
      return AsyncProcessStatus.FAILED;
    } finally {
      logger.methodEnd();
    }
  }

  private AsyncProcessStatus executeRollback() throws MsfException {
    try {
      logger.methodStart();
      RestResponseBase res = null;
      AsyncProcessStatus exeStatus = AsyncProcessStatus.RUNNING;

      setOperationEndFlag(false);
      try {

        logger.info("[ope_id={0}]:Start executeRollbackImpl.(Notify)", operationId);
        res = executeRollbackImpl();
        logger.info("[ope_id={0}]:End executeRollbackImpl.(Notify)", operationId);

        if (res == null) {
          logger.warn("rollback process not implemented.");
          setOperationEndFlag(true);
          exeStatus = AsyncProcessStatus.FAILED;
        } else {
          switch (checkLowerOperationResultFromResponse(res)) {
            case ALL_SUCCESS:
              logger.debug("[ope_id={0}]:Notify scenario execute rollback success.(HTTP Status Code={1})", operationId,
                  res.getHttpStatusCode());
              break;
            default:
              logger.warn("[ope_id={0}]:Notify scenario execute rollback failed.(HTTP Status Code={1})", operationId,
                  res.getHttpStatusCode());

              if (checkOperationEndStatus(res)) {
                setOperationEndFlag(true);
                exeStatus = AsyncProcessStatus.FAILED;
              } else {
                setOperationEndFlag(false);
              }
              break;
          }
        }
      } catch (Exception ex) {
        logger.error("[ope_id={0}]:Notify scenario execute rollback failed.", ex, operationId);
        exeStatus = AsyncProcessStatus.FAILED;

        if (ex instanceof MsfException) {
          res = new ErrorResponse(((MsfException) ex).getErrorCode(), systemIfType);
        } else {
          res = new ErrorResponse(ErrorCode.UNDEFINED_ERROR, systemIfType);
        }

        setOperationEndFlag(true);

      }

      if (isOperationEnd) {
        AsyncRequest updateRecord = this.updateAsyncRequest(exeStatus, res);
        notifyOperationResult(createOperationNotifyBody(updateRecord));
        changeBlockadeStatus(BlockadeStatus.BLOCKADE);
      }
      return exeStatus;
    } finally {
      logger.methodEnd();
    }
  }

  protected Map<Integer, String> getLowerOperationIdMap(List<AsyncRequestsForLower> asyncRequestsForLowerList) {
    try {
      logger.methodStart();
      Map<Integer, String> map = new HashMap<>();
      for (AsyncRequestsForLower asyncRequestsForLower : asyncRequestsForLowerList) {
        map.put(asyncRequestsForLower.getId().getClusterId(), asyncRequestsForLower.getId().getRequestOperationId());
      }
      return map;
    } finally {
      logger.methodEnd();
    }
  }

  protected boolean checkConsistencyNotClear(MsfException msfException) throws MsfException {
    try {
      logger.methodStart();
      AsyncRequest asyncRequest = new AsyncRequest();
      SessionWrapper sessionWrapper = new SessionWrapper();
      AsyncRequestsDao arDao = DbManager.getInstance().createAsyncRequestsDao();

      try {
        sessionWrapper.openSession();

        asyncRequest = arDao.read(sessionWrapper, operationId);
        if (asyncRequest == null) {
          String errorMessage = "Record not found in async_requests table.(operation_id={0})";
          errorMessage = MessageFormat.format(errorMessage, operationId);
          logger.debug(errorMessage);
          throw new MsfException(ErrorCode.UNDEFINED_ERROR, errorMessage);
        }
        List<AsyncRequestsForLower> lowerList = asyncRequest.getAsyncRequestsForLowerList();
        if (lowerList != null && asyncRequest.getAsyncRequestsForLowerList().size() != 0) {
          if (!msfException.getErrorCode().getCode().startsWith("90")) {
            return true;
          } else {
            return false;
          }
        } else {
          return false;
        }
      } catch (MsfException ex) {
        throw ex;
      } finally {
        sessionWrapper.closeSession();
      }
    } finally {
      logger.methodEnd();
    }
  }
}