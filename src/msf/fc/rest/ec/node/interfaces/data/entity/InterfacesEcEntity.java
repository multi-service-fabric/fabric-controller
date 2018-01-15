package msf.fc.rest.ec.node.interfaces.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.interfaces.breakout.data.entity.BreakoutIfEcEntity;
import msf.fc.rest.ec.node.interfaces.lag.data.entity.LagIfEcEntity;
import msf.fc.rest.ec.node.interfaces.physical.data.entity.PhysicalIfEcEntity;


public class InterfacesEcEntity {

  
  @SerializedName("physical_ifs")
  private List<PhysicalIfEcEntity> physicalIfList;

  
  @SerializedName("lag_ifs")
  private List<LagIfEcEntity> lagIfList;

  
  @SerializedName("breakout_ifs")
  private List<BreakoutIfEcEntity> breakoutIfList;

  
  public List<PhysicalIfEcEntity> getPhysicalIfList() {
    return physicalIfList;
  }

  
  public void setPhysicalIfList(List<PhysicalIfEcEntity> physicalIfList) {
    this.physicalIfList = physicalIfList;
  }

  
  public List<LagIfEcEntity> getLagIfList() {
    return lagIfList;
  }

  
  public void setLagIfList(List<LagIfEcEntity> lagIfList) {
    this.lagIfList = lagIfList;
  }

  
  public List<BreakoutIfEcEntity> getBreakoutIfList() {
    return breakoutIfList;
  }

  
  public void setBreakoutIfList(List<BreakoutIfEcEntity> breakoutIfList) {
    this.breakoutIfList = breakoutIfList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
