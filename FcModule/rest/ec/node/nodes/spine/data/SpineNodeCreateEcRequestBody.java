package msf.fc.rest.ec.node.nodes.spine.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.node.nodes.spine.data.entity.EquipmentEcEntity;
import msf.fc.rest.ec.node.nodes.spine.data.entity.NodeEcEntity;

public class SpineNodeCreateEcRequestBody {

  @SerializedName("equipment")
  private EquipmentEcEntity equipment;

  @SerializedName("node")
  private NodeEcEntity node;

  public EquipmentEcEntity getEquipment() {
    return equipment;
  }

  public void setEquipment(EquipmentEcEntity equipment) {
    this.equipment = equipment;
  }

  public NodeEcEntity getNode() {
    return node;
  }

  public void setNode(NodeEcEntity node) {
    this.node = node;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
