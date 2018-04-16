
package msf.fc.rest.ec.status.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.EcBlockadeStatus;
import msf.mfcfc.common.constant.EcEmServiceStatus;

public class ControllerStatusEcStatusEcEntity {

  @SerializedName("status")
  private String status;

  @SerializedName("busy")
  private String busy;

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

  public String getBusy() {
    return busy;
  }

  public void setBusy(String busy) {
    this.busy = busy;
  }

  public EcBlockadeStatus getBusyEnum() {
    return EcBlockadeStatus.getEnumFromMessage(busy);
  }

  public void setBusyEnum(EcBlockadeStatus busyType) {
    this.busy = busyType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
