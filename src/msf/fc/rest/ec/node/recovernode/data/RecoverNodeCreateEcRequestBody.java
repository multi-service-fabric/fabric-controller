
package msf.fc.rest.ec.node.recovernode.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.recovernode.data.entity.RecoverEquipmentEcEntity;
import msf.fc.rest.ec.node.recovernode.data.entity.RecoverNodeEcEntity;

public class RecoverNodeCreateEcRequestBody {

  @SerializedName("equipment")
  private RecoverEquipmentEcEntity equipment;

  @SerializedName("node")
  private RecoverNodeEcEntity node;

  public RecoverEquipmentEcEntity getEquipment() {
    return equipment;
  }

  public void setEquipment(RecoverEquipmentEcEntity equipment) {
    this.equipment = equipment;
  }

  public RecoverNodeEcEntity getNode() {
    return node;
  }

  public void setNode(RecoverNodeEcEntity node) {
    this.node = node;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
