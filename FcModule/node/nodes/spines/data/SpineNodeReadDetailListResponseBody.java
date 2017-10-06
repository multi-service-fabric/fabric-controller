package msf.fc.node.nodes.spines.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.nodes.spines.data.entity.SpineEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class SpineNodeReadDetailListResponseBody extends AbstractResponseBody {

  @SerializedName("spines")
  private List<SpineEntity> spineList;

  public List<SpineEntity> getSpineList() {
    return spineList;
  }

  public void setSpineList(List<SpineEntity> spineList) {
    this.spineList = spineList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
