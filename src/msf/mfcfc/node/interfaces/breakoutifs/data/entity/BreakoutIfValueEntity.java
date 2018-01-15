package msf.mfcfc.node.interfaces.breakoutifs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class BreakoutIfValueEntity {
  
  @SerializedName("base_if")
  private BreakoutIfBaseIfEntity baseIf;

  
  @SerializedName("division_number")
  private Integer divisionNumber;

  
  @SerializedName("breakout_if_speed")
  private String breakoutIfSpeed;

  
  public BreakoutIfBaseIfEntity getBaseIf() {
    return baseIf;
  }

  
  public void setBaseIf(BreakoutIfBaseIfEntity baseIf) {
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
