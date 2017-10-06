package msf.fc.rest.ec.core.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.CpCreateIfType;
import msf.fc.common.constant.NodeType;

public class L3BaseIfEcEntity {

  @SerializedName("node_type")
  private String nodeType;

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("router_id")
  private String routerId;

  private String type;
  @SerializedName("if_id")
  private String ifId;

  public String getNodeType() {
    return nodeType;
  }

  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getRouterId() {
    return routerId;
  }

  public void setRouterId(String routerId) {
    this.routerId = routerId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getIfId() {
    return ifId;
  }

  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  public NodeType getNodeTypeEnum() {
    return NodeType.getEnumFromSingularMessage(nodeType);
  }

  public void setNodeTypeEnum(NodeType nodeType) {
    this.nodeType = nodeType.getSingularMessage();
  }

  public CpCreateIfType getTypeEnum() {
    return CpCreateIfType.getEnumFromMessage(type);
  }

  public void setTypeEnum(CpCreateIfType type) {
    this.type = type.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
