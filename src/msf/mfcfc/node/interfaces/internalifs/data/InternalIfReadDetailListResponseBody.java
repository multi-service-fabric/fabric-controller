package msf.mfcfc.node.interfaces.internalifs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.interfaces.internalifs.data.entity.InternalLinkIfEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;


public class InternalIfReadDetailListResponseBody extends AbstractResponseBody {

  
  @SerializedName("internal_link_ifs")
  private List<InternalLinkIfEntity> internalLinkIfList;

  
  public List<InternalLinkIfEntity> getInternalLinkIfList() {
    return internalLinkIfList;
  }

  
  public void setInternalLinkIfList(List<InternalLinkIfEntity> internalLinkIfList) {
    this.internalLinkIfList = internalLinkIfList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
