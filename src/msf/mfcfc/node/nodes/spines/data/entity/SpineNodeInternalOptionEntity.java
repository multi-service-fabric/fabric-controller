package msf.mfcfc.node.nodes.spines.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class SpineNodeInternalOptionEntity {

  
  @SerializedName("ipv4_address")
  private String ipv4Address;

  
  @SerializedName("opposite_if")
  private SpineNodeOppositeIfEntity oppositeIf;

  
  @SerializedName("traffic_threshold")
  private Double trafficThreshold;

  
  public String getIpv4Address() {
    return ipv4Address;
  }

  
  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  
  public SpineNodeOppositeIfEntity getOppositeIf() {
    return oppositeIf;
  }

  
  public void setOppositeIf(SpineNodeOppositeIfEntity oppositeIf) {
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
