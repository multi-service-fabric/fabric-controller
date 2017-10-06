package msf.fc.rest.ec.node.nodes.leaf.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class L3VpnEcEntity {

  @SerializedName("bgp")
  private BgpEcEntity bgp;
  @SerializedName("as")
  private AsEcEntity as;

  public BgpEcEntity getBgp() {
    return bgp;
  }

  public void setBgp(BgpEcEntity bgp) {
    this.bgp = bgp;
  }

  public AsEcEntity getAs() {
    return as;
  }

  public void setAs(AsEcEntity as) {
    this.as = as;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
