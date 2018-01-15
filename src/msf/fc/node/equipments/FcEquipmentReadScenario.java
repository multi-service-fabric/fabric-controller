
package msf.fc.node.equipments;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcEquipment;
import msf.fc.db.dao.clusters.FcEquipmentDao;
import msf.fc.rest.ec.node.equipment.data.EquipmentReadEcResponseBody;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentEcEntity;
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
import msf.mfcfc.node.equipments.data.EquipmentReadResponseBody;
import msf.mfcfc.node.equipments.data.EquipmentRequest;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for model information acquisition.
 *
 * @author NTT
 *
 */
public class FcEquipmentReadScenario extends FcAbstractEquipmentScenarioBase<EquipmentRequest> {

  private EquipmentRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcEquipmentReadScenario.class);

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
  public FcEquipmentReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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

      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();
        FcEquipmentDao fcEquipmentDao = new FcEquipmentDao();

        FcEquipment fcEquipment = getEquipment(sessionWrapper, fcEquipmentDao,
            Integer.parseInt(request.getEquipmentTypeId()));

        EquipmentReadEcResponseBody equipmentEcResponseBody = sendEquipmentRead();

        responseBase = responseEquipmentReadData(fcEquipment, equipmentEcResponseBody);

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private EquipmentReadEcResponseBody sendEquipmentRead() throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.EQUIPMENT_READ.getHttpMethod(),
          EcRequestUri.EQUIPMENT_READ.getUri(request.getEquipmentTypeId()), null, ecControlIpAddress, ecControlPort);

      EquipmentReadEcResponseBody equipmentReadEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          EquipmentReadEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          equipmentReadEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return equipmentReadEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseEquipmentReadData(FcEquipment equipment,
      EquipmentReadEcResponseBody equipmentEcResponseBody) throws MsfException {
    try {
      logger.methodStart();
      EquipmentEcEntity equipmentEcData = equipmentEcResponseBody.getEquipment();

      EquipmentReadResponseBody body = new EquipmentReadResponseBody();
      body.setEquipmentType(getEquipmentType(equipment, equipmentEcData));
      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }

}
