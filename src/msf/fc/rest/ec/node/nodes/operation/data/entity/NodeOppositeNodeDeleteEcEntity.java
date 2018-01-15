package msf.fc.rest.ec.node.nodes.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class NodeOppositeNodeDeleteEcEntity {

  
  @SerializedName("node_id")
  private String nodeId;

  
  @SerializedName("internal_link_ifs")
  private NodeInternalLinkDeleteEcEntity internalLinkIfDelete;

  
  public String getNodeId() {
    return nodeId;
  }

  
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  
  public NodeInternalLinkDeleteEcEntity getInternalLinkIfDelete() {
    return internalLinkIfDelete;
  }

  
  public void setInternalLinkIfDelete(NodeInternalLinkDeleteEcEntity internalLinkIfDelete) {
    this.internalLinkIfDelete = internalLinkIfDelete;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
