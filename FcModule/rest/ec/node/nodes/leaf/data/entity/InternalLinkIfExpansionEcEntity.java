package msf.fc.rest.ec.node.nodes.leaf.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class InternalLinkIfExpansionEcEntity {
  @SerializedName("internal_link_if_id")
  private String internalLinkIfId;
  @SerializedName("lag_if")
  private LagIfExpansionEcEntity lagIf;

  public String getInternalLinkIfId() {
    return internalLinkIfId;
  }

  public void setInternalLinkIfId(String internalLinkIfId) {
    this.internalLinkIfId = internalLinkIfId;
  }

  public LagIfExpansionEcEntity getLagIf() {
    return lagIf;
  }

  public void setLagIf(LagIfExpansionEcEntity lagIf) {
    this.lagIf = lagIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
