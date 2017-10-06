package msf.fc.node.interfaces.internalifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.interfaces.internalifs.data.entity.InternalIfEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class InternalIfReadResponseBody extends AbstractResponseBody {

  @SerializedName("internal_link_if")
  private InternalIfEntity internalLinkIf;

  public InternalIfEntity getInternalIf() {
    return internalLinkIf;
  }

  public void setInternalIf(InternalIfEntity internalLinkIf) {
    this.internalLinkIf = internalLinkIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
