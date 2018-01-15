package msf.mfcfc.node.equipments.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.rest.common.AbstractResponseBody;


public class EquipmentReadListResponseBody extends AbstractResponseBody {

  
  @SerializedName("equipment_type_ids")
  private List<String> equipmentTypeIdList;

  
  public List<String> getEquipmentTypeIdList() {
    return equipmentTypeIdList;
  }

  
  public void setEquipmentTypeIdList(List<String> equipmentTypeIdList) {
    this.equipmentTypeIdList = equipmentTypeIdList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
