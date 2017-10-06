package msf.fc.rest.ec.node.equipment.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentIfEcEntity {

  @SerializedName("physical_if_id")
  private String physicalIfId;

  @SerializedName("if_slot")
  private String ifSlot;

  public String getPhysicalIfId() {
    return physicalIfId;
  }

  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  public String getIfSlot() {
    return ifSlot;
  }

  public void setIfSlot(String ifSlot) {
    this.ifSlot = ifSlot;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
