package msf.mfcfc.node.equipments.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class EquipmentVpnEntity {

  
  @SerializedName("l2")
  private Boolean l2;

  
  @SerializedName("l3")
  private Boolean l3;

  
  public Boolean getL2() {
    return l2;
  }

  
  public void setL2(Boolean l2) {
    this.l2 = l2;
  }

  
  public Boolean getL3() {
    return l3;
  }

  
  public void setL3(Boolean l3) {
    this.l3 = l3;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
