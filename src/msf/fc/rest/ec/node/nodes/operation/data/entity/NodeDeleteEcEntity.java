package msf.fc.rest.ec.node.nodes.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class NodeDeleteEcEntity {

  
  @SerializedName("delete_nodes")
  private NodeDeleteNodesEcEntity deleteNodes;

  
  @SerializedName("update_node")
  private NodeDeleteUpdateNodeEcEntity updateNode;

  
  public NodeDeleteNodesEcEntity getDeleteNodes() {
    return deleteNodes;
  }

  
  public void setDeleteNodes(NodeDeleteNodesEcEntity deleteNodes) {
    this.deleteNodes = deleteNodes;
  }

  
  public NodeDeleteUpdateNodeEcEntity getUpdateNode() {
    return updateNode;
  }

  
  public void setUpdateNode(NodeDeleteUpdateNodeEcEntity updateNode) {
    this.updateNode = updateNode;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
