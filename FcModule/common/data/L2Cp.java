package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.InterfaceOperationStatus;
import msf.fc.common.constant.PortMode;
import msf.fc.common.constant.ReserveStatus;
import msf.fc.common.exception.MsfException;
import msf.fc.db.SessionWrapper;

@Entity
@Table(name = "l2_cps")
@NamedQuery(name = "L2Cp.findAll", query = "SELECT l FROM L2Cp l")
public class L2Cp implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private L2CpPK id;

  @Column(name = "operation_status")
  private Integer operationStatus;

  @Column(name = "port_mode")
  private Integer portMode;

  @Column(name = "reservation_status")
  private Integer reservationStatus;

  private Integer status;

  @Column(name = "vlan_id")
  private Integer vlanId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({ @JoinColumn(name = "edge_point_id", referencedColumnName = "edge_point_id"),
      @JoinColumn(name = "sw_cluster_id", referencedColumnName = "sw_cluster_id") })
  private EdgePoint edgePoint;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "slice_id", insertable = false, updatable = false)
  private L2Slice l2Slice;

  public L2Cp() {
  }

  public L2CpPK getId() {
    return this.id;
  }

  public void setId(L2CpPK id) {
    this.id = id;
  }

  public Integer getOperationStatus() {
    return this.operationStatus;
  }

  public void setOperationStatus(Integer operationStatus) {
    this.operationStatus = operationStatus;
  }

  public InterfaceOperationStatus getOperationStatusEnum() {
    return InterfaceOperationStatus.getEnumFromCode(this.operationStatus);
  }

  public void setOperationStatusEnum(InterfaceOperationStatus operationStatus) {
    this.operationStatus = operationStatus.getCode();
  }

  public Integer getPortMode() {
    return this.portMode;
  }

  public void setPortMode(Integer portMode) {
    this.portMode = portMode;
  }

  public PortMode getPortModeEnum() {
    return PortMode.getEnumFromCode(this.portMode);
  }

  public void setPortModeEnum(PortMode portMode) {
    this.portMode = portMode.getCode();
  }

  public Integer getReservationStatus() {
    return this.reservationStatus;
  }

  public void setReservationStatus(Integer reservationStatus) {
    this.reservationStatus = reservationStatus;
  }

  public ReserveStatus getReservationStatusEnum() {
    return ReserveStatus.getEnumFromCode(this.reservationStatus);
  }

  public void setReservationStatusEnum(ReserveStatus reservationStatus) {
    this.reservationStatus = reservationStatus.getCode();
  }

  public Integer getStatus() {
    return this.status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public ActiveStatus getStatusEnum() {
    return ActiveStatus.getEnumFromCode(this.status);
  }

  public void setStatusEnum(ActiveStatus status) {
    this.status = status.getCode();
  }

  public Integer getVlanId() {
    return this.vlanId;
  }

  public void setVlanId(Integer vlanId) {
    this.vlanId = vlanId;
  }

  public EdgePoint getEdgePoint() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.edgePoint);
  }

  public void setEdgePoint(EdgePoint edgePoint) {
    this.edgePoint = edgePoint;
  }

  public L2Slice getL2Slice() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l2Slice);
  }

  public void setL2Slice(L2Slice l2Slice) {
    this.l2Slice = l2Slice;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "edgePoint", "l2Slice" })
        .toString();
  }

}