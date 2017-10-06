package msf.fc.failure.logicalif.data.entity;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.InterfaceOperationStatus;

public class LogicalIfStatusCpBaseData {
  @SerializedName("cp_id")
  private String cpId;
  @SerializedName("status")
  private String status;

  public LogicalIfStatusCpBaseData() {
  }

  public LogicalIfStatusCpBaseData(String cpId, String status) {
    this.cpId = cpId;
    this.status = status;
  }

  public String getCpId() {
    return cpId;
  }

  public void setCpId(String cpId) {
    this.cpId = cpId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public InterfaceOperationStatus getStatusEnum() {
    return InterfaceOperationStatus.getEnumFromMessage(this.status);
  }

  public void setStatusEnum(InterfaceOperationStatus statusEnum) {
    this.status = statusEnum.getMessage();
  }

  @Override
  public String toString() {
    return "LogicalIfStatusCpBaseData [cpId=" + cpId + ", status=" + status + "]";
  }

}
