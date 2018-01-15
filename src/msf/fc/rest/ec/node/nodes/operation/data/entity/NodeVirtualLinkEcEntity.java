package msf.fc.rest.ec.node.nodes.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class NodeVirtualLinkEcEntity {

  
  @SerializedName("node_id")
  private String nodeId;

  
  public String getNodeId() {
    return nodeId;
  }

  
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
