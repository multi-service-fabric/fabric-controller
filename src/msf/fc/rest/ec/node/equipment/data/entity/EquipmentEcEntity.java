
package msf.fc.rest.ec.node.equipment.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentEcEntity {

  @SerializedName("equipment_type_id")
  private String equipmentTypeId;

  @SerializedName("lag_prefix")
  private String lagPrefix;

  @SerializedName("unit_connector")
  private String unitConnector;

  @SerializedName("if_name_oid")
  private String ifNameOid;

  @SerializedName("snmptrap_if_name_oid")
  private String snmptrapIfNameOid;

  @SerializedName("max_repetitions")
  private Integer maxRepetitions;

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

  @SerializedName("ztp")
  private EquipmentZtpEcEntity ztp;

  @SerializedName("capabilities")
  private EquipmentCapabilitiesEcEntity capabilities;

  @SerializedName("qos")
  private EquipmentQosEcEntity qos;

  @SerializedName("if_name_rules")
  private List<EquipmentIfNameRulesaEcEntity> ifNameRulesaList;

  @SerializedName("equipment_ifs")
  private List<EquipmentIfEcEntity> equipmentIfList;

  @SerializedName("same_vlan_number_traffic_total_value_flag")
  private Boolean sameVlanNumberTrafficTotalValueFlag;

  @SerializedName("vlan_traffic_capability")
  private String vlanTrafficCapability;

  @SerializedName("vlan_traffic_counter_name_mib_oid")
  private String vlanTrafficCounterNameMibOid;

  @SerializedName("vlan_traffic_counter_value_mib_oid")
  private String vlanTrafficCounterValueMibOid;

  @SerializedName("cli_exec_path")
  private String cliExecPath;

  public String getEquipmentTypeId() {
    return equipmentTypeId;
  }

  public void setEquipmentTypeId(String equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  public String getLagPrefix() {
    return lagPrefix;
  }

  public void setLagPrefix(String lagPrefix) {
    this.lagPrefix = lagPrefix;
  }

  public String getUnitConnector() {
    return unitConnector;
  }

  public void setUnitConnector(String unitConnector) {
    this.unitConnector = unitConnector;
  }

  public String getIfNameOid() {
    return ifNameOid;
  }

  public void setIfNameOid(String ifNameOid) {
    this.ifNameOid = ifNameOid;
  }

  public String getSnmptrapIfNameOid() {
    return snmptrapIfNameOid;
  }

  public void setSnmptrapIfNameOid(String snmptrapIfNameOid) {
    this.snmptrapIfNameOid = snmptrapIfNameOid;
  }

  public Integer getMaxRepetitions() {
    return maxRepetitions;
  }

  public void setMaxRepetitions(Integer maxRepetitions) {
    this.maxRepetitions = maxRepetitions;
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

  public EquipmentZtpEcEntity getZtp() {
    return ztp;
  }

  public void setZtp(EquipmentZtpEcEntity ztp) {
    this.ztp = ztp;
  }

  public EquipmentCapabilitiesEcEntity getCapabilities() {
    return capabilities;
  }

  public void setCapabilities(EquipmentCapabilitiesEcEntity capabilities) {
    this.capabilities = capabilities;
  }

  public EquipmentQosEcEntity getQos() {
    return qos;
  }

  public void setQos(EquipmentQosEcEntity qos) {
    this.qos = qos;
  }

  public List<EquipmentIfNameRulesaEcEntity> getIfNameRulesaList() {
    return ifNameRulesaList;
  }

  public void setIfNameRulesaList(List<EquipmentIfNameRulesaEcEntity> ifNameRulesaList) {
    this.ifNameRulesaList = ifNameRulesaList;
  }

  public List<EquipmentIfEcEntity> getEquipmentIfList() {
    return equipmentIfList;
  }

  public void setEquipmentIfList(List<EquipmentIfEcEntity> equipmentIfList) {
    this.equipmentIfList = equipmentIfList;
  }

  public Boolean getSameVlanNumberTrafficTotalValueFlag() {
    return sameVlanNumberTrafficTotalValueFlag;
  }

  public void setSameVlanNumberTrafficTotalValueFlag(Boolean sameVlanNumberTrafficTotalValueFlag) {
    this.sameVlanNumberTrafficTotalValueFlag = sameVlanNumberTrafficTotalValueFlag;
  }

  public String getVlanTrafficCapability() {
    return vlanTrafficCapability;
  }

  public void setVlanTrafficCapability(String vlanTrafficCapability) {
    this.vlanTrafficCapability = vlanTrafficCapability;
  }

  public String getVlanTrafficCounterNameMibOid() {
    return vlanTrafficCounterNameMibOid;
  }

  public void setVlanTrafficCounterNameMibOid(String vlanTrafficCounterNameMibOid) {
    this.vlanTrafficCounterNameMibOid = vlanTrafficCounterNameMibOid;
  }

  public String getVlanTrafficCounterValueMibOid() {
    return vlanTrafficCounterValueMibOid;
  }

  public void setVlanTrafficCounterValueMibOid(String vlanTrafficCounterValueMibOid) {
    this.vlanTrafficCounterValueMibOid = vlanTrafficCounterValueMibOid;
  }

  public String getCliExecPath() {
    return cliExecPath;
  }

  public void setCliExecPath(String cliExecPath) {
    this.cliExecPath = cliExecPath;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
