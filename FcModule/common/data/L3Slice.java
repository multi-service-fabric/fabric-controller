package msf.fc.common.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.fc.common.constant.ActiveStatus;
import msf.fc.common.constant.PlaneBelongsTo;
import msf.fc.common.constant.ReserveStatus;
import msf.fc.common.exception.MsfException;
import msf.fc.db.SessionWrapper;

@Entity
@Table(name = "l3_slices")
@NamedQuery(name = "L3Slice.findAll", query = "SELECT l FROM L3Slice l")
public class L3Slice implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "slice_id")
  private String sliceId;

  private Integer plane;

  @Column(name = "reservation_status")
  private Integer reservationStatus;

  private Integer status;

  @Column(name = "vrf_id")
  private Integer vrfId;

  @OneToMany(mappedBy = "l3Slice")
  private List<L3Cp> l3Cps;

  public L3Slice() {
  }

  public String getSliceId() {
    return this.sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public Integer getPlane() {
    return this.plane;
  }

  public void setPlane(Integer plane) {
    this.plane = plane;
  }

  public PlaneBelongsTo getPlaneEnum() {
    return PlaneBelongsTo.getEnumFromCode(this.plane);
  }

  public void setPlaneEnum(PlaneBelongsTo plane) {
    this.plane = plane.getCode();
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

  public Integer getVrfId() {
    return this.vrfId;
  }

  public void setVrfId(Integer vrfId) {
    this.vrfId = vrfId;
  }

  public List<L3Cp> getL3Cps() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l3Cps);
  }

  public void setL3Cps(List<L3Cp> l3Cps) {
    this.l3Cps = l3Cps;
  }

  public L3Cp addL3Cp(L3Cp l3Cp) throws MsfException {
    getL3Cps().add(l3Cp);
    l3Cp.setL3Slice(this);

    return l3Cp;
  }

  public L3Cp removeL3Cp(L3Cp l3Cp) throws MsfException {
    getL3Cps().remove(l3Cp);
    l3Cp.setL3Slice(null);

    return l3Cp;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "l3Cps" })
        .toString();
  }

}