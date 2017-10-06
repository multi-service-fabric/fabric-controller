package msf.fc.node.equipments.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentTypeEntity {

  @SerializedName("equipment_type_id")
  private String equipmentTypeId;

  @SerializedName("platform")
  private String platform;

  @SerializedName("os")
  private String os;

  @SerializedName("firmware")
  private String firmware;

  @SerializedName("capability")
  private CapabilityEntity capability;

  @SerializedName("dhcp")
  private DhcpEntity dhcp;

  @SerializedName("snmp")
  private SnmpEntity snmp;

  @SerializedName("boot_complete_msg")
  private String bootCompleteMsg;

  @SerializedName("boot_error_msgs")
  private List<String> bootErrorMsgList;

  @SerializedName("if_definitions")
  private IfDefinitionEntity ifDefinition;

  @SerializedName("slots")
  private List<SlotEntity> slotList;

  public String getEquipmentTypeId() {
    return equipmentTypeId;
  }

  public void setEquipmentTypeId(String equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getOs() {
    return os;
  }

  public void setOs(String os) {
    this.os = os;
  }

  public String getFirmware() {
    return firmware;
  }

  public void setFirmware(String firmware) {
    this.firmware = firmware;
  }

  public CapabilityEntity getCapability() {
    return capability;
  }

  public void setCapability(CapabilityEntity capability) {
    this.capability = capability;
  }

  public DhcpEntity getDhcp() {
    return dhcp;
  }

  public void setDhcp(DhcpEntity dhcp) {
    this.dhcp = dhcp;
  }

  public SnmpEntity getSnmp() {
    return snmp;
  }

  public void setSnmp(SnmpEntity snmp) {
    this.snmp = snmp;
  }

  public String getBootCompleteMsg() {
    return bootCompleteMsg;
  }

  public void setBootCompleteMsg(String bootCompleteMsg) {
    this.bootCompleteMsg = bootCompleteMsg;
  }

  public List<String> getBootErrorMsgList() {
    return bootErrorMsgList;
  }

  public void setBootErrorMsgList(List<String> bootErrorMsgList) {
    this.bootErrorMsgList = bootErrorMsgList;
  }

  public IfDefinitionEntity getIfDefinition() {
    return ifDefinition;
  }

  public void setIfDefinition(IfDefinitionEntity ifDefinition) {
    this.ifDefinition = ifDefinition;
  }

  public List<SlotEntity> getSlotList() {
    return slotList;
  }

  public void setSlotList(List<SlotEntity> slotList) {
    this.slotList = slotList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
