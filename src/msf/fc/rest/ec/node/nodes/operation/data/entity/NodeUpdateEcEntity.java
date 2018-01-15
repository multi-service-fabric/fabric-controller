package msf.fc.rest.ec.node.nodes.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class NodeUpdateEcEntity {

  
  @SerializedName("action")
  private String action;

  
  @SerializedName("node")
  private NodeUpdateNodeEcEntity node;

  
  @SerializedName("pair_node")
  private NodePairNodeEcEntity pairNode;

  
  public String getAction() {
    return action;
  }

  
  public void setAction(String action) {
    this.action = action;
  }

  
  public NodeUpdateNodeEcEntity getNode() {
    return node;
  }

  
  public void setNode(NodeUpdateNodeEcEntity node) {
    this.node = node;
  }

  
  public NodePairNodeEcEntity getPairNode() {
    return pairNode;
  }

  
  public void setPairNode(NodePairNodeEcEntity pairNode) {
    this.pairNode = pairNode;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
