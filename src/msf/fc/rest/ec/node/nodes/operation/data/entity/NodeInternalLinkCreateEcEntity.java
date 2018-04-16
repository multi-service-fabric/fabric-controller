
package msf.fc.rest.ec.node.nodes.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class NodeInternalLinkCreateEcEntity {

  @SerializedName("internal_link_if")
  private NodeInternalLinkIfCreateEcEntity internalLinkIfCreate;

  public NodeInternalLinkIfCreateEcEntity getInternalLinkIfCreate() {
    return internalLinkIfCreate;
  }

  public void setInternalLinkIfCreate(NodeInternalLinkIfCreateEcEntity internalLinkIfCreate) {
    this.internalLinkIfCreate = internalLinkIfCreate;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
