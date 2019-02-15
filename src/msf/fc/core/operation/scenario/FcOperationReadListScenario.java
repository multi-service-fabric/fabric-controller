
package msf.fc.core.operation.scenario;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcAsyncRequest;
import msf.fc.db.dao.common.FcAsyncRequestsDao;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.scenario.data.OperationReadDetailListResponseBody;
import msf.mfcfc.core.operation.scenario.data.OperationReadListResponseBody;
import msf.mfcfc.core.operation.scenario.data.OperationRequest;
import msf.mfcfc.core.operation.scenario.data.entity.OperationDetailEntity;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;

/**
 * Implementation class for the operation list acquisition.
 *
 * @author NTT
 *
 */
public class FcOperationReadListScenario extends FcAbstractOperationScenarioBase<OperationRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcOperationReadListScenario.class);

  /**
   * Constructor.
   *
   * @param operationType
   *          Operation type
   * @param systemIfType
   *          System interface type
   */
  public FcOperationReadListScenario(OperationType operationType, SystemInterfaceType systemIfType) {

    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.info("[ope_id={0}]:Start FcOperationReadListScenario.", this.getOperationId());
      logger.methodStart();
      sessionWrapper.openSession();
      List<FcAsyncRequest> asyncRequestList = getAsyncRequestListFromDb(sessionWrapper);

      return createResponse(asyncRequestList, this.request.getFormatEnum());
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
      logger.info("[ope_id={0}]:End FcOperationReadListScenario.", this.getOperationId());
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

  private List<FcAsyncRequest> getAsyncRequestListFromDb(SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper" }, new Object[] { sessionWrapper });

      FcAsyncRequestsDao asyncRequestDao = new FcAsyncRequestsDao();
      List<FcAsyncRequest> ret = asyncRequestDao.readList(sessionWrapper);
      logger.debug("ret={0}", ret);
      return ret;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase createResponse(List<FcAsyncRequest> asyncRequestList, RestFormatOption formatOption)
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

  private OperationReadListResponseBody createOperationReadResponseBody(List<FcAsyncRequest> asyncRequestList) {
    try {
      logger.methodStart(new String[] { "asyncRequestList" }, new Object[] { asyncRequestList });

      List<String> asyncRequestIdList = new ArrayList<>();
      for (FcAsyncRequest asyncRequest : asyncRequestList) {
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
      List<FcAsyncRequest> asyncRequestList) throws MsfException {
    try {
      logger.methodStart(new String[] { "asyncRequestList" }, new Object[] { asyncRequestList });

      List<OperationDetailEntity> asyncRequestEntityList = new ArrayList<>();

      for (FcAsyncRequest asyncRequest : asyncRequestList) {
        OperationDetailEntity asyncRequestEntity = createOperationDetailEntity(asyncRequest.getCommonEntity());
        asyncRequestEntityList.add(asyncRequestEntity);
      }
      OperationReadDetailListResponseBody responseBody = new OperationReadDetailListResponseBody();
      responseBody.setOperationList(asyncRequestEntityList);

      logger.debug("response={0}", responseBody);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }
}
