package msf.fc.rest.ec.node.nodes.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class NodeInternalLinkDeleteEcEntity {

  
  @SerializedName("internal_link_if")
  private NodeInternalLinkIfDeleteEcEntity internalLinkIfDelete;

  
  public NodeInternalLinkIfDeleteEcEntity getInternalLinkIfDelete() {
    return internalLinkIfDelete;
  }

  
  public void setInternalLinkIfDelete(NodeInternalLinkIfDeleteEcEntity internalLinkIfDelete) {
    this.internalLinkIfDelete = internalLinkIfDelete;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
