package msf.fc.rest.ec.node.equipment.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.equipment.data.entity.EquipmentEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;


public class EquipmentReadEcResponseBody extends AbstractInternalResponseBody {

  
  @SerializedName("equipment")
  private EquipmentEcEntity equipment;

  
  public EquipmentEcEntity getEquipment() {
    return equipment;
  }

  
  public void setEquipment(EquipmentEcEntity equipment) {
    this.equipment = equipment;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
