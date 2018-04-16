
package msf.mfcfc.node.nodes.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class NodeInfoNodeNotifyEntity {

  @SerializedName("equipment")
  private NodeEquipmentEntity equipment;

  @SerializedName("create_node")
  private NodeCreateNodeEntity createNode;

  @SerializedName("update_node")
  private NodeUpdateNodeEntity updateNode;

  public NodeEquipmentEntity getEquipment() {
    return equipment;
  }

  public void setEquipment(NodeEquipmentEntity equipment) {
    this.equipment = equipment;
  }

  public NodeCreateNodeEntity getCreateNode() {
    return createNode;
  }

  public void setCreateNode(NodeCreateNodeEntity createNode) {
    this.createNode = createNode;
  }

  public NodeUpdateNodeEntity getUpdateNode() {
    return updateNode;
  }

  public void setUpdateNode(NodeUpdateNodeEntity updateNode) {
    this.updateNode = updateNode;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
