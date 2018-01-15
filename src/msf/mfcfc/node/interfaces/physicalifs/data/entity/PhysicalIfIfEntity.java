package msf.mfcfc.node.interfaces.physicalifs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class PhysicalIfIfEntity {
  
  @SerializedName("physical_if_id")
  private String physicalIfId;

  
  @SerializedName("if_name")
  private String ifName;

  
  public String getPhysicalIfId() {
    return physicalIfId;
  }

  
  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  
  public String getIfName() {
    return ifName;
  }

  
  public void setIfName(String ifName) {
    this.ifName = ifName;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
