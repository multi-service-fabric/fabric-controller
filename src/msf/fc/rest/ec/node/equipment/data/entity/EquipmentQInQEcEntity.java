
package msf.fc.rest.ec.node.equipment.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentQInQEcEntity {

  @SerializedName("selectable_by_node")
  private Boolean selectableByNode;

  @SerializedName("selectable_by_vlan_if")
  private Boolean selectableByVlanIf;

  public Boolean getSelectableByNode() {
    return selectableByNode;
  }

  public void setSelectableByNode(Boolean selectableByNode) {
    this.selectableByNode = selectableByNode;
  }

  public Boolean getSelectableByVlanIf() {
    return selectableByVlanIf;
  }

  public void setSelectableByVlanIf(Boolean selectableByVlanIf) {
    this.selectableByVlanIf = selectableByVlanIf;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
