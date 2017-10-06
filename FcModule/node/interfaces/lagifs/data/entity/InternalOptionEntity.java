package msf.fc.node.interfaces.lagifs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class InternalOptionEntity {

  @SerializedName("ipv4_address")
  private String ipv4Address;

  @SerializedName("opposite_if")
  private OppositeLagIfEntity oppositeIf;

  public String getIpv4Address() {
    return ipv4Address;
  }

  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  public OppositeLagIfEntity getOppositeIf() {
    return oppositeIf;
  }

  public void setOppositeIf(OppositeLagIfEntity oppositeIf) {
    this.oppositeIf = oppositeIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
