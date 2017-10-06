package msf.fc.node.equipments.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.node.equipments.data.entity.EquipmentTypeEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class EquipmentReadResponseBody extends AbstractResponseBody {

  @SerializedName("equipment_type")
  private EquipmentTypeEntity equipmentType;

  public EquipmentTypeEntity getEquipmentType() {
    return equipmentType;
  }

  public void setEquipmentType(EquipmentTypeEntity equipmentType) {
    this.equipmentType = equipmentType;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
