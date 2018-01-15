package msf.fc.rest.ec.node.nodes.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class NodeEquipmentEcEntity {

  
  @SerializedName("equipment_type_id")
  private String equipmentTypeId;

  
  public String getEquipmentTypeId() {
    return equipmentTypeId;
  }

  
  public void setEquipmentTypeId(String equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
