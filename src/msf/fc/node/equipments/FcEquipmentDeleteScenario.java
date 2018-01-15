
package msf.fc.node.equipments;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcEquipment;
import msf.fc.db.dao.clusters.FcEquipmentDao;
import msf.fc.node.FcNodeManager;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.equipments.data.EquipmentRequest;
import msf.mfcfc.rest.common.ErrorInternalResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for model information deletion.
 *
 * @author NTT
 *
 */
public class FcEquipmentDeleteScenario extends FcAbstractEquipmentScenarioBase<EquipmentRequest> {

  private EquipmentRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcEquipmentDeleteScenario.class);

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public FcEquipmentDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(EquipmentRequest request) throws MsfException {
    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getEquipmentTypeId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);

      this.request = request;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      logger.performance("start wait to equipment increasing process.");
      synchronized (FcNodeManager.getInstance().getFcEquipmentCreateLockObject()) {
        logger.performance("end wait to equipment increasing process.");
        logger.performance("start wait to equipment decreasing process.");
        synchronized (FcNodeManager.getInstance().getFcEquipmentDeleteLockObject()) {
          logger.performance("end wait to equipment decreasing process.");

          RestResponseBase responseBase = null;
          SessionWrapper sessionWrapper = new SessionWrapper();
          try {
            sessionWrapper.openSession();
            sessionWrapper.beginTransaction();
            FcEquipmentDao fcEquipmentDao = new FcEquipmentDao();

            FcEquipment fcEquipment = getEquipment(sessionWrapper, fcEquipmentDao,
                Integer.parseInt(request.getEquipmentTypeId()));

            checkForEquipmentRelation(fcEquipment);

            fcEquipmentDao.delete(sessionWrapper, fcEquipment.getEquipmentTypeId());

            sendEquipmentDelete();

            responseBase = responseEquipmentDeleteData();

            sessionWrapper.commit();
          } catch (MsfException msfException) {
            logger.error(msfException.getMessage(), msfException);
            sessionWrapper.rollback();
            throw msfException;
          } finally {
            sessionWrapper.closeSession();
          }

          return responseBase;
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkForEquipmentRelation(FcEquipment fcEquipment) throws MsfException {
    try {
      logger.methodStart(new String[] { "equipment" }, new Object[] { fcEquipment });

      if (CollectionUtils.isNotEmpty(fcEquipment.getNodes())) {

        throw new MsfException(ErrorCode.DELETE_INFORMATION_ERROR, "nodes exist related to the equipment.");
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void sendEquipmentDelete() throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.EQUIPMENT_DELETE.getHttpMethod(),
          EcRequestUri.EQUIPMENT_DELETE.getUri(request.getEquipmentTypeId()), null, ecControlIpAddress, ecControlPort);

      String errorCode = null;
      if (StringUtils.isNotEmpty(restResponseBase.getResponseBody())) {
        ErrorInternalResponseBody equipmentDeleteEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            ErrorInternalResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        errorCode = equipmentDeleteEcResponseBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.NO_CONTENT_204, errorCode,
          ErrorCode.EC_CONTROL_ERROR);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseEquipmentDeleteData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

}
