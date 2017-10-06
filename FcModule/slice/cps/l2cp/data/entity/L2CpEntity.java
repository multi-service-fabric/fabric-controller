package msf.fc.slice.cps.l2cp.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.InterfaceOperationStatus;
import msf.fc.common.constant.PortMode;
import msf.fc.common.constant.ReserveStatus;

public class L2CpEntity {

  @SerializedName("cp_id")
  private String cpId;

  @SerializedName("slice_id")
  private String sliceId;
  @SerializedName("cluster_id")
  private String clusterId;
  @SerializedName("edge_point_id")
  private String edgePointId;
  @SerializedName("port_mode")
  private String portMode;

  @SerializedName("status")
  private String status;
  @SerializedName("reservation_status")
  private String reservationStatus;
  @SerializedName("operation_status")
  private String operationStatus;

  public String getCpId() {
    return cpId;
  }

  public void setCpId(String cpId) {
    this.cpId = cpId;
  }

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getEdgePointId() {
    return edgePointId;
  }

  public void setEdgePointId(String edgePointId) {
    this.edgePointId = edgePointId;
  }

  public String getPortMode() {
    return portMode;
  }

  public void setPortMode(String portMode) {
    this.portMode = portMode;
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

  public String getOperationStatus() {
    return operationStatus;
  }

  public void setOperationStatus(String operationStatus) {
    this.operationStatus = operationStatus;
  }

  public PortMode getPortModeEnum() {
    return PortMode.getEnumFromMessage(portMode);
  }

  public void setPortModeEnum(PortMode portMode) {
    this.portMode = portMode.getMessage();
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
