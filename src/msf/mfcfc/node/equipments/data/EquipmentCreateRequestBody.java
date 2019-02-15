
package msf.mfcfc.node.equipments.data;

import java.text.MessageFormat;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.node.equipments.data.entity.EquipmentPortEntity;
import msf.mfcfc.node.equipments.data.entity.EquipmentSlotEntity;
import msf.mfcfc.node.equipments.data.entity.EquipmentTypeEntity;
import msf.mfcfc.rest.common.RestRequestValidator;

public class EquipmentCreateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(EquipmentCreateRequestBody.class);

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

      ParameterCheckUtil.checkNotNull(equipmentType);
      validateEquipmentType();

    } finally {
      logger.methodEnd();
    }

  }

  private void validateEquipmentType() throws MsfException {

    if (equipmentType.getEquipmentTypeId() != null) {
      ParameterCheckUtil.checkNumericId(equipmentType.getEquipmentTypeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
    }

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getPlatform());

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getOs());

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getFirmware());

    ParameterCheckUtil.checkNotNull(equipmentType.getRouterTypeEnum());

    switch (equipmentType.getRouterTypeEnum()) {
      case CORE_ROUTER:

        ParameterCheckUtil.checkNotNullAndLength(equipmentType.getPhysicalIfNameSyntax());
        break;
      case NORMAL_ROUTER:
        break;
      default:

        throw new IllegalArgumentException(MessageFormat.format("routerType={0}", equipmentType.getRouterTypeEnum()));
    }

    ParameterCheckUtil.checkNotNull(equipmentType.getCapability());

    ParameterCheckUtil.checkNotNull(equipmentType.getDhcp());

    ParameterCheckUtil.checkNotNull(equipmentType.getSnmp());

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getBootCompleteMsg());

    ParameterCheckUtil.checkNotNull(equipmentType.getIfDefinitions());

    ParameterCheckUtil.checkNotNull(equipmentType.getSlotList());

    validateCapability();
    validateDhcp();
    validateSnmp();
    validateIfDefinitions();
    validateSlots();
  }

  private void validateCapability() throws MsfException {

    ParameterCheckUtil.checkNotNull(equipmentType.getCapability().getVpn());

    ParameterCheckUtil.checkNotNull(equipmentType.getCapability().getQos());

    if (!equipmentType.getCapability().getVpn().getL2()) {
      if (equipmentType.getCapability().getIrb() != null) {
        String logMsg = "capability information on irb must be not set for the parameter.";
        logger.error(logMsg);
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
      }
    }

    validateVpn();
    validateQos();
    validateIrb();
    validateTraffic();
  }

  private void validateVpn() throws MsfException {

    ParameterCheckUtil.checkNotNull(equipmentType.getCapability().getVpn().getL2());

    ParameterCheckUtil.checkNotNull(equipmentType.getCapability().getVpn().getL3());
  }

  private void validateQos() throws MsfException {

    Boolean remark = equipmentType.getCapability().getQos().getRemark();
    ParameterCheckUtil.checkNotNull(remark);
    if (remark) {

      ParameterCheckUtil.checkNotNull(equipmentType.getCapability().getQos().getRemarkCapabilityList());

      ParameterCheckUtil.checkNotNullAndLength(equipmentType.getCapability().getQos().getRemarkDefault());
    }

    Boolean shaping = equipmentType.getCapability().getQos().getShaping();
    ParameterCheckUtil.checkNotNull(shaping);
    if (shaping) {

      ParameterCheckUtil.checkNotNull(equipmentType.getCapability().getQos().getEgressQueueCapabilityList());

      ParameterCheckUtil.checkNotNullAndLength(equipmentType.getCapability().getQos().getEgressQueueDefault());
    }
  }

  private void validateIrb() throws MsfException {

    if (equipmentType.getCapability().getIrb() != null) {

      Boolean asymmetric = equipmentType.getCapability().getIrb().getAsymmetric();
      ParameterCheckUtil.checkNotNull(asymmetric);

      Boolean symmetric = equipmentType.getCapability().getIrb().getSymmetric();
      ParameterCheckUtil.checkNotNull(symmetric);
    }
  }

  private void validateTraffic() throws MsfException {

    if (equipmentType.getCapability().getTraffic() != null) {
      if (equipmentType.getCapability().getTraffic().getVlanTrafficCapability() != null) {
        ParameterCheckUtil.checkNotNull(equipmentType.getCapability().getTraffic().getVlanTrafficCapabilityEnum());
        switch (equipmentType.getCapability().getTraffic().getVlanTrafficCapabilityEnum()) {
          case MIB:

            ParameterCheckUtil
                .checkNotNullAndLength(equipmentType.getCapability().getTraffic().getVlanTrafficCounterNameMibOid());

            ParameterCheckUtil
                .checkNotNullAndLength(equipmentType.getCapability().getTraffic().getVlanTrafficCounterValueMibOid());
            break;
          case CLI:

            ParameterCheckUtil.checkNotNullAndLength(equipmentType.getCapability().getTraffic().getCliExecPath());
            break;
          default:

            throw new IllegalArgumentException(MessageFormat.format("routerType={0}",
                equipmentType.getCapability().getTraffic().getVlanTrafficCapabilityEnum()));
        }
      }
    }
  }

  private void validateDhcp() throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getDhcp().getDhcpTemplate());

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getDhcp().getConfigTemplate());

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getDhcp().getInitialConfig());
  }

  private void validateSnmp() throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getSnmp().getIfNameOid());

    ParameterCheckUtil.checkNotNull(equipmentType.getSnmp().getMaxRepetitions());
  }

  private void validateIfDefinitions() throws MsfException {

    ParameterCheckUtil.checkNotNull(equipmentType.getIfDefinitions().getPortList());

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getIfDefinitions().getLagPrefix());

    ParameterCheckUtil.checkNotNullAndLength(equipmentType.getIfDefinitions().getUnitConnector());
    validatePorts();
  }

  private void validatePorts() throws MsfException {

    for (EquipmentPortEntity tempPort : equipmentType.getIfDefinitions().getPortList()) {

      ParameterCheckUtil.checkNotNullAndLength(tempPort.getSpeed());

      ParameterCheckUtil.checkNotNullAndLength(tempPort.getPortPrefix());
    }
  }

  private void validateSlots() throws MsfException {

    for (EquipmentSlotEntity tempSlot : equipmentType.getSlotList()) {

      ParameterCheckUtil.checkIdSpecifiedByUri(tempSlot.getIfId());

      ParameterCheckUtil.checkNotNullAndLength(tempSlot.getIfSlot());

      ParameterCheckUtil.checkNotNull(tempSlot.getSpeedCapacityList());
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
