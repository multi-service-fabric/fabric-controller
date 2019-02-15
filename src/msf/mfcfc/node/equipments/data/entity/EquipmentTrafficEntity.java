
package msf.mfcfc.node.equipments.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.EquipmentVlanTrafficCapability;

public class EquipmentTrafficEntity {

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

  public EquipmentVlanTrafficCapability getVlanTrafficCapabilityEnum() {
    return EquipmentVlanTrafficCapability.getEnumFromMessage(vlanTrafficCapability);
  }

  public void setVlanTrafficCapabilityEnum(EquipmentVlanTrafficCapability vlanTrafficCapability) {
    this.vlanTrafficCapability = vlanTrafficCapability.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
