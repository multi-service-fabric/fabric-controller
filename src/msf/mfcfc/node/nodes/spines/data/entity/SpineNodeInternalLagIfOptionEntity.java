package msf.mfcfc.node.nodes.spines.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class SpineNodeInternalLagIfOptionEntity {

  
  @SerializedName("ipv4_address")
  private String ipv4Address;

  
  @SerializedName("opposite_if")
  private SpineNodeOppositeLagIfEntity oppositeIf;

  
  @SerializedName("traffic_threshold")
  private Double trafficThreshold;

  
  public String getIpv4Address() {
    return ipv4Address;
  }

  
  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  
  public SpineNodeOppositeLagIfEntity getOppositeIf() {
    return oppositeIf;
  }

  
  public void setOppositeIf(SpineNodeOppositeLagIfEntity oppositeIf) {
    this.oppositeIf = oppositeIf;
  }

  
  public Double getTrafficThreshold() {
    return trafficThreshold;
  }

  
  public void setTrafficThreshold(Double trafficThreshold) {
    this.trafficThreshold = trafficThreshold;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
