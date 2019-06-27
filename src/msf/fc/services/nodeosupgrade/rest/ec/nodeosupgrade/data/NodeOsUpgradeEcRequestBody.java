
package msf.fc.services.nodeosupgrade.rest.ec.nodeosupgrade.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.services.nodeosupgrade.rest.ec.nodeosupgrade.data.entity.NodeOsUpgradeEcEntity;
import msf.fc.services.nodeosupgrade.rest.ec.nodeosupgrade.data.entity.NodeOsUpgradeEquipmentEcEntity;

public class NodeOsUpgradeEcRequestBody {

  @SerializedName("equipment")
  private NodeOsUpgradeEquipmentEcEntity equipment;

  @SerializedName("node")
  private NodeOsUpgradeEcEntity node;

  public NodeOsUpgradeEquipmentEcEntity getEquipment() {
    return equipment;
  }

  public void setEquipment(NodeOsUpgradeEquipmentEcEntity equipment) {
    this.equipment = equipment;
  }

  public NodeOsUpgradeEcEntity getNode() {
    return node;
  }

  public void setNode(NodeOsUpgradeEcEntity node) {
    this.node = node;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
