
package msf.fc.core.operation.scenario;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcAsyncRequest;
import msf.fc.db.dao.common.FcAsyncRequestsDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.operation.scenario.data.OperationReadResponseBody;
import msf.mfcfc.core.operation.scenario.data.OperationRequest;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;

/**
 * Implementation class for operation details acquisition.
 *
 * @author NTT
 *
 */
public class FcOperationReadScenario extends FcAbstractOperationScenarioBase<OperationRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcOperationReadScenario.class);

  /**
   * Constructor.
   *
   * @param operationType
   *          Operation type
   * @param systemIfType
   *          System interface type
   */
  public FcOperationReadScenario(OperationType operationType, SystemInterfaceType systemIfType) {

    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.info("[ope_id={0}]:Start FcOperationReadScenario.", this.getOperationId());

      logger.methodStart();
      sessionWrapper.openSession();
      FcAsyncRequest asyncRequest = getAsyncRequestFromDb(sessionWrapper, this.request.getOperationId());

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
      logger.info("[ope_id={0}]:End FcOperationReadScenario.", this.getOperationId());
    }
  }

  @Override
  protected void checkParameter(OperationRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotNullAndLength(request.getOperationId());
      logger.debug("Operation id=" + operationId);

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private FcAsyncRequest getAsyncRequestFromDb(SessionWrapper sessionWrapper, String operationId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "operationId" },
          new Object[] { sessionWrapper, operationId });

      FcAsyncRequestsDao asyncRequestsDao = new FcAsyncRequestsDao();
      FcAsyncRequest ret = asyncRequestsDao.read(sessionWrapper, operationId);
      logger.debug("result={1}", ret);
      return ret;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(FcAsyncRequest asyncRequest) throws MsfException {
    try {
      if (logger.isDebugEnabled()) {
        logger.methodStart(new String[] { "asyncRequest" }, new Object[] { asyncRequest });
      }

      OperationReadResponseBody responseBody = createOperationReadResponseBody(asyncRequest.getCommonEntity());
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);

      logger.debug("response={0}", restResponse);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

}
