package msf.fc.rest.ec.core.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.PortMode;

public class CreateL2CpEcEntity {

  @SerializedName("cp_id")
  private String cpId;

  @SerializedName("base_if")
  private L2BaseIfEcEntity baseIf;

  @SerializedName("vlan_id")
  private String vlanId;
  @SerializedName("port_mode")
  private String portMode;

  public String getCpId() {
    return cpId;
  }

  public void setCpId(String cpId) {
    this.cpId = cpId;
  }

  public L2BaseIfEcEntity getBaseIf() {
    return baseIf;
  }

  public void setBaseIf(L2BaseIfEcEntity baseIf) {
    this.baseIf = baseIf;
  }

  public String getVlanId() {
    return vlanId;
  }

  public void setVlanId(String vlanId) {
    this.vlanId = vlanId;
  }

  public String getPortMode() {
    return portMode;
  }

  public void setPortMode(String portMode) {
    this.portMode = portMode;
  }

  public PortMode getPortModeEnum() {
    return PortMode.getEnumFromMessage(portMode);
  }

  public void setPortModeEnum(PortMode portMode) {
    this.portMode = portMode.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
