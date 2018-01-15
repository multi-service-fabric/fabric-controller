package msf.mfcfc.node.nodes.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class NodeUpdateNodeEntity {

  
  @SerializedName("node_id")
  private String nodeId;

  
  @SerializedName("node_type")
  private String nodeType;

  
  @SerializedName("cluster_area")
  private String clusterArea;

  
  @SerializedName("virtual_link")
  private NodeVirtualLinkEntity virtualLink;

  
  public String getNodeId() {
    return nodeId;
  }

  
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  
  public String getNodeType() {
    return nodeType;
  }

  
  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  
  public String getClusterArea() {
    return clusterArea;
  }

  
  public void setClusterArea(String clusterArea) {
    this.clusterArea = clusterArea;
  }

  
  public NodeVirtualLinkEntity getVirtualLink() {
    return virtualLink;
  }

  
  public void setVirtualLink(NodeVirtualLinkEntity virtualLink) {
    this.virtualLink = virtualLink;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
