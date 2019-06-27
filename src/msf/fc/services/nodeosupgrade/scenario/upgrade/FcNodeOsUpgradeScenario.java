
package msf.fc.services.nodeosupgrade.scenario.upgrade;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.data.FcAsyncRequest;
import msf.fc.db.dao.common.FcAsyncRequestsDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.ReservationRequestType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeRequest;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeRequestBody;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeResponseBody;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.entity.NodeOsUpgradeListEntity;

/**
 * Implementation class for the node OS upgrade.
 *
 * @author NTT
 *
 */
public class FcNodeOsUpgradeScenario extends FcAbstractNodeOsUpgradeScenarioBase<NodeOsUpgradeRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeOsUpgradeScenario.class);

  private NodeOsUpgradeRequest request;
  private NodeOsUpgradeRequestBody requestBody;

  private boolean reStartFlag = false;

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type", "system interface type" and "operation id" as
   * arguments If "operation id" is set, it means a startup of already received
   * scenario in the controller startup process.
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   * @param operationId
   *          Operation ID
   *
   */
  public FcNodeOsUpgradeScenario(OperationType operationType, SystemInterfaceType systemInterfaceType,
      String operationId) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
    this.reservationRequestType = ReservationRequestType.RESERVATION;
    if (operationId != null) {
      reStartFlag = true;

      this.operationId = operationId;
    }
  }

  @Override
  protected void checkParameter(NodeOsUpgradeRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

      NodeOsUpgradeRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          NodeOsUpgradeRequestBody.class);
      requestBody.validate();

      boolean isOperatorCheck = false;
      for (NodeOsUpgradeListEntity nodeOsUpgradeListEntity : requestBody.getNodeList()) {
        if (nodeOsUpgradeListEntity.getOperatorCheck()) {

          isOperatorCheck = true;
          break;
        }
      }

      if (isOperatorCheck) {

        ParameterCheckUtil.checkNotNull(request.getNotificationAddress());
      }

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }

  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      RestResponseBase responseBase = null;
      if (!reStartFlag) {

        SessionWrapper sessionWrapper = new SessionWrapper();
        try {
          sessionWrapper.openSession();
          sessionWrapper.beginTransaction();

          FcAsyncRequestsDao fcAsyncRequestsDao = new FcAsyncRequestsDao();
          FcAsyncRequest fcAsyncRequest = fcAsyncRequestsDao.read(sessionWrapper, getOperationId());

          Date reservationTime = null;
          if (requestBody.getReservationTime() != null) {
            reservationTime = DateUtils.parseDate(requestBody.getReservationTime(),
                new String[] { ParameterCheckUtil.DATE_FORMAT });
          } else {
            reservationTime = new Date();

            requestBody.setReservationTime(ParameterCheckUtil.dateFormat.format(reservationTime));
          }
          fcAsyncRequest.setReservationTime(new Timestamp(reservationTime.getTime()));
          fcAsyncRequest.setLastUpdateTime(new Timestamp(System.currentTimeMillis()));
          fcAsyncRequestsDao.update(sessionWrapper, fcAsyncRequest);

          sessionWrapper.commit();
        } catch (MsfException msfException) {
          logger.error(msfException.getMessage(), msfException);
          sessionWrapper.rollback();
          throw msfException;
        } catch (ParseException parseException) {
          sessionWrapper.rollback();

          throw new MsfException(ErrorCode.UNDEFINED_ERROR,
              "Illegal parameter. reservationTime = " + requestBody.getReservationTime());
        } finally {
          sessionWrapper.closeSession();
        }
      }

      FcNodeOsUpgradeRunner fcNodeOsUpgradeRunner = new FcNodeOsUpgradeRunner(request, requestBody);
      execAsyncRunner(fcNodeOsUpgradeRunner);

      responseBase = responseNodeOsUpgradeData();
      return responseBase;
    } finally {
      logger.methodEnd();
    }

  }

  private RestResponseBase responseNodeOsUpgradeData() {
    try {
      logger.methodStart();
      NodeOsUpgradeResponseBody body = new NodeOsUpgradeResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }

}
