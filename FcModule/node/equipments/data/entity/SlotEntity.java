package msf.fc.node.equipments.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SlotEntity {

  @SerializedName("if_id")
  private String ifId;

  @SerializedName("if_slot")
  private String ifSlot;

  @SerializedName("speed_capabilities")
  private List<String> speedCapabilityList;

  public String getIfId() {
    return ifId;
  }

  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  public String getIfSlot() {
    return ifSlot;
  }

  public void setIfSlot(String ifSlot) {
    this.ifSlot = ifSlot;
  }

  public List<String> getSpeedCapabilityList() {
    return speedCapabilityList;
  }

  public void setSpeedCapabilityList(List<String> speedCapabilityList) {
    this.speedCapabilityList = speedCapabilityList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
