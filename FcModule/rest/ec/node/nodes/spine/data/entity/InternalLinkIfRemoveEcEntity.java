package msf.fc.rest.ec.node.nodes.spine.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class InternalLinkIfRemoveEcEntity {

  @SerializedName("internal_link_if_id")
  private String internalLinkIfId;

  @SerializedName("lag_if")
  private LagIfRemoveEcEntity lagIf;

  public String getInternalLinkIfId() {
    return internalLinkIfId;
  }

  public void setInternalLinkIfId(String internalLinkIfId) {
    this.internalLinkIfId = internalLinkIfId;
  }

  public LagIfRemoveEcEntity getLagIf() {
    return lagIf;
  }

  public void setLagIf(LagIfRemoveEcEntity lagIf) {
    this.lagIf = lagIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
