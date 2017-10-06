package msf.fc.rest.ec.node.nodes.spine.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractInternalResponseBody;
import msf.fc.rest.ec.node.nodes.spine.data.entity.SpineEcEntity;

public class SpineNodeReadEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("spine")
  private SpineEcEntity spine;

  public SpineEcEntity getSpine() {
    return spine;
  }

  public void setSpine(SpineEcEntity spine) {
    this.spine = spine;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
