
package msf.mfcfc.node.nodes.spines.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SpineNodeBreakoutEntity {

  @SerializedName("local")
  private SpineNodeLocalEntity local;

  @SerializedName("opposite")
  private List<SpineNodeOppositeEntity> oppositeList;

  public SpineNodeLocalEntity getLocal() {
    return local;
  }

  public void setLocal(SpineNodeLocalEntity local) {
    this.local = local;
  }

  public List<SpineNodeOppositeEntity> getOppositeList() {
    return oppositeList;
  }

  public void setOppositeList(List<SpineNodeOppositeEntity> oppositeList) {
    this.oppositeList = oppositeList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
