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
import msf.fc.common.constant.ReserveStatus;
import msf.fc.common.exception.MsfException;
import msf.fc.db.SessionWrapper;

@Entity
@Table(name = "l2_slices")
@NamedQuery(name = "L2Slice.findAll", query = "SELECT l FROM L2Slice l")
public class L2Slice implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "slice_id")
  private String sliceId;

  @Column(name = "reservation_status")
  private Integer reservationStatus;

  private Integer status;

  @Column(name = "vrf_id")
  private Integer vrfId;

  @OneToMany(mappedBy = "l2Slice")
  private List<L2Cp> l2Cps;

  public L2Slice() {
  }

  public String getSliceId() {
    return this.sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
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

  public List<L2Cp> getL2Cps() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l2Cps);
  }

  public void setL2Cps(List<L2Cp> l2Cps) {
    this.l2Cps = l2Cps;
  }

  public L2Cp addL2Cp(L2Cp l2Cp) throws MsfException {
    getL2Cps().add(l2Cp);
    l2Cp.setL2Slice(this);

    return l2Cp;
  }

  public L2Cp removeL2Cp(L2Cp l2Cp) throws MsfException {
    getL2Cps().remove(l2Cp);
    l2Cp.setL2Slice(null);

    return l2Cp;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "l2Cps" })
        .toString();
  }

}