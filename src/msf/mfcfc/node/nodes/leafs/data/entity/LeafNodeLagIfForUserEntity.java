
package msf.mfcfc.node.nodes.leafs.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LeafNodeLagIfForUserEntity {

  @SerializedName("lag_if_id")
  private String lagIfId;

  @SerializedName("speed")
  private String speed;

  @SerializedName("physical_if_ids")
  private List<String> physicalIfIdList;

  @SerializedName("breakout_if_ids")
  private List<String> breakoutIfIdList;

  public String getLagIfId() {
    return lagIfId;
  }

  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  public String getSpeed() {
    return speed;
  }

  public void setSpeed(String speed) {
    this.speed = speed;
  }

  public List<String> getPhysicalIfIdList() {
    return physicalIfIdList;
  }

  public void setPhysicalIfIdList(List<String> physicalIfIdList) {
    this.physicalIfIdList = physicalIfIdList;
  }

  public List<String> getBreakoutIfIdList() {
    return breakoutIfIdList;
  }

  public void setBreakoutIfIdList(List<String> breakoutIfIdList) {
    this.breakoutIfIdList = breakoutIfIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
