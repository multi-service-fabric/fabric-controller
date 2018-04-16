
package msf.mfc.core.operation.scenario;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.data.MfcAsyncRequest;
import msf.mfc.common.data.MfcAsyncRequestsForLower;
import msf.mfc.common.data.MfcAsyncRequestsForLowerRollback;
import msf.mfc.db.dao.common.MfcAsyncRequestsDao;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.RollbackResult;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.scenario.data.OperationReadDetailListResponseBody;
import msf.mfcfc.core.operation.scenario.data.OperationReadListResponseBody;
import msf.mfcfc.core.operation.scenario.data.OperationRequest;
import msf.mfcfc.core.operation.scenario.data.entity.OperationDetailEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationRequestDetailEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationResponseDetailEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationRollbackDetailEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationTargetClusterDetailEntity;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;

/**
 * Implementation class for operation list acquisition.
 *
 * @author NTT
 *
 */
public class MfcOperationReadListScenario extends MfcAbstractOperationScenarioBase<OperationRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcOperationReadListScenario.class);

  /**
   * Constructor.
   *
   * @param operationType
   *          Operation type
   * @param systemIfType
   *          System interface type
   */
  public MfcOperationReadListScenario(OperationType operationType, SystemInterfaceType systemIfType) {

    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.info("[ope_id={0}]:Start MfcOperationReadListScenario.", this.getOperationId());
      logger.methodStart();

      logger.methodStart();
      sessionWrapper.openSession();
      List<MfcAsyncRequest> asyncRequestList = getAsyncRequestListFromDb(sessionWrapper);

      return createResponse(asyncRequestList, this.request.getFormatEnum());
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
      logger.info("[ope_id={0}]:End MfcOperationReadListScenario.", this.getOperationId());
    }
  }

  @Override
  protected void checkParameter(OperationRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      if (request.getFormat() != null) {
        this.checkFormatOption(request.getFormat());
      }

      this.request = request;
    } finally {
      logger.methodEnd();
    }
  }

  private List<MfcAsyncRequest> getAsyncRequestListFromDb(SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper" }, new Object[] { sessionWrapper });

      MfcAsyncRequestsDao asyncRequestDao = new MfcAsyncRequestsDao();
      List<MfcAsyncRequest> ret = asyncRequestDao.readList(sessionWrapper);
      logger.debug("ret={0}", ret);
      return ret;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(List<MfcAsyncRequest> asyncRequestList, RestFormatOption formatOption)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "asyncRequestList", "formatOption" },
          new Object[] { asyncRequestList, formatOption });

      if (RestFormatOption.DETAIL_LIST.equals(formatOption)) {
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200,
            createOperationDetailReadResponseBody(asyncRequestList));
        return restResponse;
      } else {
        RestResponseBase restResponse = new RestResponseBase(HttpStatus.OK_200,
            createOperationReadResponseBody(asyncRequestList));

        logger.debug("response={0}", restResponse);
        return restResponse;
      }
    } finally {
      logger.methodEnd();
    }
  }

  private OperationReadListResponseBody createOperationReadResponseBody(List<MfcAsyncRequest> asyncRequestList) {
    try {
      logger.methodStart(new String[] { "asyncRequestList" }, new Object[] { asyncRequestList });

      List<String> asyncRequestIdList = new ArrayList<>();
      for (MfcAsyncRequest asyncRequest : asyncRequestList) {
        asyncRequestIdList.add(asyncRequest.getOperationId());
      }
      OperationReadListResponseBody responseBody = new OperationReadListResponseBody();
      responseBody.setOperationIdList(asyncRequestIdList);

      logger.debug("response={0}", responseBody);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private OperationReadDetailListResponseBody createOperationDetailReadResponseBody(
      List<MfcAsyncRequest> asyncRequestList) throws MsfException {
    try {
      logger.methodStart(new String[] { "asyncRequestList" }, new Object[] { asyncRequestList });

      List<OperationDetailEntity> asyncRequestEntityList = new ArrayList<>();

      for (MfcAsyncRequest asyncRequest : asyncRequestList) {

        String subStatus = getSubStatus(asyncRequest);
        OperationDetailEntity asyncRequestEntity = createOperationDetailEntity(asyncRequest.getCommonEntity());
        if (subStatus != null) {
          asyncRequestEntity.setSubStatus(subStatus);
        }
        asyncRequestEntityList.add(asyncRequestEntity);
        asyncRequestEntity.setTargetClusterList(createResponseTargetClusterEntityList(asyncRequest));
        asyncRequestEntity.setRollbacks(createResponseRollbackEntity(asyncRequest));
      }
      OperationReadDetailListResponseBody responseBody = new OperationReadDetailListResponseBody();
      responseBody.setOperationList(asyncRequestEntityList);

      logger.debug("response={0}", responseBody);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private List<OperationTargetClusterDetailEntity> createResponseTargetClusterEntityList(MfcAsyncRequest asyncRequest)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "asyncRequest" }, new Object[] { asyncRequest });
      List<OperationTargetClusterDetailEntity> entityList = new ArrayList<>();
      if (asyncRequest.getAsyncRequestsForLowers() != null) {
        for (MfcAsyncRequestsForLower lower : asyncRequest.getAsyncRequestsForLowers()) {
          OperationRequestDetailEntity requestEntity = new OperationRequestDetailEntity();
          requestEntity.setUri(lower.getRequestUri());
          requestEntity.setMethod(lower.getRequestMethod());
          OperationResponseDetailEntity responseEntity = new OperationResponseDetailEntity();
          responseEntity.setStatusCode(lower.getResponseStatusCode());
          OperationTargetClusterDetailEntity entity = new OperationTargetClusterDetailEntity();
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

  private OperationRollbackDetailEntity createResponseRollbackEntity(MfcAsyncRequest asyncRequest) throws MsfException {
    try {
      logger.methodStart(new String[] { "asyncRequest" }, new Object[] { asyncRequest });
      boolean isRollbackExecuted = false;
      OperationRollbackDetailEntity rollbackEntity = null;
      List<OperationTargetClusterDetailEntity> targetClusterEntityList = null;
      if (asyncRequest.getAsyncRequestsForLowers() != null) {
        for (MfcAsyncRequestsForLower lower : asyncRequest.getAsyncRequestsForLowers()) {
          if (lower.getAsyncRequestsForLowerRollback() != null) {
            MfcAsyncRequestsForLowerRollback rollback = lower.getAsyncRequestsForLowerRollback();

            if (!isRollbackExecuted) {
              isRollbackExecuted = true;
              rollbackEntity = new OperationRollbackDetailEntity();
              targetClusterEntityList = new ArrayList<>();
              rollbackEntity.setTargetClusterList(targetClusterEntityList);
              rollbackEntity.setResult(RollbackResult.SUCCESS.getMessage());
              rollbackEntity.setOccurredTime(writeDateFormat.format(rollback.getOccurredTime()));
            }
            OperationRequestDetailEntity requestEntity = new OperationRequestDetailEntity();
            requestEntity.setUri(rollback.getRequestUri());
            requestEntity.setMethod(rollback.getRequestMethod());
            OperationResponseDetailEntity responseEntity = new OperationResponseDetailEntity();
            responseEntity.setStatusCode(rollback.getResponseStatusCode());
            OperationTargetClusterDetailEntity entity = new OperationTargetClusterDetailEntity();
            entity.setClusterId(String.valueOf(rollback.getId().getClusterId()));
            entity.setRequest(requestEntity);
            entity.setResponse(responseEntity);
            if (!HttpStatus.isSuccess(responseEntity.getStatusCode())) {
              rollbackEntity.setResult(RollbackResult.FAILED.getMessage());
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
