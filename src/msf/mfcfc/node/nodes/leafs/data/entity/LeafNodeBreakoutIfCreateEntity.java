
package msf.mfcfc.node.nodes.leafs.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class LeafNodeBreakoutIfCreateEntity {

  
  @SerializedName("breakout_if_ids")
  private List<String> breakoutIfIdList;
  
  @SerializedName("base_if")
  private LeafNodeBaseIfEntity baseIf;

  
  @SerializedName("division_number")
  private Integer divisionNumber;

  
  @SerializedName("breakout_if_speed")
  private String breakoutIfSpeed;

  
  public List<String> getBreakoutIfIdList() {
    return breakoutIfIdList;
  }

  
  public void setBreakoutIfIdList(List<String> breakoutIfIdList) {
    this.breakoutIfIdList = breakoutIfIdList;
  }

  
  public LeafNodeBaseIfEntity getBaseIf() {
    return baseIf;
  }

  
  public void setBaseIf(LeafNodeBaseIfEntity baseIf) {
    this.baseIf = baseIf;
  }

  
  public Integer getDivisionNumber() {
    return divisionNumber;
  }

  
  public void setDivisionNumber(Integer divisionNumber) {
    this.divisionNumber = divisionNumber;
  }

  
  public String getBreakoutIfSpeed() {
    return breakoutIfSpeed;
  }

  
  public void setBreakoutIfSpeed(String breakoutIfSpeed) {
    this.breakoutIfSpeed = breakoutIfSpeed;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
