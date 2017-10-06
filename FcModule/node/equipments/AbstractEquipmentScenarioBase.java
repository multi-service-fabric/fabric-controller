package msf.fc.node.equipments;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.data.BootErrorMessage;
import msf.fc.common.data.Equipment;
import msf.fc.common.data.EquipmentIf;
import msf.fc.common.data.EquipmentPK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.AbstractScenario;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EquipmentDao;
import msf.fc.node.equipments.data.entity.CapabilityEntity;
import msf.fc.node.equipments.data.entity.DhcpEntity;
import msf.fc.node.equipments.data.entity.EquipmentTypeEntity;
import msf.fc.node.equipments.data.entity.IfDefinitionEntity;
import msf.fc.node.equipments.data.entity.PortEntity;
import msf.fc.node.equipments.data.entity.SlotEntity;
import msf.fc.node.equipments.data.entity.SnmpEntity;
import msf.fc.rest.common.AbstractResponseBody;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentEcEntity;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentIfEcEntity;
import msf.fc.rest.ec.node.equipment.data.entity.IfNameRuleEcEntity;

public abstract class AbstractEquipmentScenarioBase<T extends RestRequestBase> extends AbstractScenario<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractEquipmentScenarioBase.class);

  private static final Integer FIRST_ID_NUM = 1;

  protected Equipment getEquipment(SessionWrapper sessionWrapper, EquipmentDao equipmentDao, String swClusterId,
      Integer equipmentTypeId) throws MsfException {
    try {
      logger.methodStart();
      EquipmentPK equipmentPk = new EquipmentPK();
      equipmentPk.setSwClusterId(swClusterId);
      equipmentPk.setEquipmentTypeId(equipmentTypeId);
      Equipment equipment = equipmentDao.read(sessionWrapper, equipmentPk);
      if (equipment == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = equipment");
      }
      return equipment;
    } finally {
      logger.methodEnd();
    }
  }

  protected EquipmentTypeEntity getEquipmentType(Equipment equipment, EquipmentEcEntity equipmentEcData)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "equipment", "equipmentEcData" }, new Object[] { equipment, equipmentEcData });
      CapabilityEntity capability = new CapabilityEntity();
      capability.setL2Vpn(equipment.getL2vpnCapability());
      capability.setL3Vpn(equipment.getL3vpnCapability());
      DhcpEntity dhcp = new DhcpEntity();
      dhcp.setConfigTemplate(equipment.getConfigTemplate());
      dhcp.setInitialConfig(equipment.getInitialConfig());
      SnmpEntity snmp = new SnmpEntity();
      snmp.setIfNameOid(equipmentEcData.getIfNameOid());
      snmp.setSnmptrapIfNameOid(equipmentEcData.getSnmptrapIfNameOid());
      snmp.setMaxRepetitions(equipmentEcData.getMaxRepetitions());
      IfDefinitionEntity ifDefinition = new IfDefinitionEntity();
      ifDefinition.setPortList(getPorts(equipmentEcData));
      ifDefinition.setLagPrefix(equipmentEcData.getLagPrefix());
      ifDefinition.setUnitConnector(equipmentEcData.getUnitConnector());
      EquipmentTypeEntity equipmentType = new EquipmentTypeEntity();
      equipmentType.setEquipmentTypeId(String.valueOf(equipment.getId().getEquipmentTypeId()));
      equipmentType.setPlatform(equipment.getPlatformName());
      equipmentType.setOs(equipment.getOsName());
      equipmentType.setFirmware(equipment.getFirmwareVersion());
      equipmentType.setCapability(capability);
      equipmentType.setDhcp(dhcp);
      equipmentType.setSnmp(snmp);
      equipmentType.setBootCompleteMsg(equipment.getBootCompleteMsg());
      equipmentType.setBootErrorMsgList(getBootErrorMessages(equipment));
      equipmentType.setIfDefinition(ifDefinition);
      equipmentType.setSlotList(getSlots(equipment, equipmentEcData));
      return equipmentType;
    } finally {
      logger.methodEnd();
    }
  }

  private ArrayList<String> getBootErrorMessages(Equipment equipment) throws MsfException {
    try {
      logger.methodStart(new String[] { "equipment" }, new Object[] { equipment });
      ArrayList<String> bootErrorMessages = new ArrayList<>();
      for (BootErrorMessage bootErrorMessage : equipment.getBootErrorMessages()) {
        bootErrorMessages.add(bootErrorMessage.getId().getBootErrorMsg());
      }
      return bootErrorMessages;
    } finally {
      logger.methodEnd();
    }
  }

  private ArrayList<PortEntity> getPorts(EquipmentEcEntity equipmentEcData) {
    try {
      logger.methodStart(new String[] { "equipmentEcData" }, new Object[] { equipmentEcData });
      ArrayList<PortEntity> ports = new ArrayList<>();
      for (IfNameRuleEcEntity ifNameRule : equipmentEcData.getIfNameRuleList()) {
        PortEntity port = new PortEntity();
        port.setSpeed(ifNameRule.getSpeed());
        port.setPortPrefix(ifNameRule.getPortPrefix());
        ports.add(port);
      }
      return ports;
    } finally {
      logger.methodEnd();
    }
  }

  private ArrayList<SlotEntity> getSlots(Equipment equipment, EquipmentEcEntity equipmentEcData) throws MsfException {
    try {
      logger.methodStart(new String[] { "equipment", "equipmentEcData" }, new Object[] { equipment, equipmentEcData });
      ArrayList<SlotEntity> slots = new ArrayList<>();
      List<EquipmentIf> equipmentIfs = equipment.getEquipmentIfs();
      TreeMap<String, List<String>> speedCapabilityMap = new TreeMap<>();
      for (EquipmentIf equipmentIf : equipmentIfs) {
        String physicalIfId = equipmentIf.getId().getPhysicalIfId();
        List<String> speedCapabilities;
        if (null == (speedCapabilities = speedCapabilityMap.get(physicalIfId))) {
          speedCapabilities = new ArrayList<>();
        }
        speedCapabilities.add(equipmentIf.getId().getSpeedCapability());
        speedCapabilityMap.put(physicalIfId, speedCapabilities);
      }

      for (EquipmentIfEcEntity equipmentEcIf : equipmentEcData.getEquipmentIfList()) {
        List<String> speedCapabilities = speedCapabilityMap.get(equipmentEcIf.getPhysicalIfId());
        if (speedCapabilities == null) {
          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is only the data in the EC system.");
        }
        SlotEntity slot = new SlotEntity();
        slot.setIfId(equipmentEcIf.getPhysicalIfId());
        slot.setIfSlot(equipmentEcIf.getIfSlot());
        slot.setSpeedCapabilityList(speedCapabilities);
        slots.add(slot);
      }

      if (slots.size() != speedCapabilityMap.size()) {
        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is only the data in the FC system.");
      }
      return slots;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body" }, new Object[] { ToStringBuilder.reflectionToString(body) });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  protected Integer getNextEquipmentTypeId(SessionWrapper sessionWrapper, EquipmentDao equipmentDao)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "equipmentDao" },
          new Object[] { sessionWrapper, equipmentDao });
      Equipment biggestIdEquipment = equipmentDao.readFromBiggestId(sessionWrapper);
      if (null == biggestIdEquipment) {
        return FIRST_ID_NUM;
      } else {
        return biggestIdEquipment.getId().getEquipmentTypeId() + 1;
      }
    } finally {
      logger.methodEnd();
    }
  }

}
