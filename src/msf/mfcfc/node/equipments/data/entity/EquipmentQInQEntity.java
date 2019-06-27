
package msf.mfcfc.node.equipments.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EquipmentQInQEntity {

  @SerializedName("selectable_by_node")
  private Boolean selectableByNode;

  @SerializedName("selectable_by_cp")
  private Boolean selectableByCp;

  public Boolean getSelectableByNode() {
    return selectableByNode;
  }

  public void setSelectableByNode(Boolean selectableByNode) {
    this.selectableByNode = selectableByNode;
  }

  public Boolean getSelectableByCp() {
    return selectableByCp;
  }

  public void setSelectableByCp(Boolean selectableByCp) {
    this.selectableByCp = selectableByCp;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
