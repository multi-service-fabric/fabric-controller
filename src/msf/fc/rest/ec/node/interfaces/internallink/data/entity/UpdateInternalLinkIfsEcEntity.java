
package msf.fc.rest.ec.node.interfaces.internallink.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class UpdateInternalLinkIfsEcEntity {

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("internal_link_if")
  private InternalLinkIfEcEntity internalLinkIf;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public InternalLinkIfEcEntity getInternalLinkIf() {
    return internalLinkIf;
  }

  public void setInternalLinkIf(InternalLinkIfEcEntity internalLinkIf) {
    this.internalLinkIf = internalLinkIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
