
package msf.mfcfc.node.equipments.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.EquipmentRouterType;

public class EquipmentTypeEntity {

  @SerializedName("equipment_type_id")
  private String equipmentTypeId;

  @SerializedName("platform")
  private String platform;

  @SerializedName("os")
  private String os;

  @SerializedName("firmware")
  private String firmware;

  @SerializedName("router_type")
  private String routerType;

  @SerializedName("physical_if_name_syntax")
  private String physicalIfNameSyntax;

  @SerializedName("breakout_if_name_syntax")
  private String breakoutIfNameSyntax;

  @SerializedName("breakout_if_name_suffix_list")
  private String breakoutIfNameSuffixList;

  @SerializedName("capability")
  private EquipmentCapabilityEntity capability;

  @SerializedName("dhcp")
  private EquipmentDhcpEntity dhcp;

  @SerializedName("snmp")
  private EquipmentSnmpEntity snmp;

  @SerializedName("boot_complete_msg")
  private String bootCompleteMsg;

  @SerializedName("boot_error_msgs")
  private List<String> bootErrorMsgList;

  @SerializedName("if_definitions")
  private EquipmentIfDefinitionEntity ifDefinitions;

  @SerializedName("slots")
  private List<EquipmentSlotEntity> slotList;

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

  public String getRouterType() {
    return routerType;
  }

  public void setRouterType(String routerType) {
    this.routerType = routerType;
  }

  public String getPhysicalIfNameSyntax() {
    return physicalIfNameSyntax;
  }

  public void setPhysicalIfNameSyntax(String physicalIfNameSyntax) {
    this.physicalIfNameSyntax = physicalIfNameSyntax;
  }

  public String getBreakoutIfNameSyntax() {
    return breakoutIfNameSyntax;
  }

  public void setBreakoutIfNameSyntax(String breakoutIfNameSyntax) {
    this.breakoutIfNameSyntax = breakoutIfNameSyntax;
  }

  public String getBreakoutIfNameSuffixList() {
    return breakoutIfNameSuffixList;
  }

  public void setBreakoutIfNameSuffixList(String breakoutIfNameSuffixList) {
    this.breakoutIfNameSuffixList = breakoutIfNameSuffixList;
  }

  public EquipmentCapabilityEntity getCapability() {
    return capability;
  }

  public void setCapability(EquipmentCapabilityEntity capability) {
    this.capability = capability;
  }

  public EquipmentDhcpEntity getDhcp() {
    return dhcp;
  }

  public void setDhcp(EquipmentDhcpEntity dhcp) {
    this.dhcp = dhcp;
  }

  public EquipmentSnmpEntity getSnmp() {
    return snmp;
  }

  public void setSnmp(EquipmentSnmpEntity snmp) {
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

  public EquipmentIfDefinitionEntity getIfDefinitions() {
    return ifDefinitions;
  }

  public void setIfDefinitions(EquipmentIfDefinitionEntity ifDefinitions) {
    this.ifDefinitions = ifDefinitions;
  }

  public List<EquipmentSlotEntity> getSlotList() {
    return slotList;
  }

  public void setSlotList(List<EquipmentSlotEntity> slotList) {
    this.slotList = slotList;
  }

  public EquipmentRouterType getRouterTypeEnum() {
    return EquipmentRouterType.getEnumFromMessage(routerType);
  }

  public void setRouterTypeEnum(EquipmentRouterType routerType) {
    this.routerType = routerType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
