package msf.fc.node.equipments;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.Equipment;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EquipmentDao;
import msf.fc.node.equipments.data.EquipmentReadResponseBody;
import msf.fc.node.equipments.data.EquipmentRequest;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.node.equipment.data.EquipmentReadEcResponseBody;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentEcEntity;

public class EquipmentReadScenario extends AbstractEquipmentScenarioBase<EquipmentRequest> {

  private EquipmentRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(EquipmentCreateScenario.class);

  public EquipmentReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
        EquipmentDao equipmentDao = new EquipmentDao();
        Equipment equipment = getEquipment(sessionWrapper, equipmentDao, request.getClusterId(),
            Integer.parseInt(request.getEquipmentTypeId()));

        EquipmentReadEcResponseBody equipmentEcResponseBody = sendEquipmentRead();

        responseBase = responseEquipmentReadData(equipment, equipmentEcResponseBody);

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

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.EQUIPMENT_READ.getHttpMethod(),
          EcRequestUri.EQUIPMENT_READ.getUri(request.getEquipmentTypeId()), null);

      EquipmentReadEcResponseBody equipmentReadEcResponseBody = new EquipmentReadEcResponseBody();

      equipmentReadEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          EquipmentReadEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      if (restResponseBase.getHttpStatusCode() != HttpStatus.OK_200) {
        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
            restResponseBase.getHttpStatusCode(), equipmentReadEcResponseBody.getErrorCode());
        logger.error(errorMsg);
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
      }

      return equipmentReadEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseEquipmentReadData(Equipment equipment,
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
