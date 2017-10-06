package msf.fc.node.equipments.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.node.equipments.EquipmentCreateScenario;
import msf.fc.node.equipments.data.entity.EquipmentTypeEntity;
import msf.fc.node.equipments.data.entity.PortEntity;
import msf.fc.node.equipments.data.entity.SlotEntity;
import msf.fc.rest.common.RestRequestValidator;


public class EquipmentCreateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(EquipmentCreateScenario.class);

  @SerializedName("equipment_type")
  private EquipmentTypeEntity equipmentType;

  public EquipmentTypeEntity getEquipmentType() {
    return equipmentType;
  }

  public void setEquipmentType(EquipmentTypeEntity equipmentType) {
    this.equipmentType = equipmentType;
  }

  @Override
  public void validate() throws MsfException {

    try {
      logger.methodStart();

      validateEquipmentType();

    } finally {
      logger.methodEnd();
    }

  }

  private void validateEquipmentType() throws MsfException {

    ParameterCheckUtil.checkNotNull(equipmentType);

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getPlatform());

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getOs());

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getFirmware());

    validateCapability();

    validateDhcp();

    validateSnmp();

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getBootCompleteMsg());


    validateIfDefinition();

    validateSlots();
  }

  private void validateCapability() throws MsfException {
    ParameterCheckUtil.checkNotNull(equipmentType.getCapability());

    ParameterCheckUtil.checkNotNull(equipmentType.getCapability().isL2Vpn());

    ParameterCheckUtil.checkNotNull(equipmentType.getCapability().isL3Vpn());

  }

  private void validateDhcp() throws MsfException {

    ParameterCheckUtil.checkNotNull(equipmentType.getDhcp());

    ParameterCheckUtil.checkNotNull(equipmentType.getDhcp().getConfigTemplate());

    ParameterCheckUtil.checkNotNull(equipmentType.getDhcp().getInitialConfig());

  }

  private void validateSnmp() throws MsfException {

    ParameterCheckUtil.checkNotNull(equipmentType.getSnmp());

    ParameterCheckUtil.checkNotNull(equipmentType.getSnmp().getIfNameOid());


    ParameterCheckUtil.checkNotNull(equipmentType.getSnmp().getMaxRepetitions());

  }

  private void validateIfDefinition() throws MsfException {

    ParameterCheckUtil.checkNotNull(equipmentType.getIfDefinition());

    validatePort();

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getIfDefinition().getLagPrefix());

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getIfDefinition().getUnitConnector());

  }

  private void validatePort() throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getIfDefinition().getPortList());

    for (PortEntity tempPort : equipmentType.getIfDefinition().getPortList()) {

      ParameterCheckUtil.checkNotNullAndLength(tempPort.getPortPrefix());

      ParameterCheckUtil.checkNotNullAndLength(tempPort.getSpeed());

    }

  }

  private void validateSlots() throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getSlotList());

    for (SlotEntity tempSlot : equipmentType.getSlotList()) {
      ParameterCheckUtil.checkIdSpecifiedByUri(tempSlot.getIfId());

      ParameterCheckUtil.checkNotNullAndLength(tempSlot.getIfSlot());

      ParameterCheckUtil.checkNotNullAndLength(tempSlot.getSpeedCapabilityList());
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
