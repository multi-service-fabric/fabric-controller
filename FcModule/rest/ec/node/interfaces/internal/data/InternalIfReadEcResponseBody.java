package msf.fc.rest.ec.node.interfaces.internal.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractInternalResponseBody;
import msf.fc.rest.ec.node.interfaces.internal.data.entity.InternalLinkIfEcEntity;

public class InternalIfReadEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("internal_link_if")
  private InternalLinkIfEcEntity internalLinkIf;

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
