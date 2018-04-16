
package msf.mfcfc.node.nodes.spines.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SpineNodePhysicalIfCreateEntity {

  @SerializedName("physical_if_id")
  private String physicalIfId;

  @SerializedName("physical_if_speed")
  private String physicalIfSpeed;

  public String getPhysicalIfId() {
    return physicalIfId;
  }

  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  public String getPhysicalIfSpeed() {
    return physicalIfSpeed;
  }

  public void setPhysicalIfSpeed(String physicalIfSpeed) {
    this.physicalIfSpeed = physicalIfSpeed;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
