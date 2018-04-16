
package msf.mfcfc.node.nodes.leafs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LeafNodeInternalOppositeEntity {

  @SerializedName("physical_if")
  private LeafNodePhysicalIfCreateEntity physicalIf;

  @SerializedName("breakout_if")
  private LeafNodeInternalBreakoutIfEntity breakoutIf;

  public LeafNodePhysicalIfCreateEntity getPhysicalIf() {
    return physicalIf;
  }

  public void setPhysicalIf(LeafNodePhysicalIfCreateEntity physicalIf) {
    this.physicalIf = physicalIf;
  }

  public LeafNodeInternalBreakoutIfEntity getBreakoutIf() {
    return breakoutIf;
  }

  public void setBreakoutIf(LeafNodeInternalBreakoutIfEntity breakoutIf) {
    this.breakoutIf = breakoutIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
