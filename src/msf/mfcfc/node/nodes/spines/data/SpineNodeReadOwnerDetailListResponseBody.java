
package msf.mfcfc.node.nodes.spines.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;


public class SpineNodeReadOwnerDetailListResponseBody extends AbstractResponseBody {

  
  @SerializedName("spines")
  private List<SpineNodeEntity> spineList;

  
  public List<SpineNodeEntity> getSpineList() {
    return spineList;
  }

  
  public void setSpineList(List<SpineNodeEntity> spineList) {
    this.spineList = spineList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
