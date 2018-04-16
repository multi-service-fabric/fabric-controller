
package msf.fc.rest.ec.node.nodes.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class NodeVpnEcEntity {

  @SerializedName("vpn_type")
  private String vpnType;

  @SerializedName("l3vpn")
  private NodeL3VpnEcEntity l3Vpn;

  @SerializedName("l2vpn")
  private NodeL2VpnEcEntity l2Vpn;

  public String getVpnType() {
    return vpnType;
  }

  public void setVpnType(String vpnType) {
    this.vpnType = vpnType;
  }

  public NodeL3VpnEcEntity getL3Vpn() {
    return l3Vpn;
  }

  public void setL3Vpn(NodeL3VpnEcEntity l3Vpn) {
    this.l3Vpn = l3Vpn;
  }

  public NodeL2VpnEcEntity getL2Vpn() {
    return l2Vpn;
  }

  public void setL2Vpn(NodeL2VpnEcEntity l2Vpn) {
    this.l2Vpn = l2Vpn;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
