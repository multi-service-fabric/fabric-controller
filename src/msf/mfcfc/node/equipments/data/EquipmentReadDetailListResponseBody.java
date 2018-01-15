package msf.mfcfc.node.equipments.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.equipments.data.entity.EquipmentTypeEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;


public class EquipmentReadDetailListResponseBody extends AbstractResponseBody {

  
  @SerializedName("equipment_types")
  private List<EquipmentTypeEntity> equipmentTypeList;

  
  public List<EquipmentTypeEntity> getEquipmentTypeList() {
    return equipmentTypeList;
  }

  
  public void setEquipmentTypeList(List<EquipmentTypeEntity> equipmentTypeList) {
    this.equipmentTypeList = equipmentTypeList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
