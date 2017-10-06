package msf.fc.node.interfaces.internalifs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.interfaces.internalifs.data.entity.InternalIfEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class InternalIfReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("internal_link_ifs")
  private List<InternalIfEntity> internalLinkIfList;

  public List<InternalIfEntity> getInternalIfList() {
    return internalLinkIfList;
  }

  public void setInternalIfList(List<InternalIfEntity> internalLinkIfList) {
    this.internalLinkIfList = internalLinkIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
