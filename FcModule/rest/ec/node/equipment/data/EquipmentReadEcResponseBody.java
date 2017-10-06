package msf.fc.rest.ec.node.equipment.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.rest.common.AbstractInternalResponseBody;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentEcEntity;

public class EquipmentReadEcResponseBody extends AbstractInternalResponseBody {

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
