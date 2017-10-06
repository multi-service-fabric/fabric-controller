package msf.fc.rest.ec.node.nodes.leaf.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OppositeNodeExpansionEcEntity {

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("internal_link_if")
  private InternalLinkIfExpansionEcEntity internalLinkIf;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public InternalLinkIfExpansionEcEntity getInternalLinkIf() {
    return internalLinkIf;
  }

  public void setInternalLinkIf(InternalLinkIfExpansionEcEntity internalLinkIf) {
    this.internalLinkIf = internalLinkIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
