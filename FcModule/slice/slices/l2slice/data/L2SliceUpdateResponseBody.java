package msf.fc.slice.slices.l2slice.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.ReserveStatus;
import msf.fc.rest.common.AbstractResponseBody;

public class L2SliceUpdateResponseBody extends AbstractResponseBody {

  @SerializedName("status")
  private String status;

  @SerializedName("reservation_status")
  private String reservationStatus;

  @SerializedName("operation_id")
  private String operationId;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getOperationId() {
    return operationId;
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  public String getReservationStatus() {
    return reservationStatus;
  }

  public void setReservationStatus(String reservationStatus) {
    this.reservationStatus = reservationStatus;
  }

  public ActiveStatus getStatusEnum() {
    return ActiveStatus.getEnumFromMessage(status);
  }

  public void setStatusEnum(ActiveStatus status) {
    this.status = status.getMessage();
  }

  public ReserveStatus getReservationStatusEnum() {
    return ReserveStatus.getEnumFromMessage(reservationStatus);
  }

  public void setReservationStatusEnum(ReserveStatus reservationStatus) {
    this.reservationStatus = reservationStatus.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
