package msf.fc.failure.logicalif.data.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class LogicalIfStatusData {
  @SerializedName("cluster_id")
  private String clusterId;
  @SerializedName("nodes")
  private List<LogicalIfStatusNodeData> nodes;
  @SerializedName("slices")
  private LogicalIfStatusSliceData slices;

  public LogicalIfStatusData() {
  }

  public LogicalIfStatusData(String clusterId, List<LogicalIfStatusNodeData> nodes, LogicalIfStatusSliceData slices) {
    this.clusterId = clusterId;
    this.nodes = nodes;
    this.slices = slices;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public List<LogicalIfStatusNodeData> getNodes() {
    return nodes;
  }

  public void setNodes(List<LogicalIfStatusNodeData> nodes) {
    this.nodes = nodes;
  }

  public LogicalIfStatusSliceData getSlices() {
    return slices;
  }

  public void setSlices(LogicalIfStatusSliceData slices) {
    this.slices = slices;
  }

  @Override
  public String toString() {
    return "LogicalIfStatusData [clusterId=" + clusterId + ", nodes=" + nodes + ", slices=" + slices + "]";
  }

}
