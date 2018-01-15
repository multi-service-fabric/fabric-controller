package msf.mfcfc.node.interfaces.physicalifs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class PhysicalIfEntity {

  
  @SerializedName("physical_if_id")
  private String physicalIfId;

  
  @SerializedName("opposite_if")
  private PhysicalIfOppositeIfEntity oppositeIf;

  
  @SerializedName("speed")
  private String speed;

  
  @SerializedName("if_name")
  private String ifName;

  
  @SerializedName("qos")
  private PhysicalIfQosEntity qos;

  
  @SerializedName("breakout")
  private PhysicalIfBreakoutEntity breakout;

  
  public String getPhysicalIfId() {
    return physicalIfId;
  }

  
  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  
  public PhysicalIfOppositeIfEntity getOppositeIf() {
    return oppositeIf;
  }

  
  public void setOppositeIf(PhysicalIfOppositeIfEntity oppositeIf) {
    this.oppositeIf = oppositeIf;
  }

  
  public String getSpeed() {
    return speed;
  }

  
  public void setSpeed(String speed) {
    this.speed = speed;
  }

  
  public String getIfName() {
    return ifName;
  }

  
  public void setIfName(String ifName) {
    this.ifName = ifName;
  }

  
  public PhysicalIfQosEntity getQos() {
    return qos;
  }

  
  public void setQos(PhysicalIfQosEntity qos) {
    this.qos = qos;
  }

  
  public PhysicalIfBreakoutEntity getBreakout() {
    return breakout;
  }

  
  public void setBreakout(PhysicalIfBreakoutEntity breakout) {
    this.breakout = breakout;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
