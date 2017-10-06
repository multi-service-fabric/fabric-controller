package msf.fc.slice.slices.l2slice.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.ReserveStatus;

public class L2SliceEntity {

  @SerializedName("slice_id")
  private String sliceId;

  private String status;
  @SerializedName("reservation_status")
  private String reservationStatus;
  @SerializedName("l2_cp_ids")
  private List<String> l2CpIds;

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getReservationStatus() {
    return reservationStatus;
  }

  public void setReservationStatus(String reservationStatus) {
    this.reservationStatus = reservationStatus;
  }

  public List<String> getL2CpIds() {
    return l2CpIds;
  }

  public void setL2CpIds(List<String> l2CpIds) {
    this.l2CpIds = l2CpIds;
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
