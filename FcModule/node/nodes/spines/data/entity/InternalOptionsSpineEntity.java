package msf.fc.node.nodes.spines.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class InternalOptionsSpineEntity {

  @SerializedName("ipv4_address")
  private String ipv4Address;

  @SerializedName("opposite_if")
  private OppositeLagIfSpineEntity oppositeIf;

  public String getIpv4Address() {
    return ipv4Address;
  }

  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  public OppositeLagIfSpineEntity getOppositeIf() {
    return oppositeIf;
  }

  public void setOppositeIf(OppositeLagIfSpineEntity oppositeIf) {
    this.oppositeIf = oppositeIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
