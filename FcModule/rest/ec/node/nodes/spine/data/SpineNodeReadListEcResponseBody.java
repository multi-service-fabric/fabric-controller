package msf.fc.rest.ec.node.nodes.spine.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractInternalResponseBody;
import msf.fc.rest.ec.node.nodes.spine.data.entity.SpineEcEntity;

public class SpineNodeReadListEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("spines")
  private List<SpineEcEntity> spineList;

  public List<SpineEcEntity> getSpineList() {
    return spineList;
  }

  public void setSpineList(List<SpineEcEntity> spineList) {
    this.spineList = spineList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
