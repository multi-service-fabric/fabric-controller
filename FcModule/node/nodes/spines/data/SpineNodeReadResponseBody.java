package msf.fc.node.nodes.spines.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.nodes.spines.data.entity.SpineEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class SpineNodeReadResponseBody extends AbstractResponseBody {

  @SerializedName("spine")
  private SpineEntity spine;

  public SpineEntity getSpine() {
    return spine;
  }

  public void setSpine(SpineEntity spine) {
    this.spine = spine;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
