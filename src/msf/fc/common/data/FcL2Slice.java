
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

  @Column(name = "irb_type")
  private Integer irbType;

  private Integer l3vni;

  @Column(name = "l3vni_vlan_id")
  private Integer l3vniVlanId;

  @Column(name = "remark_menu")
  private String remarkMenu;

  private Integer vni;

  @Column(name = "vrf_id")
  private Integer vrfId;

  @OneToMany(mappedBy = "l2Slice")
  private List<FcIrbInstance> irbInstances;

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

  public Integer getIrbType() {
    return this.irbType;
  }

  public void setIrbType(Integer irbType) {
    this.irbType = irbType;
  }

  public Integer getL3vni() {
    return this.l3vni;
  }

  public void setL3vni(Integer l3vni) {
    this.l3vni = l3vni;
  }

  public Integer getL3vniVlanId() {
    return this.l3vniVlanId;
  }

  public void setL3vniVlanId(Integer l3vniVlanId) {
    this.l3vniVlanId = l3vniVlanId;
  }

  public String getRemarkMenu() {
    return this.remarkMenu;
  }

  public void setRemarkMenu(String remarkMenu) {
    this.remarkMenu = remarkMenu;
  }

  public Integer getVni() {
    return this.vni;
  }

  public void setVni(Integer vni) {
    this.vni = vni;
  }

  public Integer getVrfId() {
    return this.vrfId;
  }

  public void setVrfId(Integer vrfId) {
    this.vrfId = vrfId;
  }

  public List<FcIrbInstance> getIrbInstances() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.irbInstances);
  }

  public void setIrbInstances(List<FcIrbInstance> irbInstances) {
    this.irbInstances = irbInstances;
  }

  public FcIrbInstance addIrbInstance(FcIrbInstance irbInstance) throws MsfException {
    getIrbInstances().add(irbInstance);
    irbInstance.setL2Slice(this);

    return irbInstance;
  }

  public FcIrbInstance removeIrbInstance(FcIrbInstance irbInstance) throws MsfException {
    getIrbInstances().remove(irbInstance);
    irbInstance.setL2Slice(null);

    return irbInstance;
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
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "irbInstances", "l2Cps" })
        .toString();
  }

}
