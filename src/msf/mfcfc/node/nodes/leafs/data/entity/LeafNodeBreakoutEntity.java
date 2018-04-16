
package msf.mfcfc.node.nodes.leafs.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LeafNodeBreakoutEntity {

  @SerializedName("local")
  private LeafNodeLocalEntity local;

  @SerializedName("opposite")
  private List<LeafNodeOppositeEntity> oppositeList;

  public LeafNodeLocalEntity getLocal() {
    return local;
  }

  public void setLocal(LeafNodeLocalEntity local) {
    this.local = local;
  }

  public List<LeafNodeOppositeEntity> getOppositeList() {
    return oppositeList;
  }

  public void setOppositeList(List<LeafNodeOppositeEntity> oppositeList) {
    this.oppositeList = oppositeList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
