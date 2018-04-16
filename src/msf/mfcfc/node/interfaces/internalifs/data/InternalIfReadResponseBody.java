
package msf.mfcfc.node.interfaces.internalifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.interfaces.internalifs.data.entity.InternalLinkIfEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class InternalIfReadResponseBody extends AbstractResponseBody {

  @SerializedName("internal_link_if")
  private InternalLinkIfEntity internalLinkIf;

  public InternalLinkIfEntity getInternalLinkIf() {
    return internalLinkIf;
  }

  public void setInternalLinkIf(InternalLinkIfEntity internalLinkIf) {
    this.internalLinkIf = internalLinkIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
