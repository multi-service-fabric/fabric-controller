package msf.mfcfc.node.interfaces.lagifs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class LagIfInternalOptionEntity {
  
  @SerializedName("ipv4_address")
  private String ipv4Address;

  
  @SerializedName("opposite_if")
  private LagIfOppositeEntity oppositeIf;

  
  public String getIpv4Address() {
    return ipv4Address;
  }

  
  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  
  public LagIfOppositeEntity getOppositeIf() {
    return oppositeIf;
  }

  
  public void setOppositeIf(LagIfOppositeEntity oppositeIf) {
    this.oppositeIf = oppositeIf;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
