
package msf.fc.rest.ec.node.interfaces.internallink.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.internallink.data.entity.UpdateInternalLinkIfsEcEntity;

public class InternalLinkIfUpdateEcRequestBody {

  @SerializedName("update_internal_link_ifs")
  private List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfList;

  public List<UpdateInternalLinkIfsEcEntity> getUpdateInternalLinkIfList() {
    return updateInternalLinkIfList;
  }

  public void setUpdateInternalLinkIfList(List<UpdateInternalLinkIfsEcEntity> updateInternalLinkIfList) {
    this.updateInternalLinkIfList = updateInternalLinkIfList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
