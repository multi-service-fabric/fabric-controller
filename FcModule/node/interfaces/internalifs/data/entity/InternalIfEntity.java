package msf.fc.node.interfaces.internalifs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.InterfaceOperationStatus;

public class InternalIfEntity {

  @SerializedName("internal_link_if_id")
  private String internalIfId;

  @SerializedName("lag_if_id")
  private String laglIfId;

  @SerializedName("operation_status")
  private String operationStatus;

  public String getInternalIfId() {
    return internalIfId;
  }

  public void setInternalIfId(String internalIfId) {
    this.internalIfId = internalIfId;
  }

  public String getLaglIfId() {
    return laglIfId;
  }

  public void setLaglIfId(String laglIfId) {
    this.laglIfId = laglIfId;
  }

  public String getOperationStatus() {
    return operationStatus;
  }

  public void setOperationStatus(String operationStatus) {
    this.operationStatus = operationStatus;
  }

  public InterfaceOperationStatus getOperationStatusEnum() {
    return InterfaceOperationStatus.getEnumFromMessage(operationStatus);
  }

  public void setOperationStatusEnum(InterfaceOperationStatus operationStatus) {
    this.operationStatus = operationStatus.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
