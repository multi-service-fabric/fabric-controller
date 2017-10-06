package msf.fc.rest.ec.node.nodes.spine.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EmEquipmentEcEntity {

  @SerializedName("equipment_type_id")
  private String equipmentTypeId;

  @SerializedName("platform")
  private String platform;

  @SerializedName("os")
  private String os;

  @SerializedName("firmware")
  private String firmware;

  public String getEquipmentTypeId() {
    return equipmentTypeId;
  }

  public void setEquipmentTypeId(String equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getOs() {
    return os;
  }

  public void setOs(String os) {
    this.os = os;
  }

  public String getFirmware() {
    return firmware;
  }

  public void setFirmware(String firmware) {
    this.firmware = firmware;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
