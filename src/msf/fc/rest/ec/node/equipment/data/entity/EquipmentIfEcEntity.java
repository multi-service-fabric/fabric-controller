
package msf.fc.rest.ec.node.equipment.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentIfEcEntity {

  @SerializedName("physical_if_id")
  private String physicalIfId;

  @SerializedName("if_slot")
  private String ifSlot;

  @SerializedName("port_speed_type")
  private List<String> portSpeedTypeList;

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

  public List<String> getPortSpeedTypeList() {
    return portSpeedTypeList;
  }

  public void setPortSpeedTypeList(List<String> portSpeedTypeList) {
    this.portSpeedTypeList = portSpeedTypeList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
