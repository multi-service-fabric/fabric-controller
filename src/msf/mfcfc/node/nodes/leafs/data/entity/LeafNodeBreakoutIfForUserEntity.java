
package msf.mfcfc.node.nodes.leafs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LeafNodeBreakoutIfForUserEntity {

  @SerializedName("breakout_if_id")
  private String breakoutIfId;

  @SerializedName("speed")
  private String speed;

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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
