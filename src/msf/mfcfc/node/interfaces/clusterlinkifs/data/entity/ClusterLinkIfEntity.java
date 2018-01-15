package msf.mfcfc.node.interfaces.clusterlinkifs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class ClusterLinkIfEntity {
  
  @SerializedName("cluster_link_if_id")
  private String clusterLinkIfId;

  
  @SerializedName("opposite_cluster_id")
  private String oppositeClusterId;

  
  @SerializedName("physical_link")
  private ClusterLinkIfPhysicalLinkEntity physicalLink;

  
  @SerializedName("lag_link")
  private ClusterLinkIfPhysicalLagLinkEntity lagLink;

  
  @SerializedName("igp_cost")
  private Integer igpCost;

  
  @SerializedName("port_status")
  private Boolean portStatus;

  
  @SerializedName("ipv4_address")
  private String ipv4Address;

  
  @SerializedName("traffic_threshold")
  private Double trafficThreshold;

  
  public String getClusterLinkIfId() {
    return clusterLinkIfId;
  }

  
  public void setClusterLinkIfId(String clusterLinkIfId) {
    this.clusterLinkIfId = clusterLinkIfId;
  }

  
  public String getOppositeClusterId() {
    return oppositeClusterId;
  }

  
  public void setOppositeClusterId(String oppositeClusterId) {
    this.oppositeClusterId = oppositeClusterId;
  }

  
  public ClusterLinkIfPhysicalLinkEntity getPhysicalLink() {
    return physicalLink;
  }

  
  public void setPhysicalLink(ClusterLinkIfPhysicalLinkEntity physicalLink) {
    this.physicalLink = physicalLink;
  }

  
  public ClusterLinkIfPhysicalLagLinkEntity getLagLink() {
    return lagLink;
  }

  
  public void setLagLink(ClusterLinkIfPhysicalLagLinkEntity lagLink) {
    this.lagLink = lagLink;
  }

  
  public Integer getIgpCost() {
    return igpCost;
  }

  
  public void setIgpCost(Integer igpCost) {
    this.igpCost = igpCost;
  }

  
  public Boolean getPortStatus() {
    return portStatus;
  }

  
  public void setPortStatus(Boolean portStatus) {
    this.portStatus = portStatus;
  }

  
  public String getIpv4Address() {
    return ipv4Address;
  }

  
  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
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
