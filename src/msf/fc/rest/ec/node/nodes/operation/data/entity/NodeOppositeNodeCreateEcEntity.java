package msf.fc.rest.ec.node.nodes.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class NodeOppositeNodeCreateEcEntity {

  
  @SerializedName("node_id")
  private String nodeId;

  
  @SerializedName("breakout_base_ifs")
  private List<NodeBreakoutBaseIfEcEntity> breakoutBaseIfsList;

  
  @SerializedName("internal_link_if")
  private NodeInternalLinkIfCreateEcEntity internalLink;

  
  public String getNodeId() {
    return nodeId;
  }

  
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  
  public List<NodeBreakoutBaseIfEcEntity> getBreakoutBaseIfsList() {
    return breakoutBaseIfsList;
  }

  
  public void setBreakoutBaseIfsList(List<NodeBreakoutBaseIfEcEntity> breakoutBaseIfsList) {
    this.breakoutBaseIfsList = breakoutBaseIfsList;
  }

  
  public NodeInternalLinkIfCreateEcEntity getInternalLink() {
    return internalLink;
  }

  
  public void setInternalLink(NodeInternalLinkIfCreateEcEntity internalLink) {
    this.internalLink = internalLink;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
