package msf.fc.rest.ec.traffic.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.NodeType;

public class SwitchTrafficEcEntity {
  @SerializedName("node_type")
  private String nodeType;

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("traffic_value")
  private TrafficValueEcEntity trafficValue;

  public String getNodeType() {
    return nodeType;
  }

  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  public NodeType getNodeTypeEnum() {
    return NodeType.getEnumFromSingularMessage(nodeType);
  }

  public void setNodeTypeEnum(NodeType nodeType) {
    this.nodeType = nodeType.getSingularMessage();
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public TrafficValueEcEntity getTrafficValue() {
    return trafficValue;
  }

  public void setTrafficValue(TrafficValueEcEntity trafficValue) {
    this.trafficValue = trafficValue;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
