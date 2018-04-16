
package msf.fc.rest.ec.node.nodes.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class NodeCreateEcEntity {

  @SerializedName("equipment")
  private NodeEquipmentEcEntity equipment;

  @SerializedName("create_node")
  private NodeCreateNodeEcEntity createNode;

  @SerializedName("update_node")
  private NodeCreateUpdateNodeEcEntity updateNode;

  public NodeEquipmentEcEntity getEquipment() {
    return equipment;
  }

  public void setEquipment(NodeEquipmentEcEntity equipment) {
    this.equipment = equipment;
  }

  public NodeCreateNodeEcEntity getCreateNode() {
    return createNode;
  }

  public void setCreateNode(NodeCreateNodeEcEntity createNode) {
    this.createNode = createNode;
  }

  public NodeCreateUpdateNodeEcEntity getUpdateNode() {
    return updateNode;
  }

  public void setUpdateNode(NodeCreateUpdateNodeEcEntity updateNode) {
    this.updateNode = updateNode;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
