package msf.mfcfc.node.nodes.spines.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;


public class SpineNodeReadOwnerResponseBody extends AbstractResponseBody {
  
  @SerializedName("spine")
  private SpineNodeEntity spine;

  
  public SpineNodeEntity getSpine() {
    return spine;
  }

  
  public void setSpine(SpineNodeEntity spine) {
    this.spine = spine;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
