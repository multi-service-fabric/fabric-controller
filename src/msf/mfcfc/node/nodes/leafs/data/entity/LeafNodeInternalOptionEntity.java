
package msf.mfcfc.node.nodes.leafs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LeafNodeInternalOptionEntity {

  @SerializedName("ipv4_address")
  private String ipv4Address;

  @SerializedName("opposite_if")
  private LeafNodeOppositeIfEntity oppositeIf;

  @SerializedName("traffic_threshold")
  private Double trafficThreshold;

  public String getIpv4Address() {
    return ipv4Address;
  }

  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  public LeafNodeOppositeIfEntity getOppositeIf() {
    return oppositeIf;
  }

  public void setOppositeIf(LeafNodeOppositeIfEntity oppositeIf) {
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
