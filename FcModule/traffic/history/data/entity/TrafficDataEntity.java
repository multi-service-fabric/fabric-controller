package msf.fc.traffic.history.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class TrafficDataEntity {
  @SerializedName("start_cluster_id")
  private String startClusterId;

  @SerializedName("start_leaf_node_id")
  private String startLeafNodeId;

  @SerializedName("start_edge_point_id")
  private String startEdgePointId;

  @SerializedName("start_cp_id")
  private String startCpId;

  @SerializedName("end_cluster_id")
  private String endClusterId;

  @SerializedName("end_leaf_node_id")
  private String endLeafNodeId;

  @SerializedName("end_edge_point_id")
  private String endEdgePointId;

  @SerializedName("end_cp_id")
  private String endCpId;

  private String time;

  private Float value;

  public String getStartClusterId() {
    return startClusterId;
  }

  public void setStartClusterId(String startClusterId) {
    this.startClusterId = startClusterId;
  }

  public String getStartLeafNodeId() {
    return startLeafNodeId;
  }

  public void setStartLeafNodeId(String startLeafNodeId) {
    this.startLeafNodeId = startLeafNodeId;
  }

  public String getStartEdgePointId() {
    return startEdgePointId;
  }

  public void setStartEdgePointId(String startEdgePointId) {
    this.startEdgePointId = startEdgePointId;
  }

  public String getStartCpId() {
    return startCpId;
  }

  public void setStartCpId(String startCpId) {
    this.startCpId = startCpId;
  }

  public String getEndClusterId() {
    return endClusterId;
  }

  public void setEndClusterId(String endClusterId) {
    this.endClusterId = endClusterId;
  }

  public String getEndLeafNodeId() {
    return endLeafNodeId;
  }

  public void setEndLeafNodeId(String endLeafNodeId) {
    this.endLeafNodeId = endLeafNodeId;
  }

  public String getEndEdgePointId() {
    return endEdgePointId;
  }

  public void setEndEdgePointId(String endEdgePointId) {
    this.endEdgePointId = endEdgePointId;
  }

  public String getEndCpId() {
    return endCpId;
  }

  public void setEndCpId(String endCpId) {
    this.endCpId = endCpId;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public Float getValue() {
    return value;
  }

  public void setValue(Float value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
