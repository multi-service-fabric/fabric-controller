
package msf.mfcfc.node.nodes.spines.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SpineNodePhysicalIfEntity {

  @SerializedName("physical_if_id")
  private String physicalIfId;

  @SerializedName("internal_options")
  private SpineNodeInternalOptionEntity internalOptions;

  @SerializedName("speed")
  private String speed;

  public String getPhysicalIfId() {
    return physicalIfId;
  }

  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  public SpineNodeInternalOptionEntity getInternalOptions() {
    return internalOptions;
  }

  public void setInternalOptions(SpineNodeInternalOptionEntity internalOptions) {
    this.internalOptions = internalOptions;
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
