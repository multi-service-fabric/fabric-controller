
package msf.mfcfc.node.nodes.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class NodeOppositeNodeEntity {

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("breakout_base_ifs")
  private List<NodeBreakoutBaseIfEntity> breakoutBaseIfList;

  @SerializedName("internal_link_if")
  private NodeInternalLinkIfInnerEntity internalLinkIf;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public List<NodeBreakoutBaseIfEntity> getBreakoutBaseIfList() {
    return breakoutBaseIfList;
  }

  public void setBreakoutBaseIfList(List<NodeBreakoutBaseIfEntity> breakoutBaseIfList) {
    this.breakoutBaseIfList = breakoutBaseIfList;
  }

  public NodeInternalLinkIfInnerEntity getInternalLinkIf() {
    return internalLinkIf;
  }

  public void setInternalLinkIf(NodeInternalLinkIfInnerEntity internalLinkIf) {
    this.internalLinkIf = internalLinkIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
