package msf.fc.core.operation.scenario;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.AsyncRequest;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.operation.scenario.data.OperationReadDetailListResponseBody;
import msf.fc.core.operation.scenario.data.OperationReadListResponseBody;
import msf.fc.core.operation.scenario.data.OperationRequest;
import msf.fc.core.operation.scenario.data.entity.AsyncRequestEntity;
import msf.fc.core.operation.scenario.data.entity.RequestEntity;
import msf.fc.core.operation.scenario.data.entity.ResponseEntity;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.common.AsyncRequestsDao;

public class OperationReadListScenario extends AbstractOperationScenarioBase<OperationRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(OperationReadListScenario.class);

  public OperationReadListScenario(OperationType operationType, SystemInterfaceType systemIfType) {
    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    logger.info("[ope_id={0}]:Start OperationReadListScenario.", this.getOperationId());

    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();
      List<AsyncRequest> asyncRequestList = getAsyncRequestListFromDb(sessionWrapper);

      return createResponse(asyncRequestList, this.request.getFormatEnum());
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
      logger.info("[ope_id={0}]:End OperationReadListScenario.", this.getOperationId());
    }
  }

  @Override
  protected void checkParameter(OperationRequest request) throws MsfException {
    try {
      if (logger.isDebugEnabled()) {
        logger.methodStart(new String[] { "request" }, new Object[] { request });
      }


      this.checkFormatOption(request.getFormat());


      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private List<AsyncRequest> getAsyncRequestListFromDb(SessionWrapper sessionWrapper) throws MsfException {
    try {
      if (logger.isDebugEnabled()) {
        logger.methodStart(new String[] { "sessionWrapper" }, new Object[] { sessionWrapper });
      }

      AsyncRequestsDao asyncRequestDao = new AsyncRequestsDao();
      List<AsyncRequest> ret = asyncRequestDao.readList(sessionWrapper);
      logger.debug("ret={0}", ret);
      return ret;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(List<AsyncRequest> asyncRequestList, RestFormatOption formatOption)
      throws MsfException {
    try {
      if (logger.isDebugEnabled()) {
        logger.methodStart(new String[] { "asyncRequestList", "formatOption" },
            new Object[] { asyncRequestList, formatOption });
      }

        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200,
            createOperationDetailReadResponseBody(asyncRequestList));
        return restResponse;
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200,
            createOperationReadResponseBody(asyncRequestList));

        logger.debug("response={0}", restResponse);
        return restResponse;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private OperationReadDetailListResponseBody createOperationDetailReadResponseBody(List<AsyncRequest> asyncRequestList)
      throws MsfException {
    try {
      if (logger.isDebugEnabled()) {
        logger.methodStart(new String[] { "asyncRequestList" }, new Object[] { asyncRequestList });
      }

      List<AsyncRequestEntity> asyncRequestEntityList = new ArrayList<>();

      for (AsyncRequest asyncRequest : asyncRequestList) {
        AsyncRequestEntity asyncRequestEntity = this.createAsyncRequestEntity(asyncRequest);
        asyncRequestEntityList.add(asyncRequestEntity);
      }
      OperationReadDetailListResponseBody responseBody = new OperationReadDetailListResponseBody();
      responseBody.setAsyncRequestList(asyncRequestEntityList);

      logger.debug("response={0}", responseBody);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private OperationReadListResponseBody createOperationReadResponseBody(List<AsyncRequest> asyncRequestList) {
    try {
      if (logger.isDebugEnabled()) {
        logger.methodStart(new String[] { "asyncRequestList" }, new Object[] { asyncRequestList });
      }

      List<String> asyncRequestIdList = new ArrayList<>();
      for (AsyncRequest asyncRequest : asyncRequestList) {
        asyncRequestIdList.add(asyncRequest.getOperationId());
      }
      OperationReadListResponseBody responseBody = new OperationReadListResponseBody();
      responseBody.setAsyncRequestIdList(asyncRequestIdList);

      logger.debug("response={0}", responseBody);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private AsyncRequestEntity createAsyncRequestEntity(AsyncRequest asyncRequest) throws MsfException {
    try {
      if (logger.isDebugEnabled()) {
        logger.methodStart(new String[] { "asyncRequest" }, new Object[] { asyncRequest });
      }
      AsyncRequestEntity asyncRequestEntity = new AsyncRequestEntity();

      asyncRequestEntity.setOperationId(asyncRequest.getOperationId());
      asyncRequestEntity.setOccurredTime(writeDateFormat.format(asyncRequest.getOccurredTime()));
      asyncRequestEntity.setLastUpdateTime(writeDateFormat.format(asyncRequest.getLastUpdateTime()));
      asyncRequestEntity.setStatusEnum(asyncRequest.getStatusEnum());
      asyncRequestEntity.setSubStatus(asyncRequest.getSubStatus());

      RequestEntity requestEntity = new RequestEntity();
      requestEntity.setRequestUri(asyncRequest.getRequestUri());
      requestEntity.setRequestMethod(asyncRequest.getRequestMethod());

      asyncRequestEntity.setRequest(requestEntity);

      switch (asyncRequest.getStatusEnum()) {
        case FAILED:

          if (asyncRequest.getResponseStatusCode() == null) {
            String message = "Async response is mandatory when Status is completed or faild.(StatusCode={0})";
            message = MessageFormat.format(message, asyncRequest.getResponseStatusCode());

            logger.debug(message);
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, message);
          }

          ResponseEntity responseEntity = new ResponseEntity();
          responseEntity.setResponseStatusCode(asyncRequest.getResponseStatusCode());
          asyncRequestEntity.setResponseBody(responseEntity);

          break;
        default:
          logger.debug("Status={0}", asyncRequest.getStatusEnum());
          break;
      }

      logger.debug("result={0}", asyncRequestEntity);
      return asyncRequestEntity;
    } finally {
      logger.methodEnd();
    }
  }

}
