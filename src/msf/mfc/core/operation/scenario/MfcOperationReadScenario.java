
package msf.mfc.core.operation.scenario;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.data.MfcAsyncRequest;
import msf.mfc.common.data.MfcAsyncRequestsForLower;
import msf.mfc.common.data.MfcAsyncRequestsForLowerRollback;
import msf.mfc.db.dao.common.MfcAsyncRequestsDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RollbackResult;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.operation.scenario.data.OperationReadResponseBody;
import msf.mfcfc.core.operation.scenario.data.OperationRequest;
import msf.mfcfc.core.operation.scenario.data.entity.OperationRequestEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationResponseEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationRollbackEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationTargetClusterEntity;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;

/**
 * Implementation class for operation details acquisition.
 *
 * @author NTT
 *
 */
public class MfcOperationReadScenario extends MfcAbstractOperationScenarioBase<OperationRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcOperationReadScenario.class);

  /**
   * Constructor.
   *
   * @param operationType
   *          Operation type
   * @param systemIfType
   *          System interface type
   */
  public MfcOperationReadScenario(OperationType operationType, SystemInterfaceType systemIfType) {

    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.info("[ope_id={0}]:Start MfcOperationReadScenario.", this.getOperationId());

      logger.methodStart();
      sessionWrapper.openSession();
      MfcAsyncRequest asyncRequest = getAsyncRequestFromDb(sessionWrapper, this.request.getOperationId());

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
      logger.info("[ope_id={0}]:End MfcOperationReadScenario.", this.getOperationId());
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

  private MfcAsyncRequest getAsyncRequestFromDb(SessionWrapper sessionWrapper, String operationId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "operationId" },
          new Object[] { sessionWrapper, operationId });

      MfcAsyncRequestsDao asyncRequestsDao = new MfcAsyncRequestsDao();
      MfcAsyncRequest ret = asyncRequestsDao.read(sessionWrapper, operationId);
      logger.debug("result={1}", ret);
      return ret;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(MfcAsyncRequest asyncRequest) throws MsfException {
    try {
      logger.methodStart(new String[] { "asyncRequest" }, new Object[] { asyncRequest });

      OperationReadResponseBody responseBody = createOperationReadResponseBody(asyncRequest.getCommonEntity());

      String subStatus = getSubStatus(asyncRequest);
      if (subStatus != null) {
        responseBody.setSubStatus(subStatus);
      }
      responseBody.setTargetClusterList(createResponseTargetClusterEntityList(asyncRequest));
      responseBody.setRollbacks(createResponseRollbackEntity(asyncRequest));
      RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200, responseBody);

      logger.debug("response={0}", restResponse);
      return restResponse;
    } finally {
      logger.methodEnd();
    }
  }

  private List<OperationTargetClusterEntity> createResponseTargetClusterEntityList(MfcAsyncRequest asyncRequest)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "asyncRequest" }, new Object[] { asyncRequest });
      List<OperationTargetClusterEntity> entityList = new ArrayList<>();
      if (asyncRequest.getAsyncRequestsForLowers() != null) {
        for (MfcAsyncRequestsForLower lower : asyncRequest.getAsyncRequestsForLowers()) {
          OperationRequestEntity requestEntity = new OperationRequestEntity(lower.getRequestUri(),
              lower.getRequestMethod(), lower.getRequestBody());
          OperationResponseEntity responseEntity = new OperationResponseEntity(lower.getResponseStatusCode(),
              lower.getResponseBody());
          OperationTargetClusterEntity entity = new OperationTargetClusterEntity();
          entity.setClusterId(String.valueOf(lower.getId().getClusterId()));
          entity.setRequest(requestEntity);
          entity.setResponse(responseEntity);
          entityList.add(entity);
        }
      }
      return entityList;
    } finally {
      logger.methodEnd();
    }
  }

  private OperationRollbackEntity createResponseRollbackEntity(MfcAsyncRequest asyncRequest) throws MsfException {
    try {
      logger.methodStart(new String[] { "asyncRequest" }, new Object[] { asyncRequest });
      boolean isRollbackExecuted = false;
      OperationRollbackEntity rollbackEntity = null;
      List<OperationTargetClusterEntity> targetClusterEntityList = null;
      if (asyncRequest.getAsyncRequestsForLowers() != null) {
        for (MfcAsyncRequestsForLower lower : asyncRequest.getAsyncRequestsForLowers()) {
          if (lower.getAsyncRequestsForLowerRollback() != null) {
            MfcAsyncRequestsForLowerRollback rollback = lower.getAsyncRequestsForLowerRollback();

            if (!isRollbackExecuted) {
              isRollbackExecuted = true;
              rollbackEntity = new OperationRollbackEntity();
              targetClusterEntityList = new ArrayList<>();
              rollbackEntity.setTargetClusterList(targetClusterEntityList);
              rollbackEntity.setResultEnum(RollbackResult.SUCCESS);
              rollbackEntity.setOccurredTime(writeDateFormat.format(rollback.getOccurredTime()));
            }
            OperationRequestEntity requestEntity = new OperationRequestEntity(rollback.getRequestUri(),
                rollback.getRequestMethod(), rollback.getRequestBody());
            OperationResponseEntity responseEntity = new OperationResponseEntity(rollback.getResponseStatusCode(),
                rollback.getResponseBody());
            OperationTargetClusterEntity entity = new OperationTargetClusterEntity();
            entity.setClusterId(String.valueOf(rollback.getId().getClusterId()));
            entity.setRequest(requestEntity);
            entity.setResponse(responseEntity);
            if (!HttpStatus.isSuccess(responseEntity.getStatusCode())) {
              rollbackEntity.setResultEnum(RollbackResult.FAILED);
            }
            targetClusterEntityList.add(entity);
          }
        }
      }
      return rollbackEntity;
    } finally {
      logger.methodEnd();
    }
  }

}
