package msf.fc.rest.ec.node.interfaces.lag.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class LagIfLagMemberEcEntity {

  
  @SerializedName("physical_ifs")
  private List<LagIfPhysicalIfEcEntity> physicalIfList;

  
  @SerializedName("breakout_ifs")
  private List<LagIfBreakoutIfEcEntity> breakoutIfList;

  
  public List<LagIfPhysicalIfEcEntity> getPhysicalIfList() {
    return physicalIfList;
  }

  
  public void setPhysicalIfList(List<LagIfPhysicalIfEcEntity> physicalIfList) {
    this.physicalIfList = physicalIfList;
  }

  
  public List<LagIfBreakoutIfEcEntity> getBreakoutIfList() {
    return breakoutIfList;
  }

  
  public void setBreakoutIfList(List<LagIfBreakoutIfEcEntity> breakoutIfList) {
    this.breakoutIfList = breakoutIfList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
