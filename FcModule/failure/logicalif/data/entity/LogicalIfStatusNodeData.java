package msf.fc.failure.logicalif.data.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class LogicalIfStatusNodeData {

  @SerializedName("node_type")
  private String nodeType;
  @SerializedName("node_id")
  private String nodeId;
  @SerializedName("internal_link_ifs")
  private List<LogicalIfStatusInternalLinkIfData> internalLinkIfs;

  public LogicalIfStatusNodeData() {

  }

  public LogicalIfStatusNodeData(String nodeType, String nodeId,
      List<LogicalIfStatusInternalLinkIfData> internalLinkIfs) {
    this.nodeType = nodeType;
    this.nodeId = nodeId;
    this.internalLinkIfs = internalLinkIfs;
  }

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

  public List<LogicalIfStatusInternalLinkIfData> getInternalLinkIfs() {
    return internalLinkIfs;
  }

  public void setInternalLinkIfs(List<LogicalIfStatusInternalLinkIfData> internalLinkIfs) {
    this.internalLinkIfs = internalLinkIfs;
  }

  @Override
  public String toString() {
    return "LogicalIfStatusNodeData [nodeType=" + nodeType + ", nodeId=" + nodeId + ", internalLinkIfs="
        + internalLinkIfs + "]";
  }

}
