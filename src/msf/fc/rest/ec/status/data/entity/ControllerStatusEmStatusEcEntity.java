
package msf.fc.rest.ec.status.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.EcEmServiceStatus;

public class ControllerStatusEmStatusEcEntity {

  @SerializedName("status")
  private String status;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public EcEmServiceStatus getStatusEnum() {
    return EcEmServiceStatus.getEnumFromMessage(status);
  }

  public void setStatusEnum(EcEmServiceStatus statusType) {
    this.status = statusType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
