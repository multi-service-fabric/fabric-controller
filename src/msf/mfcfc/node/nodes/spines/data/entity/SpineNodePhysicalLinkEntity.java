package msf.mfcfc.node.nodes.spines.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class SpineNodePhysicalLinkEntity {

  
  @SerializedName("opposite_node_id")
  private String oppositeNodeId;

  
  @SerializedName("local_traffic_threshold")
  private Double localTrafficThreshold;

  
  @SerializedName("opposite_traffic_threshold")
  private Double oppositeTrafficThreshold;

  
  @SerializedName("internal_link_if")
  private SpineNodeInternalLinkIfEntity internalLinkIf;

  
  public String getOppositeNodeId() {
    return oppositeNodeId;
  }

  
  public void setOppositeNodeId(String oppositeNodeId) {
    this.oppositeNodeId = oppositeNodeId;
  }

  
  public Double getLocalTrafficThreshold() {
    return localTrafficThreshold;
  }

  
  public void setLocalTrafficThreshold(Double localTrafficThreshold) {
    this.localTrafficThreshold = localTrafficThreshold;
  }

  
  public Double getOppositeTrafficThreshold() {
    return oppositeTrafficThreshold;
  }

  
  public void setOppositeTrafficThreshold(Double oppositeTrafficThreshold) {
    this.oppositeTrafficThreshold = oppositeTrafficThreshold;
  }

  
  public SpineNodeInternalLinkIfEntity getInternalLinkIf() {
    return internalLinkIf;
  }

  
  public void setInternalLinkIf(SpineNodeInternalLinkIfEntity internalLinkIf) {
    this.internalLinkIf = internalLinkIf;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
