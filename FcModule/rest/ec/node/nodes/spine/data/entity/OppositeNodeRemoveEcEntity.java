package msf.fc.rest.ec.node.nodes.spine.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OppositeNodeRemoveEcEntity {

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("internal_link_ifs")
  private InternalLinkIfRemoveEcEntity internalLinkIf;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public InternalLinkIfRemoveEcEntity getInternalLinkIf() {
    return internalLinkIf;
  }

  public void setInternalLinkIf(InternalLinkIfRemoveEcEntity internalLinkIf) {
    this.internalLinkIf = internalLinkIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
