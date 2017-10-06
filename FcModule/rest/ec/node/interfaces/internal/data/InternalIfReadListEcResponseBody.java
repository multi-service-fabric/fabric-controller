package msf.fc.rest.ec.node.interfaces.internal.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractInternalResponseBody;
import msf.fc.rest.ec.node.interfaces.internal.data.entity.InternalLinkIfEcEntity;

public class InternalIfReadListEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("internal_link_ifs")
  private List<InternalLinkIfEcEntity> internalLinkIf;

  public List<InternalLinkIfEcEntity> getInternalLinkIf() {
    return internalLinkIf;
  }

  public void setInternalLinkIf(List<InternalLinkIfEcEntity> internalLinkIf) {
    this.internalLinkIf = internalLinkIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
