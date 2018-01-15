package msf.mfcfc.node.nodes.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class NodeInternalLinkIfEntity {

  
  @SerializedName("internal_link_if")
  private NodeInternalLinkIfInnerEntity internalLinkIf;

  
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
