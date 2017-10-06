package msf.fc.node.equipments;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.BootErrorMessage;
import msf.fc.common.data.BootErrorMessagePK;
import msf.fc.common.data.Equipment;
import msf.fc.common.data.EquipmentIf;
import msf.fc.common.data.EquipmentIfPK;
import msf.fc.common.data.EquipmentPK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EquipmentDao;
import msf.fc.node.NodeManager;
import msf.fc.node.equipments.data.EquipmentCreateRequestBody;
import msf.fc.node.equipments.data.EquipmentCreateResponseBody;
import msf.fc.node.equipments.data.EquipmentRequest;
import msf.fc.node.equipments.data.entity.PortEntity;
import msf.fc.node.equipments.data.entity.SlotEntity;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.node.equipment.data.EquipmentCreateEcRequestBody;
import msf.fc.rest.ec.node.equipment.data.EquipmentCreateEcResponseBody;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentEcEntity;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentIfEcEntity;
import msf.fc.rest.ec.node.equipment.data.entity.IfNameRuleEcEntity;

public class EquipmentCreateScenario extends AbstractEquipmentScenarioBase<EquipmentRequest> {

  private EquipmentRequest request;
  private EquipmentCreateRequestBody requestBody;

  private static final MsfLogger logger = MsfLogger.getInstance(EquipmentCreateScenario.class);

  public EquipmentCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(EquipmentRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      EquipmentCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          EquipmentCreateRequestBody.class);
      requestBody.validate();
      logger.debug("requestBody=" + request.getRequestBody());

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
      logger.performance("start wait to equipment increasing process.");
      synchronized (NodeManager.getInstance().getEquipmentCreateLockObject()) {
        logger.performance("end wait to equipment increasing process.");
        RestResponseBase responseBase = null;
        SessionWrapper sessionWrapper = new SessionWrapper();
        try {
          sessionWrapper.openSession();
          sessionWrapper.beginTransaction();
          EquipmentDao equipmentDao = new EquipmentDao();
          checkForDuplicate(sessionWrapper, equipmentDao, requestBody.getEquipmentType().getPlatform(),
              requestBody.getEquipmentType().getOs(), requestBody.getEquipmentType().getFirmware());

          Equipment equipment = setEquipmentData(sessionWrapper, equipmentDao);

          equipmentDao.create(sessionWrapper, equipment);

          sendEquipmentCreate(equipment);

          responseBase = responseEquipmentCreateData(equipment);

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
    } finally {
      logger.methodEnd();
    }
  }

  private void checkForDuplicate(SessionWrapper sessionWrapper, EquipmentDao equipmentDao, String platform,
      String osName, String firmware) throws MsfException {
    try {
      logger.methodStart();
      Equipment equipment = equipmentDao.read(sessionWrapper, platform, osName, firmware);
      if (equipment != null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST,
            "target resouece already exist. target = equipment");
      }
    } finally {
      logger.methodEnd();
    }
  }

  private Equipment setEquipmentData(SessionWrapper sessionWrapper, EquipmentDao equipmentDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "equipmentDao" },
          new Object[] { sessionWrapper, equipmentDao });
      Equipment equipment = new Equipment();
      EquipmentPK equipmentPk = new EquipmentPK();
      equipmentPk.setSwClusterId(request.getClusterId());
      equipmentPk.setEquipmentTypeId(getNextEquipmentTypeId(sessionWrapper, equipmentDao));
      equipment.setId(equipmentPk);
      equipment.setPlatformName(requestBody.getEquipmentType().getPlatform());
      equipment.setOsName(requestBody.getEquipmentType().getOs());
      equipment.setFirmwareVersion(requestBody.getEquipmentType().getFirmware());
      equipment.setL2vpnCapability(requestBody.getEquipmentType().getCapability().isL2Vpn());
      equipment.setL3vpnCapability(requestBody.getEquipmentType().getCapability().isL3Vpn());
      equipment.setConfigTemplate(requestBody.getEquipmentType().getDhcp().getConfigTemplate());
      equipment.setInitialConfig(requestBody.getEquipmentType().getDhcp().getInitialConfig());
      equipment.setBootCompleteMsg(requestBody.getEquipmentType().getBootCompleteMsg());
      equipment.setBootErrorMessages(setBootErrorMessageData(equipmentPk.getEquipmentTypeId()));
      equipment.setEquipmentIfs(setEquipmentIfData(equipmentPk.getEquipmentTypeId()));
      return equipment;
    } finally {
      logger.methodEnd();
    }
  }

  private List<BootErrorMessage> setBootErrorMessageData(Integer equipmentTypeId) {
    try {
      logger.methodStart(new String[] { "equipmentTypeId" }, new Object[] { equipmentTypeId });
      List<BootErrorMessage> bootErrorMessages = new ArrayList<>();
      List<String> bootErrorMsgs = requestBody.getEquipmentType().getBootErrorMsgList();
      if (bootErrorMsgs != null) {
        for (String bootErrorMsg : bootErrorMsgs) {
          BootErrorMessagePK bootErrorMessagePk = new BootErrorMessagePK();
          bootErrorMessagePk.setSwClusterId(request.getClusterId());
          bootErrorMessagePk.setEquipmentTypeId(equipmentTypeId);
          bootErrorMessagePk.setBootErrorMsg(bootErrorMsg);
          BootErrorMessage bootErrorMessage = new BootErrorMessage();
          bootErrorMessage.setId(bootErrorMessagePk);
          bootErrorMessages.add(bootErrorMessage);
        }
      }
      return bootErrorMessages;
    } finally {
      logger.methodEnd();
    }
  }

  private List<EquipmentIf> setEquipmentIfData(Integer equipmentTypeId) {
    try {
      logger.methodStart(new String[] { "equipmentTypeId" }, new Object[] { equipmentTypeId });
      List<EquipmentIf> equipmentIfs = new ArrayList<>();
      List<SlotEntity> slots = requestBody.getEquipmentType().getSlotList();
      for (SlotEntity slot : slots) {
        List<String> speedCapabilityList = slot.getSpeedCapabilityList();
        for (String speedCapability : speedCapabilityList) {
          EquipmentIfPK equipmentIfPk = new EquipmentIfPK();
          equipmentIfPk.setSwClusterId(request.getClusterId());
          equipmentIfPk.setEquipmentTypeId(equipmentTypeId);
          equipmentIfPk.setPhysicalIfId(slot.getIfId());
          equipmentIfPk.setSpeedCapability(speedCapability);
          EquipmentIf equipmentIf = new EquipmentIf();
          equipmentIf.setId(equipmentIfPk);
          equipmentIfs.add(equipmentIf);
        }
      }
      return equipmentIfs;
    } finally {
      logger.methodEnd();
    }
  }

  private void sendEquipmentCreate(Equipment equipment) throws MsfException {
    try {
      logger.methodStart(new String[] { "equipment" }, new Object[] { equipment });
      EquipmentEcEntity equipmentEc = new EquipmentEcEntity();
      equipmentEc.setEquipmentTypeId(String.valueOf(equipment.getId().getEquipmentTypeId()));
      equipmentEc.setLagPrefix(requestBody.getEquipmentType().getIfDefinition().getLagPrefix());
      equipmentEc.setUnitConnector(requestBody.getEquipmentType().getIfDefinition().getUnitConnector());
      equipmentEc.setIfNameOid(requestBody.getEquipmentType().getSnmp().getIfNameOid());
      equipmentEc.setSnmptrapIfNameOid(requestBody.getEquipmentType().getSnmp().getSnmptrapIfNameOid());
      equipmentEc.setMaxRepetitions(requestBody.getEquipmentType().getSnmp().getMaxRepetitions());
      equipmentEc.setIfNameRuleList(getIfNameRules());
      equipmentEc.setEquipmentIfList(getEquipmentIfs());
      EquipmentCreateEcRequestBody equipmentCreateEcRequestBody = new EquipmentCreateEcRequestBody();
      equipmentCreateEcRequestBody.setEquipment(equipmentEc);

      RestRequestBase restRequest = new RestRequestBase();
      restRequest.setRequestBody(JsonUtil.toJson(equipmentCreateEcRequestBody));
      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.EQUIPMENT_CREATE.getHttpMethod(),
          EcRequestUri.EQUIPMENT_CREATE.getUri(), restRequest);

      if (restResponseBase.getHttpStatusCode() != HttpStatus.CREATED_201) {
        EquipmentCreateEcResponseBody equipmentCreateEcResponseBody = new EquipmentCreateEcResponseBody();
        equipmentCreateEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            EquipmentCreateEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
            restResponseBase.getHttpStatusCode(), equipmentCreateEcResponseBody.getErrorCode());
        logger.error(errorMsg);
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private List<IfNameRuleEcEntity> getIfNameRules() {
    try {
      logger.methodStart();
      List<IfNameRuleEcEntity> ifNameRules = new ArrayList<>();
      List<PortEntity> portList = requestBody.getEquipmentType().getIfDefinition().getPortList();
      for (PortEntity port : portList) {
        IfNameRuleEcEntity ifNameRule = new IfNameRuleEcEntity();
        ifNameRule.setSpeed(port.getSpeed());
        ifNameRule.setPortPrefix(port.getPortPrefix());
        ifNameRules.add(ifNameRule);
      }
      return ifNameRules;
    } finally {
      logger.methodEnd();
    }
  }

  private List<EquipmentIfEcEntity> getEquipmentIfs() {
    try {
      logger.methodStart();
      List<EquipmentIfEcEntity> equipmentIfs = new ArrayList<>();
      List<SlotEntity> slotList = requestBody.getEquipmentType().getSlotList();
      for (SlotEntity slot : slotList) {
        EquipmentIfEcEntity equipmentIf = new EquipmentIfEcEntity();
        equipmentIf.setPhysicalIfId(slot.getIfId());
        equipmentIf.setIfSlot(slot.getIfSlot());
        equipmentIfs.add(equipmentIf);
      }
      return equipmentIfs;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseEquipmentCreateData(Equipment equipment) {
    try {
      logger.methodStart(new String[] { "equipment" }, new Object[] { equipment });
      EquipmentCreateResponseBody body = new EquipmentCreateResponseBody();
      body.setEquipmentTypeId(String.valueOf(equipment.getId().getEquipmentTypeId()));
      return createRestResponse(body, HttpStatus.CREATED_201);
    } finally {
      logger.methodEnd();
    }
  }

}
