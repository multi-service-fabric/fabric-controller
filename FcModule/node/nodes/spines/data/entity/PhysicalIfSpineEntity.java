package msf.fc.node.nodes.spines.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class PhysicalIfSpineEntity {

  @SerializedName("physical_if_id")
  private String physicalIfId;

  @SerializedName("opposite_if")
  private OppositePhysicalIfSpineEntity oppositeIf;

  @SerializedName("speed")
  private String speed;

  public String getPhysicalIfId() {
    return physicalIfId;
  }

  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  public OppositePhysicalIfSpineEntity getOppositeIf() {
    return oppositeIf;
  }

  public void setOppositeIf(OppositePhysicalIfSpineEntity oppositeIf) {
    this.oppositeIf = oppositeIf;
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
