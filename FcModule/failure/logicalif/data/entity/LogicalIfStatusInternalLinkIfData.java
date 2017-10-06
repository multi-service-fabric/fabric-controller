package msf.fc.failure.logicalif.data.entity;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.InterfaceOperationStatus;

public class LogicalIfStatusInternalLinkIfData {

  @SerializedName("internal_link_if_id")
  private String internalLinkIfId;
  @SerializedName("status")
  private String status;

  public LogicalIfStatusInternalLinkIfData() {
  }

  public LogicalIfStatusInternalLinkIfData(String internalLinkIfId, String status) {
    this.internalLinkIfId = internalLinkIfId;
    this.status = status;
  }

  public String getInternalLinkIfId() {
    return internalLinkIfId;
  }

  public void setInternalLinkIfId(String internalLinkIfId) {
    this.internalLinkIfId = internalLinkIfId;
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
    return "LogicalIfStatusInternalLinkIfData [internalLinkIfId=" + internalLinkIfId + ", status=" + status + "]";
  }
}
