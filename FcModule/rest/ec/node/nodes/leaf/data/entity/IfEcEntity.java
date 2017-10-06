package msf.fc.rest.ec.node.nodes.leaf.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class IfEcEntity {

  @SerializedName("internal_link_ifs")
  private List<InternalLinkIfExpansionEcEntity> internalLinkIfList;

  public List<InternalLinkIfExpansionEcEntity> getInternalLinkIfList() {
    return internalLinkIfList;
  }

  public void setInternalLinkIfList(List<InternalLinkIfExpansionEcEntity> internalLinkIfList) {
    this.internalLinkIfList = internalLinkIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
