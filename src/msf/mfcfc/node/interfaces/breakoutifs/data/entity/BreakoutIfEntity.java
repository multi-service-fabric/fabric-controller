
package msf.mfcfc.node.interfaces.breakoutifs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class BreakoutIfEntity {

  @SerializedName("breakout_if_id")
  private String breakoutIfId;

  @SerializedName("speed")
  private String speed;

  @SerializedName("if_name")
  private String ifName;

  @SerializedName("base_if")
  private BreakoutIfBaseIfEntity baseIf;

  @SerializedName("qos")
  private BreakoutIfQosEntity qos;

  public String getBreakoutIfId() {
    return breakoutIfId;
  }

  public void setBreakoutIfId(String breakoutIfId) {
    this.breakoutIfId = breakoutIfId;
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

  public BreakoutIfBaseIfEntity getBaseIf() {
    return baseIf;
  }

  public void setBaseIf(BreakoutIfBaseIfEntity baseIf) {
    this.baseIf = baseIf;
  }

  public BreakoutIfQosEntity getQos() {
    return qos;
  }

  public void setQos(BreakoutIfQosEntity qos) {
    this.qos = qos;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
