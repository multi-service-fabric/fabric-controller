package msf.fc.node.interfaces.internalifs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractResponseBody;

public class InternalIfReadListResponseBody extends AbstractResponseBody {

  @SerializedName("internal_link_if_ids")
  private List<String> internalLinkIfIdList;

  public List<String> getInternalIfIdList() {
    return internalLinkIfIdList;
  }

  public void setInternalIfIdList(List<String> internalLinkIfIdList) {
    this.internalLinkIfIdList = internalLinkIfIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
