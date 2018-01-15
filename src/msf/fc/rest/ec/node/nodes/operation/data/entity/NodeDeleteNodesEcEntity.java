package msf.fc.rest.ec.node.nodes.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class NodeDeleteNodesEcEntity {

  
  @SerializedName("node_id")
  private String nodeId;

  
  @SerializedName("node_type")
  private String nodeType;

  
  @SerializedName("opposite_nodes")
  private List<NodeOppositeNodeDeleteEcEntity> oppositeNodeDeleteList;

  
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

  
  public List<NodeOppositeNodeDeleteEcEntity> getOppositeNodeDeleteList() {
    return oppositeNodeDeleteList;
  }

  
  public void setOppositeNodeDeleteList(List<NodeOppositeNodeDeleteEcEntity> oppositeNodeDeleteList) {
    this.oppositeNodeDeleteList = oppositeNodeDeleteList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
