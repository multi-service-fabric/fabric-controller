package msf.fc.slice.slices.l3slice.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.PlaneBelongsTo;
import msf.fc.common.constant.ReserveStatus;

public class L3SliceEntity {

  @SerializedName("slice_id")
  private String sliceId;

  @SerializedName("plane")
  private Integer plane;

  @SerializedName("status")
  private String status;

  @SerializedName("reservation_status")
  private String reservationStatus;

  @SerializedName("l3_cp_ids")
  private List<String> l3CpIds;

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public Integer getPlane() {
    return plane;
  }

  public void setPlane(Integer plane) {
    this.plane = plane;
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

  public List<String> getL3CpIds() {
    return l3CpIds;
  }

  public void setL3CpIds(List<String> l3CpIds) {
    this.l3CpIds = l3CpIds;
  }

  public PlaneBelongsTo getPlaneEnum() {
    return PlaneBelongsTo.getEnumFromMessage(plane);
  }

  public void setPlaneEnum(PlaneBelongsTo plane) {
    this.plane = plane.getMessage();
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
