package msf.mfcfc.node.nodes.leafs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class LeafNodePhysicalIfForUserEntity {
  
  @SerializedName("physical_if_id")
  private String physicalIfId;

  
  @SerializedName("speed")
  private String speed;

  
  public String getPhysicalIfId() {
    return physicalIfId;
  }

  
  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
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
