
package msf.mfcfc.node.nodes.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.VpnType;

public class NodeVpnEntity {

  @SerializedName("vpn_type")
  private String vpnType;

  @SerializedName("l3vpn")
  private NodeL3VpnEntity l3vpn;

  @SerializedName("l2vpn")
  private NodeL2VpnEntity l2vpn;

  public String getVpnType() {
    return vpnType;
  }

  public void setVpnType(String vpnType) {
    this.vpnType = vpnType;
  }

  public NodeL3VpnEntity getL3vpn() {
    return l3vpn;
  }

  public void setL3vpn(NodeL3VpnEntity l3vpn) {
    this.l3vpn = l3vpn;
  }

  public NodeL2VpnEntity getL2vpn() {
    return l2vpn;
  }

  public void setL2vpn(NodeL2VpnEntity l2vpn) {
    this.l2vpn = l2vpn;
  }

  public VpnType getVpnTypeEnum() {
    return VpnType.getEnumFromMessage(vpnType);
  }

  public void setVpnTypeEnum(VpnType vpnType) {
    this.vpnType = vpnType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
