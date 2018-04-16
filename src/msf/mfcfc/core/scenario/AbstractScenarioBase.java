
package msf.mfcfc.core.scenario;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.BlockadeStatus;
import msf.mfcfc.common.constant.ClusterRequestResult;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.ErrorResponseDataConsistency;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.constant.RestRequestType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.async.SendRequestExecutor;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.core.scenario.ErrorResponse.ErrorResponseBody;
import msf.mfcfc.core.status.SystemStatusManager;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Common class for synchronous/asynchronous base class.
 *
 * @author NTT
 *
 */
public abstract class AbstractScenarioBase {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractScenarioBase.class);

  protected SynchronousType syncType = null;

  protected String operationId = null;

  protected OperationType operationType = null;

  protected SystemInterfaceType systemIfType = null;

  protected RestRequestType restRequestType = RestRequestType.NORMAL;

  protected SynchronousType lowerSystemSyncType = SynchronousType.SYNC;

  protected TimerTaskMaker timerTaskMaker = null;

  protected List<RestResponseData> sendRequest(List<RestRequestData> requestDataList, RequestType requestType)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "requestDataList", "requestType" },
          new Object[] { requestDataList, requestType });
      List<RequestSendRunner> taskList = new ArrayList<>();
      for (RestRequestData restRequestData : requestDataList) {
        RequestSendRunner requestSendRunner = new RequestSendRunner(restRequestData, operationId);

        requestSendRunner.setSendRequestSyncType(lowerSystemSyncType);

        requestSendRunner.setRequestType(requestType);

        if (timerTaskMaker != null) {
          requestSendRunner.setTimerTaskMaker(timerTaskMaker);
        }
        taskList.add(requestSendRunner);
      }

      OperationManager.getInstance().setLowerRequestNumber(operationId, taskList.size());

      int invokeAllTimeout = ConfigManager.getInstance().getInvokeAllTimout();

      List<RestResponseData> responseDataList = SendRequestExecutor.getInstance().invokeAll(taskList, invokeAllTimeout,
          TimeUnit.MILLISECONDS);

      return responseDataList;

    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseData sendRequest(RestRequestData requestData, RequestType requestType) throws MsfException {
    try {
      logger.methodStart(new String[] { "requestData", "requestType" }, new Object[] { requestData, requestType });
      List<RestRequestData> requestDataList = new ArrayList<>();
      requestDataList.add(requestData);
      List<RestResponseData> responseDataList = sendRequest(requestDataList, requestType);
      if (responseDataList.size() != 0) {
        return responseDataList.get(0);
      } else {
        return null;
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected boolean checkRollbackRequired(List<RestResponseData> responseDataList) {
    try {
      logger.methodStart(new String[] { "responseDataList" }, new Object[] { responseDataList });
      int successNum = 0;
      int failureNum = 0;
      for (RestResponseData restResponseData : responseDataList) {

        if (HttpStatus.isSuccess(restResponseData.getResponse().getHttpStatusCode())) {
          successNum++;
        } else {
          failureNum++;
        }
      }

      if (successNum >= 1 && failureNum >= 1) {

        return true;
      } else {

        return false;
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected boolean checkResponseAllSuccess(List<RestResponseData> responseDataList) {
    try {
      logger.methodStart(new String[] { "responseDataList" }, new Object[] { responseDataList });
      for (RestResponseData restResponseData : responseDataList) {
        if (!checkResponseSuccess(restResponseData)) {
          return false;
        }
      }
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  protected boolean checkResponseSuccess(RestResponseData responseData) {
    try {
      logger.methodStart(new String[] { "responseData" }, new Object[] { responseData });

      if (!HttpStatus.isSuccess(responseData.getResponse().getHttpStatusCode())) {
        return false;
      }
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createErrorResponse(List<RestResponseData> responseDataList,
      List<RestResponseData> rollbackResponseDataList) throws MsfException {
    try {
      logger.methodStart(new String[] { "responseDataList", "rollbackResponseDataList" },
          new Object[] { responseDataList, rollbackResponseDataList });

      List<TargetClusterEntity> targetClusterList = createErrorResponseDataListEachCluster(responseDataList,
          rollbackResponseDataList);

      ErrorResponseDataConsistency dataConsistency = getDataConsistencyFromTargetClusterList(targetClusterList);

      ErrorResponse errorResponse = new ErrorResponse(ErrorCode.CLUSTER_CONTROL_ERROR, dataConsistency,
          targetClusterList);

      setFailedStatus(errorResponse, responseDataList, rollbackResponseDataList);

      return errorResponse;

    } catch (Exception ex) {

      String errorMessage = "error occurred while create error resoponse.";
      logger.warn(errorMessage);
      if (!(ex instanceof MsfException)) {
        logger.warn("unknown error occurred.", ex);
      }
      throw new MsfException(ErrorCode.UNDEFINED_ERROR, errorMessage);
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createErrorResponse(RestResponseData restResponseData) throws MsfException {
    try {
      logger.methodStart(new String[] { "restResponseData" }, new Object[] { restResponseData });
      List<RestResponseData> restResponseDataList = new ArrayList<>();
      restResponseDataList.add(restResponseData);
      return createErrorResponse(restResponseDataList, null);
    } finally {
      logger.methodEnd();
    }
  }

  protected void changeBlockadeStatus(BlockadeStatus blockadeStatus) throws MsfException {
    try {
      logger.methodStart(new String[] { "blockadeStatus" }, new Object[] { blockadeStatus });

      SystemStatus systemStatus = SystemStatusManager.getInstance().getSystemStatus();

      systemStatus.setBlockadeStatus(blockadeStatus.getCode());
      SystemStatusManager.getInstance().changeSystemStatus(systemStatus);
    } finally {
      logger.methodEnd();
    }

  }

  private List<TargetClusterEntity> createErrorResponseDataListEachCluster(List<RestResponseData> responseDataList,
      List<RestResponseData> rollbackResponseDataList) throws MsfException {
    try {
      logger.methodStart(new String[] { "responseDataList", "rollbackResponseDataList" },
          new Object[] { responseDataList, rollbackResponseDataList });
      List<TargetClusterEntity> targetClusterList = new ArrayList<>();

      for (RestResponseData restResponseData : responseDataList) {

        RestResponseData rollbackResponseData = searchRollbackResponseData(restResponseData, rollbackResponseDataList);

        if (rollbackResponseData == null) {

          if (HttpStatus.isSuccess(restResponseData.getResponse().getHttpStatusCode())) {

            ErrorResponseDataConsistency errorResponseDataConsistency = null;
            if (restResponseData.getRequest() != null
                && HttpMethod.GET.equals(restResponseData.getRequest().getHttpMethod())) {
              errorResponseDataConsistency = ErrorResponseDataConsistency.ROLLED_BACK;
            } else {
              errorResponseDataConsistency = ErrorResponseDataConsistency.UPDATED;
            }

            TargetClusterEntity targetClusterEntity = createTargetClusterEntity(restResponseData,
                ClusterRequestResult.SUCCESS, null, errorResponseDataConsistency);
            targetClusterList.add(targetClusterEntity);

          } else {

            ErrorCode errorCode = getErrorCodeFromResponse(restResponseData);

            TargetClusterEntity targetClusterEntity = createTargetClusterEntity(restResponseData,
                ClusterRequestResult.FAILED, errorCode,
                errorCode.getDataConsistency().getErrorResponseDataConsistency());
            targetClusterList.add(targetClusterEntity);
          }
        } else {

          if (HttpStatus.isSuccess(rollbackResponseData.getResponse().getHttpStatusCode())) {

            TargetClusterEntity targetClusterEntity = createTargetClusterEntity(rollbackResponseData,
                ClusterRequestResult.SUCCESS, null, ErrorResponseDataConsistency.ROLLED_BACK);
            targetClusterList.add(targetClusterEntity);

          } else {

            ErrorCode errorCode = getErrorCodeFromResponse(rollbackResponseData);

            TargetClusterEntity targetClusterEntity = createTargetClusterEntity(restResponseData,
                ClusterRequestResult.SUCCESS, null,
                errorCode.getDataConsistency().getErrorResponseDataConsistencyForRollback());
            targetClusterList.add(targetClusterEntity);

          }
        }
      }
      return targetClusterList;
    } finally {
      logger.methodEnd();
    }
  }

  private ErrorCode getErrorCodeFromResponse(RestResponseData restResponseData) throws MsfException {
    try {
      logger.methodStart(new String[] { "restResponseData" }, new Object[] { restResponseData });
      ErrorResponseBody body = JsonUtil.fromJson(restResponseData.getResponse().getResponseBody(),
          ErrorResponseBody.class, ErrorCode.UNDEFINED_ERROR);
      return ErrorCode.getEnumFromErrorCode(body.getErrorCode());
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseData searchRollbackResponseData(RestResponseData restResponseData,
      List<RestResponseData> rollbackResponseDataList) {
    try {
      logger.methodStart(new String[] { "restResponseData", "rollbackResponseDataList" },
          new Object[] { restResponseData, rollbackResponseDataList });
      RestResponseData rollbackResponseData = null;

      if (rollbackResponseDataList != null) {
        for (RestResponseData rollbackResponseDataTmp : rollbackResponseDataList) {

          if (restResponseData.getRequest().getClusterId() == rollbackResponseDataTmp.getRequest().getClusterId()) {

            rollbackResponseData = rollbackResponseDataTmp;
            break;
          }
        }
      }
      return rollbackResponseData;
    } finally {
      logger.methodEnd();
    }
  }

  private ErrorResponseDataConsistency getDataConsistencyFromTargetClusterList(
      List<TargetClusterEntity> targetClusterList) {
    try {
      logger.methodStart(new String[] { "targetClusterList" }, new Object[] { targetClusterList });

      ErrorResponseDataConsistency dataConsistency = null;

      int rolledBackNum = 0;

      int updatedNum = 0;

      int inconsistentNum = 0;

      int unknownNum = 0;

      for (TargetClusterEntity targetClusterEntity : targetClusterList) {
        switch (targetClusterEntity.getDataConsistencyEnum()) {
          case ROLLED_BACK:
            rolledBackNum++;
            break;
          case UPDATED:
            updatedNum++;
            break;
          case NOT_CONSISTENT:
            inconsistentNum++;
            break;
          case UNKNOWN:
          default:
            unknownNum++;
            break;
        }
      }
      logger.debug("rolled back : {0}, updated : {1}, not consistent : {2}, unknown : {3}", rolledBackNum, updatedNum,
          inconsistentNum, unknownNum);

      if (rolledBackNum == targetClusterList.size()) {

        dataConsistency = ErrorResponseDataConsistency.ROLLED_BACK;
      } else if (updatedNum == targetClusterList.size()) {

        dataConsistency = ErrorResponseDataConsistency.UPDATED;
      } else if (unknownNum >= 1) {

        dataConsistency = ErrorResponseDataConsistency.UNKNOWN;
      } else {

        dataConsistency = ErrorResponseDataConsistency.NOT_CONSISTENT;
      }
      logger.debug("data consistency : {0}", dataConsistency);
      return dataConsistency;

    } finally {
      logger.methodEnd();
    }
  }

  private void setFailedStatus(ErrorResponse errorResponse, List<RestResponseData> responseDataList,
      List<RestResponseData> rollbackResponseDataList) {
    try {
      logger.methodStart(new String[] { "errorResponse", "responseDataList", "rollbackResponseDataList" },
          new Object[] { errorResponse, responseDataList, rollbackResponseDataList });

      for (RestResponseData restResponseData : responseDataList) {
        RestResponseData rollbackResponseData = searchRollbackResponseData(restResponseData, rollbackResponseDataList);
        if (rollbackResponseData != null) {

          if (rollbackResponseData.getResponse().isFailedRegistRecord()) {
            logger.debug("found rollback response that failed to regist record.");
            errorResponse.setFailedRegistRecord(true);
          }

          if (!HttpStatus.isSuccess(rollbackResponseData.getResponse().getHttpStatusCode())) {
            errorResponse.setFailedRollback(true);
          }
        } else {

          if (restResponseData.getResponse().isFailedRegistRecord()) {
            logger.debug("found response that failed to regist record.");
            errorResponse.setFailedRegistRecord(true);
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private TargetClusterEntity createTargetClusterEntity(RestResponseData restResponseData,
      ClusterRequestResult clusterRequestResult, ErrorCode errorCode,
      ErrorResponseDataConsistency errorResponseDataConsistency) {
    try {
      logger.methodStart(
          new String[] { "restResponseData", "clusterRequestResult", "errorCode", "errorResponseDataConsistency" },
          new Object[] { restResponseData, clusterRequestResult, errorCode, errorResponseDataConsistency });
      String clusterId = String.valueOf(restResponseData.getRequest().getClusterId());
      String requestResult = clusterRequestResult.getMessage();
      String errorCodeStr = null;
      String errorMessage = null;
      String dataConsistency = errorResponseDataConsistency.getMessage();

      if (errorCode != null) {
        errorCodeStr = errorCode.getCode();
        errorMessage = errorCode.getMessage();
      }
      return new TargetClusterEntity(clusterId, requestResult, errorCodeStr, errorMessage, dataConsistency);
    } finally {
      logger.methodEnd();
    }
  }

  protected void setTimerTask(TimerTaskMaker timerTaskMaker) {
    this.timerTaskMaker = timerTaskMaker;
  }

  protected String getOperationId() {
    return this.operationId;
  }

  protected void checkRestResponseHttpStatusCode(int httpStatusCode, int expectHttpStatusCode, String errorCode,
      ErrorCode throwErrorCode) throws MsfException {
    try {
      logger.methodStart(new String[] { "httpStatusCode", "expectHttpStatusCode", "errorCode", "throwErrorCode" },
          new Object[] { httpStatusCode, expectHttpStatusCode, errorCode, throwErrorCode });

      if (httpStatusCode != expectHttpStatusCode) {

        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}", httpStatusCode, errorCode);
        logger.error(errorMsg);
        throw new MsfException(throwErrorCode, errorMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

}
