
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

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "l2_slices")
@NamedQuery(name = "FcL2Slice.findAll", query = "SELECT f FROM FcL2Slice f")
public class FcL2Slice implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "slice_id")
  private String sliceId;

  @Column(name = "remark_menu")
  private String remarkMenu;

  @Column(name = "vrf_id")
  private Integer vrfId;

  @OneToMany(mappedBy = "l2Slice")
  private List<FcL2Cp> l2Cps;

  public FcL2Slice() {
  }

  public String getSliceId() {
    return this.sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public String getRemarkMenu() {
    return this.remarkMenu;
  }

  public void setRemarkMenu(String remarkMenu) {
    this.remarkMenu = remarkMenu;
  }

  public Integer getVrfId() {
    return this.vrfId;
  }

  public void setVrfId(Integer vrfId) {
    this.vrfId = vrfId;
  }

  public List<FcL2Cp> getL2Cps() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l2Cps);
  }

  public void setL2Cps(List<FcL2Cp> l2Cps) {
    this.l2Cps = l2Cps;
  }

  public FcL2Cp addL2Cp(FcL2Cp l2Cp) throws MsfException {
    getL2Cps().add(l2Cp);
    l2Cp.setL2Slice(this);

    return l2Cp;
  }

  public FcL2Cp removeL2Cp(FcL2Cp l2Cp) throws MsfException {
    getL2Cps().remove(l2Cp);
    l2Cp.setL2Slice(null);

    return l2Cp;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "l2Cps" }).toString();
  }

}