package msf.fc.node.equipments;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.Equipment;
import msf.fc.common.data.SwCluster;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EquipmentDao;
import msf.fc.db.dao.clusters.SwClusterDao;
import msf.fc.node.equipments.data.EquipmentReadDetailListResponseBody;
import msf.fc.node.equipments.data.EquipmentReadListResponseBody;
import msf.fc.node.equipments.data.EquipmentRequest;
import msf.fc.node.equipments.data.entity.EquipmentTypeEntity;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.node.equipment.data.EquipmentReadListEcResponseBody;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentEcEntity;

public class EquipmentReadListScenario extends AbstractEquipmentScenarioBase<EquipmentRequest> {

  private EquipmentRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(EquipmentCreateScenario.class);

  public EquipmentReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(EquipmentRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      if (request.getFormat() != null) {
        ParameterCheckUtil.checkNotNull(request.getFormatEnum());
      }

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
        List<Equipment> equipments = equipmentDao.readList(sessionWrapper, request.getClusterId());

        if (equipments.isEmpty()) {
          checkSwCluster(sessionWrapper, request.getClusterId());
        }

        responseBase = responseEquipmentReadListData(equipments, request.getFormat());

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

  private RestResponseBase responseEquipmentReadListData(List<Equipment> equipments, String format)
      throws MsfException {
    try {
      logger.methodStart();
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        EquipmentReadDetailListResponseBody body = new EquipmentReadDetailListResponseBody();
        if (!equipments.isEmpty()) {
          EquipmentReadListEcResponseBody equipmentReadListEcResponseBody = sendEquipmentReadList();
          body.setEquipmentTypeList(getEquipmentTypes(equipments, equipmentReadListEcResponseBody));
        } else {
          body.setEquipmentTypeList(new ArrayList<>());
        }
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        EquipmentReadListResponseBody body = new EquipmentReadListResponseBody();
        body.setEquipmentTypeIdList(getEquipmentTypeIds(equipments));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private EquipmentReadListEcResponseBody sendEquipmentReadList() throws MsfException {
    try {
      logger.methodStart();
      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.EQUIPMENT_READ_LIST.getHttpMethod(),
          EcRequestUri.EQUIPMENT_READ_LIST.getUri(), null);

      EquipmentReadListEcResponseBody equipmentReadListEcResponseBody = new EquipmentReadListEcResponseBody();
      equipmentReadListEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          EquipmentReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      if (restResponseBase.getHttpStatusCode() != HttpStatus.OK_200) {
        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
            restResponseBase.getHttpStatusCode(), equipmentReadListEcResponseBody.getErrorCode());
        logger.error(errorMsg);
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
      }

      return equipmentReadListEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private List<EquipmentTypeEntity> getEquipmentTypes(List<Equipment> equipments, EquipmentReadListEcResponseBody body)
      throws MsfException {
    try {
      logger.methodStart();
      List<EquipmentTypeEntity> equipmentTypes = new ArrayList<>();
      List<EquipmentEcEntity> equipmentEcList = body.getEquipmentList();
      boolean isExist;
      for (Equipment equipment : equipments) {
        isExist = false;
        String equipmentTypeId = String.valueOf(equipment.getId().getEquipmentTypeId());
        for (EquipmentEcEntity equipmentEc : equipmentEcList) {
          if (equipmentTypeId.equals(equipmentEc.getEquipmentTypeId())) {
            equipmentTypes.add(getEquipmentType(equipment, equipmentEc));
            isExist = true;
            break;
          }
        }
        if (!isExist) {
          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is only the data in the FC system.");
        }
      }
      if (equipmentEcList.size() != equipmentTypes.size()) {
        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is only the data in the EC system.");
      }
      return equipmentTypes;
    } finally {
      logger.methodEnd();
    }
  }

  private List<String> getEquipmentTypeIds(List<Equipment> equipments) {
    try {
      logger.methodStart();
      List<String> equipmentTypeIds = new ArrayList<>();
      for (Equipment equipment : equipments) {
        equipmentTypeIds.add(String.valueOf(equipment.getId().getEquipmentTypeId()));
      }
      return equipmentTypeIds;
    } finally {
      logger.methodEnd();
    }
  }

  private void checkSwCluster(SessionWrapper sessionWrapper, String swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "swClusterId" },
          new Object[] { sessionWrapper, swClusterId });
      SwClusterDao swClusterDao = new SwClusterDao();
      SwCluster swCluster = swClusterDao.read(sessionWrapper, swClusterId);
      if (swCluster == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = swCluster");
      }
    } finally {
      logger.methodEnd();
    }
  }

}
