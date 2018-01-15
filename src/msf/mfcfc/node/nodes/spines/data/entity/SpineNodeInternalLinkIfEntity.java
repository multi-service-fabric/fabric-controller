
package msf.mfcfc.node.nodes.spines.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class SpineNodeInternalLinkIfEntity {

  
  @SerializedName("local")
  private SpineNodeInternalLocalEntity local;

  
  @SerializedName("opposite")
  private SpineNodeInternalOppositeEntity opposite;

  
  public SpineNodeInternalLocalEntity getLocal() {
    return local;
  }

  
  public void setLocal(SpineNodeInternalLocalEntity local) {
    this.local = local;
  }

  
  public SpineNodeInternalOppositeEntity getOpposite() {
    return opposite;
  }

  
  public void setOpposite(SpineNodeInternalOppositeEntity opposite) {
    this.opposite = opposite;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
