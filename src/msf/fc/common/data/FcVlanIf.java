
package msf.fc.common.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.mfcfc.common.exception.MsfException;

@Entity
@Table(name = "vlan_ifs")
@NamedQuery(name = "FcVlanIf.findAll", query = "SELECT f FROM FcVlanIf f")
public class FcVlanIf implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private FcVlanIfPK id;

  @OneToMany(mappedBy = "vlanIf")
  private List<FcL2Cp> l2Cps;

  @OneToMany(mappedBy = "vlanIf")
  private List<FcL3Cp> l3Cps;

  public FcVlanIf() {
  }

  public FcVlanIfPK getId() {
    return this.id;
  }

  public void setId(FcVlanIfPK id) {
    this.id = id;
  }

  public List<FcL2Cp> getL2Cps() {
    return this.l2Cps;
  }

  public void setL2Cps(List<FcL2Cp> l2Cps) {
    this.l2Cps = l2Cps;
  }

  public FcL2Cp addL2Cp(FcL2Cp l2Cp) {
    getL2Cps().add(l2Cp);
    l2Cp.setVlanIf(this);

    return l2Cp;
  }

  public FcL2Cp removeL2Cp(FcL2Cp l2Cp) {
    getL2Cps().remove(l2Cp);
    l2Cp.setVlanIf(null);

    return l2Cp;
  }

  public List<FcL3Cp> getL3Cps() {
    return this.l3Cps;
  }

  public void setL3Cps(List<FcL3Cp> l3Cps) {
    this.l3Cps = l3Cps;
  }

  public FcL3Cp addL3Cp(FcL3Cp l3Cp) {
    getL3Cps().add(l3Cp);
    l3Cp.setVlanIf(this);

    return l3Cp;
  }

  public FcL3Cp removeL3Cp(FcL3Cp l3Cp) {
    getL3Cps().remove(l3Cp);
    l3Cp.setVlanIf(null);

    return l3Cp;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "l2Cps", "l3Cps" }).toString();
  }

}
