package msf.fc.rest.ec.node.nodes.leaf.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.VpnType;

public class VpnEcEntity {
  @SerializedName("vpn_type")
  private String vpnType;

  @SerializedName("l3vpn")
  private L3VpnEcEntity l3Vpn;

  @SerializedName("l2vpn")
  private L2VpnEcEntity l2Vpn;

  public String getVpnType() {
    return vpnType;
  }

  public void setVpnType(String vpnType) {
    this.vpnType = vpnType;
  }

  public L3VpnEcEntity getL3Vpn() {
    return l3Vpn;
  }

  public VpnType getVpnTypEnum() {
    return VpnType.getEnumFromMessage(vpnType);
  }

  public void setVpnTypeEnum(VpnType vpnType) {
    this.vpnType = vpnType.getMessage();
  }

  public void setL3Vpn(L3VpnEcEntity l3Vpn) {
    this.l3Vpn = l3Vpn;
  }

  public L2VpnEcEntity getL2Vpn() {
    return l2Vpn;
  }

  public void setL2Vpn(L2VpnEcEntity l2Vpn) {
    this.l2Vpn = l2Vpn;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
