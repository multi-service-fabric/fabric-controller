package msf.fc.rest.ec.node.nodes.spine.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class VpnEcEntity {

  @SerializedName("l2vpn")
  private L2VpnEcEntity l2Vpn;

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
