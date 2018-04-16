
package msf.mfcfc.node.nodes.leafs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LeafNodeMemberIfEntity {

  @SerializedName("local")
  private LeafNodeInternalLocalEntity local;

  @SerializedName("opposite")
  private LeafNodeInternalOppositeEntity opposite;

  public LeafNodeInternalLocalEntity getLocal() {
    return local;
  }

  public void setLocal(LeafNodeInternalLocalEntity local) {
    this.local = local;
  }

  public LeafNodeInternalOppositeEntity getOpposite() {
    return opposite;
  }

  public void setOpposite(LeafNodeInternalOppositeEntity opposite) {
    this.opposite = opposite;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
