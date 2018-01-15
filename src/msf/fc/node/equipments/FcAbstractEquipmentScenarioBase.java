
package msf.fc.node.equipments;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.data.FcEquipment;
import msf.fc.db.dao.clusters.FcEquipmentDao;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentEcEntity;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentIfEcEntity;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentIfNameRulesaEcEntity;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.equipments.AbstractEquipmentScenarioBase;
import msf.mfcfc.node.equipments.data.entity.EquipmentCapabilityEntity;
import msf.mfcfc.node.equipments.data.entity.EquipmentDhcpEntity;
import msf.mfcfc.node.equipments.data.entity.EquipmentIfDefinitionEntity;
import msf.mfcfc.node.equipments.data.entity.EquipmentPortEntity;
import msf.mfcfc.node.equipments.data.entity.EquipmentSlotEntity;
import msf.mfcfc.node.equipments.data.entity.EquipmentSnmpEntity;
import msf.mfcfc.node.equipments.data.entity.EquipmentTypeEntity;
import msf.mfcfc.node.equipments.data.entity.EquipmentVpnEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

/**
 * Abstract class to implement common process of model information-related
 * processing in configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request classs that inherited the RestRequestBase class
 */
public abstract class FcAbstractEquipmentScenarioBase<T extends RestRequestBase>
    extends AbstractEquipmentScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractEquipmentScenarioBase.class);

  private static final Integer FIRST_ID_NUM = 1;

  protected FcEquipment getEquipment(SessionWrapper sessionWrapper, FcEquipmentDao fcEquipmentDao,
      Integer equipmentTypeId) throws MsfException {
    try {
      logger.methodStart();
      FcEquipment fcEquipment = fcEquipmentDao.read(sessionWrapper, equipmentTypeId);
      if (fcEquipment == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = equipment");
      }
      return fcEquipment;
    } finally {
      logger.methodEnd();
    }
  }

  protected EquipmentTypeEntity getEquipmentType(FcEquipment fcEquipment, EquipmentEcEntity equipmentEcData)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "equipment", "equipmentEcData" },
          new Object[] { fcEquipment, equipmentEcData });
      EquipmentTypeEntity equipmentType = new EquipmentTypeEntity();
      equipmentType.setEquipmentTypeId(String.valueOf(fcEquipment.getEquipmentTypeId()));
      equipmentType.setPlatform(equipmentEcData.getPlatform());
      equipmentType.setOs(equipmentEcData.getOs());
      equipmentType.setFirmware(equipmentEcData.getFirmware());
      equipmentType.setRouterType(equipmentEcData.getRouterType());
      equipmentType.setPhysicalIfNameSyntax(equipmentEcData.getPhysicalIfNameSyntax());
      equipmentType.setBreakoutIfNameSyntax(equipmentEcData.getBreakoutIfNameSyntax());
      equipmentType.setBreakoutIfNameSuffixList(equipmentEcData.getBreakoutIfNameSuffixList());

      if (equipmentEcData.getCapabilities() != null) {
        EquipmentCapabilityEntity capability = new EquipmentCapabilityEntity();
        EquipmentVpnEntity vpn = new EquipmentVpnEntity();
        vpn.setL2(equipmentEcData.getCapabilities().getL2vpn());
        vpn.setL3(equipmentEcData.getCapabilities().getL3vpn());
        capability.setVpn(vpn);
        equipmentType.setCapability(capability);
      }

      EquipmentDhcpEntity dhcp = new EquipmentDhcpEntity();
      dhcp.setDhcpTemplate(equipmentEcData.getZtp().getDhcpTemplate());
      dhcp.setConfigTemplate(equipmentEcData.getZtp().getConfigTemplate());
      dhcp.setInitialConfig(equipmentEcData.getZtp().getInitialConfig());
      equipmentType.setDhcp(dhcp);

      EquipmentSnmpEntity snmp = new EquipmentSnmpEntity();
      snmp.setIfNameOid(equipmentEcData.getIfNameOid());
      snmp.setSnmptrapIfNameOid(equipmentEcData.getSnmptrapIfNameOid());
      snmp.setMaxRepetitions(equipmentEcData.getMaxRepetitions());
      equipmentType.setSnmp(snmp);

      equipmentType.setBootCompleteMsg(equipmentEcData.getZtp().getBootCompleteMsg());
      equipmentType.setBootErrorMsgList(equipmentEcData.getZtp().getBootErrorMsgList());

      EquipmentIfDefinitionEntity ifDefinition = new EquipmentIfDefinitionEntity();
      List<EquipmentPortEntity> equipmentPortList = new ArrayList<EquipmentPortEntity>();
      for (EquipmentIfNameRulesaEcEntity equipmentIfNameRulesaEcEntity : equipmentEcData.getIfNameRulesaList()) {
        EquipmentPortEntity equipmentPort = new EquipmentPortEntity();
        equipmentPort.setSpeed(equipmentIfNameRulesaEcEntity.getSpeed());
        equipmentPort.setPortPrefix(equipmentIfNameRulesaEcEntity.getPortPrefix());
        equipmentPortList.add(equipmentPort);
      }
      ifDefinition.setPortList(equipmentPortList);
      ifDefinition.setLagPrefix(equipmentEcData.getLagPrefix());
      ifDefinition.setUnitConnector(equipmentEcData.getUnitConnector());
      equipmentType.setIfDefinitions(ifDefinition);

      List<EquipmentSlotEntity> slotList = new ArrayList<>();
      for (EquipmentIfEcEntity equipmentIfEcEntity : equipmentEcData.getEquipmentIfList()) {
        EquipmentSlotEntity equipmentSlot = new EquipmentSlotEntity();
        equipmentSlot.setIfId(equipmentIfEcEntity.getPhysicalIfId());
        equipmentSlot.setIfSlot(equipmentIfEcEntity.getIfSlot());
        equipmentSlot.setSpeedCapacityList(equipmentIfEcEntity.getPortSpeedTypeList());
        slotList.add(equipmentSlot);
      }
      equipmentType.setSlotList(slotList);
      return equipmentType;
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

  protected Integer getNextEquipmentTypeId(SessionWrapper sessionWrapper, FcEquipmentDao fcEquipmentDao)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "equipmentDao" },
          new Object[] { sessionWrapper, fcEquipmentDao });
      FcEquipment biggestIdEquipment = fcEquipmentDao.readFromBiggestId(sessionWrapper);
      if (null == biggestIdEquipment) {

        return FIRST_ID_NUM;
      } else {
        return biggestIdEquipment.getEquipmentTypeId() + 1;
      }
    } finally {
      logger.methodEnd();
    }
  }

}
