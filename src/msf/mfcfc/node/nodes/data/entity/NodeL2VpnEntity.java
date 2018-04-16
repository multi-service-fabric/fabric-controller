
package msf.mfcfc.node.nodes.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class NodeL2VpnEntity {

  @SerializedName("bgp")
  private NodeBgpNodeNotifyEntity bgp;

  @SerializedName("as")
  private NodeAsNodeNotifyEntity as;

  public NodeBgpNodeNotifyEntity getBgp() {
    return bgp;
  }

  public void setBgp(NodeBgpNodeNotifyEntity bgp) {
    this.bgp = bgp;
  }

  public NodeAsNodeNotifyEntity getAs() {
    return as;
  }

  public void setAs(NodeAsNodeNotifyEntity as) {
    this.as = as;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
