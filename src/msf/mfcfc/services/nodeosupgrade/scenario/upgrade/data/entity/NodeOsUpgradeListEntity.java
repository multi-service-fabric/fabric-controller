
package msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.NodeType;

public class NodeOsUpgradeListEntity {

  @SerializedName("fabric_type")
  private String fabricType;

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("equipment_type_id")
  private String equipmentTypeId;

  @SerializedName("os_upgrade")
  private OsUpgradeEntity osUpgrade;

  @SerializedName("operator_check")
  private Boolean operatorCheck;

  public String getFabricType() {
    return fabricType;
  }

  public void setFabricType(String fabricType) {
    this.fabricType = fabricType;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getEquipmentTypeId() {
    return equipmentTypeId;
  }

  public void setEquipmentTypeId(String equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  public OsUpgradeEntity getOsUpgrade() {
    return osUpgrade;
  }

  public void setOsUpgrade(OsUpgradeEntity osUpgrade) {
    this.osUpgrade = osUpgrade;
  }

  public Boolean getOperatorCheck() {
    return operatorCheck;
  }

  public void setOperatorCheck(Boolean operatorCheck) {
    this.operatorCheck = operatorCheck;
  }

  public NodeType getFabricTypeEnum() {
    return NodeType.getEnumFromSingularMessage(fabricType);
  }

  public void setFabricTypeEnum(NodeType fabricType) {
    this.fabricType = fabricType.getSingularMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
