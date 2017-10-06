package msf.fc.core.operation.scenario;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.AsyncRequest;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.operation.scenario.data.OperationReadResponseBody;
import msf.fc.core.operation.scenario.data.OperationRequest;
import msf.fc.core.operation.scenario.data.entity.RequestDetailEntity;
import msf.fc.core.operation.scenario.data.entity.ResponseDetailEntity;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.common.AsyncRequestsDao;

public class OperationReadScenario extends AbstractOperationScenarioBase<OperationRequest> {
  private static final MsfLogger logger = MsfLogger.getInstance(OperationReadScenario.class);

  public OperationReadScenario(OperationType operationType, SystemInterfaceType systemIfType) {
    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    logger.info("[ope_id={0}]:Start OperationReadScenario.", this.getOperationId());

    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();
      AsyncRequest asyncRequest = getAsyncRequestFromDb(sessionWrapper, this.request.getOperationId());

      if (asyncRequest == null) {
        String message = "Request operation not found.(operation id={0})";
        message = MessageFormat.format(message, this.request.getOperationId());
        String opeId = "[ope_id={0}]:";
        opeId = MessageFormat.format(opeId, this.getOperationId());

        logger.error(opeId + message);
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, message);
      }

      RestResponseBase response = createResponse(asyncRequest);
      logger.debug("response={0}", response);
      return response;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
      logger.info("[ope_id={0}]:End OperationReadScenario.", this.getOperationId());
    }
  }

  @Override
  protected void checkParameter(OperationRequest request) throws MsfException {
    try {
      if (logger.isDebugEnabled()) {
        logger.methodStart(new String[] { "request" }, new Object[] { request });
      }

      String operationId = request.getOperationId();
      if (operationId == null) {
        String message = "Request operation id is null.";
        logger.error("[ope_id={0}]:" + message, this.getOperationId());

        throw new MsfException(ErrorCode.PARAMETER_FORMAT_ERROR, message);
      }
      logger.debug("Operation id=" + operationId);



      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private AsyncRequest getAsyncRequestFromDb(SessionWrapper sessionWrapper, String operationId) throws MsfException {
    try {
      if (logger.isDebugEnabled()) {
        logger.methodStart(new String[] { "sessionWrapper", "operationId" },
            new Object[] { sessionWrapper, operationId });
      }

      AsyncRequestsDao asyncRequestsDao = new AsyncRequestsDao();
      AsyncRequest ret = asyncRequestsDao.read(sessionWrapper, operationId);
      logger.debug("result={1}", ret);
      return ret;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(AsyncRequest asyncRequest) throws MsfException {
    try {
      if (logger.isDebugEnabled()) {
        logger.methodStart(new String[] { "asyncRequest" }, new Object[] { asyncRequest });
      }

      OperationReadResponseBody responseBody = createOperationReadResponseBody(asyncRequest);
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);

      logger.debug("response={0}", restResponse);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  private OperationReadResponseBody createOperationReadResponseBody(AsyncRequest asyncRequest) throws MsfException {
    try {
      if (logger.isDebugEnabled()) {
        logger.methodStart(new String[] { "asyncRequest" }, new Object[] { asyncRequest });
      }
      OperationReadResponseBody operationReadResponseBody = new OperationReadResponseBody();

      operationReadResponseBody.setOperationId(asyncRequest.getOperationId());
      operationReadResponseBody.setOccurredTime(writeDateFormat.format(asyncRequest.getOccurredTime()));
      operationReadResponseBody.setLastUpdateTime(writeDateFormat.format(asyncRequest.getLastUpdateTime()));
      operationReadResponseBody.setStatusEnum(asyncRequest.getStatusEnum());
      operationReadResponseBody.setSubStatus(asyncRequest.getSubStatus());

      RequestDetailEntity requestEntity = new RequestDetailEntity();
      requestEntity.setRequestUri(asyncRequest.getRequestUri());
      requestEntity.setRequestMethod(asyncRequest.getRequestMethod());
      requestEntity.setRequestBody(asyncRequest.getRequestBody());

      operationReadResponseBody.setRequest(requestEntity);

      switch (asyncRequest.getStatusEnum()) {
        case FAILED:

          if (asyncRequest.getResponseStatusCode() == null) {
            String message = "Async response is mandatory when Status is completed or faild.(StatusCode={0})";
            message = MessageFormat.format(message, asyncRequest.getResponseStatusCode());

            logger.debug(message);
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, message);
          }

          ResponseDetailEntity responseEntity = new ResponseDetailEntity();
          responseEntity.setResponseStatusCode(asyncRequest.getResponseStatusCode());

          if (asyncRequest.getResponseBody() == null) {
            responseEntity.setResponseBody("");
            logger.debug("ResponseBody was changed to empty-string from null.");
          } else {
            responseEntity.setResponseBody(asyncRequest.getResponseBody());
          }

          operationReadResponseBody.setResponseBody(responseEntity);

          break;
        default:
          logger.debug("Status={0}", asyncRequest.getStatusEnum());
          break;
      }

      logger.debug("result={0}", operationReadResponseBody);
      return operationReadResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

}
