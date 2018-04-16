
package msf.mfcfc.core.operation.scenario;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.scenario.data.OperationReadResponseBody;
import msf.mfcfc.core.operation.scenario.data.OperationRequest;
import msf.mfcfc.core.operation.scenario.data.entity.OperationDetailEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationRequestDetailEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationRequestEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationResponseDetailEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationResponseEntity;
import msf.mfcfc.core.scenario.AbstractScenario;
import msf.mfcfc.core.scenario.RestRequestBase;

/**
 * Abstract class to implement the common process of operation-related
 * processing in system basic function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class AbstractOperationScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractOperationScenarioBase.class);

  protected OperationRequest request;

  protected final SimpleDateFormat writeDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

  protected void checkFormatOption(String format) throws MsfException {
    try {
      logger.methodStart();
      RestFormatOption formatEnum = RestFormatOption.getEnumFromMessage(format);
      if (formatEnum == null) {
        String logMsg = MessageFormat.format("Format is undefined.(format={0})", format);
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected OperationReadResponseBody createOperationReadResponseBody(AsyncRequest asyncRequest) throws MsfException {
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

      String requestBody = asyncRequest.getRequestBody() == null ? "" : asyncRequest.getRequestBody();
      OperationRequestEntity requestEntity = new OperationRequestEntity(asyncRequest.getRequestUri(),
          asyncRequest.getRequestMethod(), requestBody);

      operationReadResponseBody.setRequest(requestEntity);

      switch (asyncRequest.getStatusEnum()) {
        case COMPLETED:
        case FAILED:

          if (asyncRequest.getResponseStatusCode() == null) {
            String message = "Async response is mandatory when Status is completed or faild.(StatusCode={0})";
            message = MessageFormat.format(message, asyncRequest.getResponseStatusCode());

            logger.debug(message);
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, message);
          }

          OperationResponseEntity responseEntity = new OperationResponseEntity();
          responseEntity.setStatusCode(asyncRequest.getResponseStatusCode());

          if (asyncRequest.getResponseBody() == null) {
            responseEntity.setBody("");
            logger.debug("ResponseBody was changed to empty-string from null.");
          } else {
            responseEntity.setBody(asyncRequest.getResponseBody());
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

  protected OperationDetailEntity createOperationDetailEntity(AsyncRequest asyncRequest) throws MsfException {
    try {
      logger.methodStart(new String[] { "asyncRequest" }, new Object[] { asyncRequest });
      OperationDetailEntity operationDetailEntity = new OperationDetailEntity();

      operationDetailEntity.setOperationId(asyncRequest.getOperationId());
      operationDetailEntity.setOccurredTime(writeDateFormat.format(asyncRequest.getOccurredTime()));
      operationDetailEntity.setLastUpdateTime(writeDateFormat.format(asyncRequest.getLastUpdateTime()));
      operationDetailEntity.setStatusEnum(asyncRequest.getStatusEnum());

      operationDetailEntity.setSubStatus(asyncRequest.getSubStatus());

      OperationRequestDetailEntity requestEntity = new OperationRequestDetailEntity();
      requestEntity.setUri(asyncRequest.getRequestUri());
      requestEntity.setMethod(asyncRequest.getRequestMethod());

      operationDetailEntity.setRequest(requestEntity);

      switch (asyncRequest.getStatusEnum()) {
        case COMPLETED:
        case FAILED:

          if (asyncRequest.getResponseStatusCode() == null) {
            String message = "Async response is mandatory when Status is completed or faild.(StatusCode={0})";
            message = MessageFormat.format(message, asyncRequest.getResponseStatusCode());

            logger.debug(message);
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, message);
          }

          OperationResponseDetailEntity responseEntity = new OperationResponseDetailEntity();
          responseEntity.setStatusCode(asyncRequest.getResponseStatusCode());
          operationDetailEntity.setResponse(responseEntity);

          break;
        default:
          logger.debug("Status={0}", asyncRequest.getStatusEnum());
          break;
      }

      logger.debug("result={0}", operationDetailEntity);
      return operationDetailEntity;
    } finally {
      logger.methodEnd();
    }
  }
}
