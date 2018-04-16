
package msf.mfcfc.core.scenario;

import java.sql.Timestamp;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.data.AsyncRequestsForLower;
import msf.mfcfc.common.data.AsyncRequestsForLowerPK;
import msf.mfcfc.common.data.AsyncRequestsForLowerRollback;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.AbstractOperationNotifyTimerTask;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.db.DbManager;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.db.dao.common.AsyncRequestsDao;
import msf.mfcfc.db.dao.common.AsyncRequestsForLowerDao;
import msf.mfcfc.db.dao.common.AsyncRequestsForLowerRollbackDao;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Base class for request send runner.
 *
 * @author NTT
 *
 */
public class RequestSendRunner implements Callable<RestResponseData> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractScenario.class);

  private RestRequestData restRequestData;

  private TimerTaskMaker timerTaskMaker;

  private SynchronousType synchronousType = SynchronousType.SYNC;

  private RequestType requestType = RequestType.REQUEST;

  private String operationId;

  private static final Object lockObject = new Object();

  private static String lastSurrogateKey = "";

  private static final String SURROGATE_KEY_NO_RESPONSE = "_no_response";

  private static final String OPERAION_NOTIFY_IP_ADDRESS = "notification_address";

  private static final String OPERATION_NOTIFY_PORT_NUMBER = "notification_port";

  /**
   * Constructor.
   *
   * @param restRequestData
   *          Request data to send
   * @param operationId
   *          Upper operation ID
   */
  public RequestSendRunner(RestRequestData restRequestData, String operationId) {
    this.restRequestData = restRequestData;
    this.operationId = operationId;
  }

  @Override
  public RestResponseData call() {
    try {
      logger.methodStart();

      RestResponseBase restResponseBase = executeImpl();

      if (synchronousType.equals(SynchronousType.ASYNC)) {
        try {

          if (!restResponseBase.isFailedSendRequest()) {

            if (timerTaskMaker != null) {

              setTimerForOperation(restRequestData.getClusterId(), operationId, restResponseBase);
            }
          }

          registAsyncRecord(restResponseBase, operationId);
        } catch (Exception exp) {
          logger.warn("error occurred after send async request.", exp);

          restResponseBase = new ErrorResponse(ErrorCode.UNDEFINED_ERROR, SystemInterfaceType.EXTERNAL);

          restResponseBase.setFailedRegistRecord(true);
        }
      }

      RestResponseData restResponseData = new RestResponseData(restRequestData, restResponseBase);
      return restResponseData;
    } finally {
      logger.methodEnd();
    }
  }

  protected void setSendRequestSyncType(SynchronousType synchronousType) {
    this.synchronousType = synchronousType;
  }

  /**
   * Set the request type.
   *
   * @param requestType
   *          Request type
   */
  public void setRequestType(RequestType requestType) {
    this.requestType = requestType;
  }

  /**
   * Set the operation ID.
   *
   * @param operationId
   *          Opeation ID
   */
  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  protected void setTimerForOperation(int clusterId, String operationId, RestResponseBase restResponseBase)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "clusterId", "operationId", "restResponseBase" },
          new Object[] { clusterId, operationId, restResponseBase });
      String lowerOperationId = getLowerOperationId(restResponseBase);

      TimerTask timerTask = timerTaskMaker.makeTimerTask();
      if (timerTask instanceof AbstractOperationNotifyTimerTask) {
        ((AbstractOperationNotifyTimerTask) timerTask).setClusterId(String.valueOf(clusterId));
        ((AbstractOperationNotifyTimerTask) timerTask).setLowerOperationId(lowerOperationId);
      }

      int timeout = ConfigManager.getInstance().getWaitOperationResultTimeout();
      OperationManager.getInstance().setTimerForOperation(operationId, String.valueOf(clusterId), lowerOperationId,
          timeout, timerTask);
    } finally {
      logger.methodEnd();
    }
  }

  protected void registAsyncRecord(RestResponseBase restResponseBase, String operationId) throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart(new String[] { "restResponseBase" }, new Object[] { restResponseBase });
      sessionWrapper.openSession();
      sessionWrapper.beginTransaction();

      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      if (requestType.equals(RequestType.REQUEST)) {

        AsyncRequestsForLower entity = new AsyncRequestsForLower();
        AsyncRequestsForLowerPK id = new AsyncRequestsForLowerPK();
        id.setClusterId(restRequestData.getClusterId());
        entity.setId(id);
        entity.setLastUpdateTime(timestamp);
        entity.setOccurredTime(timestamp);
        if (restRequestData.getRequest() != null) {
          entity.setRequestBody(restRequestData.getRequest().getRequestBody());
        } else {

          entity.setRequestBody("");
        }
        entity.setRequestMethod(restRequestData.getHttpMethod().getMessage());
        entity.setRequestUri(restRequestData.getTargetUri());

        AsyncRequestsDao asyncRequestsDao = DbManager.getInstance().createAsyncRequestsDao();
        AsyncRequest asyncRequest = asyncRequestsDao.read(sessionWrapper, operationId);
        entity.setAsyncRequest(asyncRequest);
        if (restResponseBase.getHttpStatusCode() == HttpStatus.ACCEPTED_202) {

          id.setRequestOperationId(getLowerOperationId(restResponseBase));
        } else {
          id.setRequestOperationId(createSurrogateKey());
          entity.setResponseBody(restResponseBase.getResponseBody());
          entity.setResponseStatusCode(restResponseBase.getHttpStatusCode());
        }
        AsyncRequestsForLowerDao dao = DbManager.getInstance().createAsyncRequestsForLowerDao();
        dao.create(sessionWrapper, entity);
      } else {

        AsyncRequestsForLowerRollback entity = new AsyncRequestsForLowerRollback();
        AsyncRequestsForLowerPK id = new AsyncRequestsForLowerPK();
        id.setClusterId(restRequestData.getClusterId());
        id.setRequestOperationId(restRequestData.getLowerOperationId());
        entity.setId(id);
        entity.setLastUpdateTime(timestamp);
        entity.setOccurredTime(timestamp);
        if (restRequestData.getRequest() != null) {
          entity.setRequestBody(restRequestData.getRequest().getRequestBody());
        } else {

          entity.setRequestBody("");
        }
        entity.setRequestMethod(restRequestData.getHttpMethod().getMessage());
        entity.setRequestUri(restRequestData.getTargetUri());
        if (restResponseBase.getHttpStatusCode() == HttpStatus.ACCEPTED_202) {

          entity.setRollbackOperationId(getLowerOperationId(restResponseBase));
        } else {
          entity.setRollbackOperationId(createSurrogateKey());
          entity.setResponseBody(restResponseBase.getResponseBody());
          entity.setResponseStatusCode(restResponseBase.getHttpStatusCode());
        }
        AsyncRequestsForLowerRollbackDao dao = DbManager.getInstance().createAsyncRequestsForLowerRollbackDao();
        dao.create(sessionWrapper, entity);
      }

      sessionWrapper.commit();
    } catch (MsfException exp) {
      logger.error(exp.getMessage(), exp);
      sessionWrapper.rollback();
      throw exp;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  protected RestResponseBase executeImpl() {
    RestResponseBase restResponseBase = null;
    try {
      logger.methodStart();

      HttpMethod httpMethod = restRequestData.getHttpMethod();
      String targetUriPath = restRequestData.getTargetUri();
      RestRequestBase restRequest = restRequestData.getRequest();
      String ipAddress = restRequestData.getIpAddress();
      int port = restRequestData.getPort();
      logger.debug("restRequestData = {0}", restRequestData);

      try {

        if (synchronousType.equals(SynchronousType.ASYNC) && !targetUriPath.contains(OPERAION_NOTIFY_IP_ADDRESS)
            && !targetUriPath.contains(OPERATION_NOTIFY_PORT_NUMBER)) {
          String separatorChar = targetUriPath.contains("?") ? "&" : "?";
          targetUriPath = targetUriPath + separatorChar + OPERAION_NOTIFY_IP_ADDRESS + "="
              + ConfigManager.getInstance().getRestServerListeningAddress() + "&" + OPERATION_NOTIFY_PORT_NUMBER + "="
              + ConfigManager.getInstance().getRestServerListeningPort();
          logger.debug("targetUriPath=" + targetUriPath);
        }
        restResponseBase = RestClient.sendRequest(httpMethod, targetUriPath, restRequest, ipAddress, port);
        if (HttpStatus.isSuccess(restResponseBase.getHttpStatusCode())) {

          if (restResponseBase.getHttpStatusCode() != restRequestData.getExpectHttpStatusCode()) {

            logger.warn("response http status code {0} different from expect http status code {1}.",
                restResponseBase.getHttpStatusCode(), restRequestData.getExpectHttpStatusCode());
            restResponseBase = new ErrorResponse(ErrorCode.UNDEFINED_ERROR, SystemInterfaceType.EXTERNAL);

            restResponseBase.setFailedSendRequest(true);
          }
        } else {
          logger.warn("response http status code {0} ", restResponseBase.getHttpStatusCode());

          restResponseBase.setFailedSendRequest(true);
        }
      } catch (MsfException exp) {
        logger.debug("failed executeImpl.");
        restResponseBase = new ErrorResponse(exp.getErrorCode(), SystemInterfaceType.EXTERNAL);

        restResponseBase.setFailedSendRequest(true);
      } catch (Exception exp) {
        logger.debug("failed executeImpl.", exp);
        restResponseBase = new ErrorResponse(ErrorCode.UNDEFINED_ERROR, SystemInterfaceType.EXTERNAL);

        restResponseBase.setFailedSendRequest(true);
      }

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Set the implementation for task generation at timeout on the timer to
   * receive operation result notification.
   *
   * @param timerTaskMaker
   *          Implementation for task generation at timeout
   */
  public void setTimerTaskMaker(TimerTaskMaker timerTaskMaker) {
    this.timerTaskMaker = timerTaskMaker;
  }

  private String createSurrogateKey() {

    synchronized (lockObject) {
      try {
        logger.methodStart();
        String key;

        while (true) {
          long currentTime = System.currentTimeMillis();

          key = String.valueOf(currentTime) + SURROGATE_KEY_NO_RESPONSE;

          if (!key.equals(lastSurrogateKey)) {

            lastSurrogateKey = key;
            break;
          }
        }

        logger.debug("create surrogate key : {0}", key);
        return key;
      } finally {
        logger.methodEnd();
      }
    }
  }

  private String getLowerOperationId(RestResponseBase restResponseBase) throws MsfException {
    try {
      logger.methodStart();
      AsyncOperationResponseBody body = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          AsyncOperationResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
      return body.getOperationId();
    } finally {
      logger.methodEnd();
    }
  }
}
