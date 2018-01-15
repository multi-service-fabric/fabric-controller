package msf.fc.rest.ec.node.equipment.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class EquipmentShapingEcEntity {

  
  @SerializedName("enable")
  private Boolean enable;

  
  public Boolean getEnable() {
    return enable;
  }

  
  public void setEnable(Boolean enable) {
    this.enable = enable;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
