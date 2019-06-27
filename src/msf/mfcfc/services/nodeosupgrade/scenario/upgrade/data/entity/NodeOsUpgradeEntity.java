
package msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.services.nodeosupgrade.common.constant.NodeOsUpgradeStatus;

public class NodeOsUpgradeEntity {

  @SerializedName("fabric_type")
  private String fabricType;

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("equipment_type_id")
  private String equipmentTypeId;

  @SerializedName("status")
  private String status;

  @SerializedName("error_code")
  private String errorCode;

  @SerializedName("error_message")
  private String errorMessage;

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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public NodeOsUpgradeStatus getStatusEnum() {
    return NodeOsUpgradeStatus.getEnumFromMessage(status);
  }

  public void setStatusEnum(NodeOsUpgradeStatus status) {
    this.status = status.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
