package msf.mfcfc.node.nodes.spines.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class SpineNodeInternalOppositeEntity {

  
  @SerializedName("physical_if")
  private SpineNodePhysicalIfCreateEntity physicalIf;

  
  @SerializedName("breakout_if")
  private SpineNodeInternalBreakoutIfEntity breakoutIf;

  
  public SpineNodePhysicalIfCreateEntity getPhysicalIf() {
    return physicalIf;
  }

  
  public void setPhysicalIf(SpineNodePhysicalIfCreateEntity physicalIf) {
    this.physicalIf = physicalIf;
  }

  
  public SpineNodeInternalBreakoutIfEntity getBreakoutIf() {
    return breakoutIf;
  }

  
  public void setBreakoutIf(SpineNodeInternalBreakoutIfEntity breakoutIf) {
    this.breakoutIf = breakoutIf;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
