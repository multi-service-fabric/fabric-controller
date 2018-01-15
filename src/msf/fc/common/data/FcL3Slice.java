
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
@Table(name = "l3_slices")
@NamedQuery(name = "FcL3Slice.findAll", query = "SELECT f FROM FcL3Slice f")
public class FcL3Slice implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "slice_id")
  private String sliceId;

  private Integer plane;

  @Column(name = "remark_menu")
  private String remarkMenu;

  @Column(name = "vrf_id")
  private Integer vrfId;

  @OneToMany(mappedBy = "l3Slice")
  private List<FcL3Cp> l3Cps;

  public FcL3Slice() {
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

  public List<FcL3Cp> getL3Cps() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l3Cps);
  }

  public void setL3Cps(List<FcL3Cp> l3Cps) {
    this.l3Cps = l3Cps;
  }

  public FcL3Cp addL3Cp(FcL3Cp l3Cp) throws MsfException {
    getL3Cps().add(l3Cp);
    l3Cp.setL3Slice(this);

    return l3Cp;
  }

  public FcL3Cp removeL3Cp(FcL3Cp l3Cp) throws MsfException {
    getL3Cps().remove(l3Cp);
    l3Cp.setL3Slice(null);

    return l3Cp;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "l3Cps" }).toString();
  }

}