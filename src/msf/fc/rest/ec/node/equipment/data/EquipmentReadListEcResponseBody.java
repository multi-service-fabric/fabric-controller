
package msf.fc.rest.ec.node.equipment.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.equipment.data.entity.EquipmentEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class EquipmentReadListEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("equipments")
  private List<EquipmentEcEntity> equipmentList;

  public List<EquipmentEcEntity> getEquipmentList() {
    return equipmentList;
  }

  public void setEquipmentList(List<EquipmentEcEntity> equipmentList) {
    this.equipmentList = equipmentList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
