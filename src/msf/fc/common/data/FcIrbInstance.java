
package msf.fc.common.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "irb_instances")
@NamedQuery(name = "FcIrbInstance.findAll", query = "SELECT f FROM FcIrbInstance f")
public class FcIrbInstance implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "irb_instance_id")
  private Long irbInstanceId;

  @Column(name = "node_info_id")
  private Long nodeInfoId;

  @Column(name = "vlan_id")
  private Integer vlanId;

  private Integer vni;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "slice_id")
  private FcL2Slice l2Slice;

  @OneToMany(mappedBy = "irbInstance")
  private List<FcVlanIf> vlanIfs;

  public FcIrbInstance() {
  }

  public Long getIrbInstanceId() {
    return this.irbInstanceId;
  }

  public void setIrbInstanceId(Long irbInstanceId) {
    this.irbInstanceId = irbInstanceId;
  }

  public Long getNodeInfoId() {
    return this.nodeInfoId;
  }

  public void setNodeInfoId(Long nodeInfoId) {
    this.nodeInfoId = nodeInfoId;
  }

  public Integer getVlanId() {
    return this.vlanId;
  }

  public void setVlanId(Integer vlanId) {
    this.vlanId = vlanId;
  }

  public Integer getVni() {
    return this.vni;
  }

  public void setVni(Integer vni) {
    this.vni = vni;
  }

  public FcL2Slice getL2Slice() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l2Slice);
  }

  public void setL2Slice(FcL2Slice l2Slice) {
    this.l2Slice = l2Slice;
  }

  public List<FcVlanIf> getVlanIfs() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.vlanIfs);
  }

  public void setVlanIfs(List<FcVlanIf> vlanIfs) {
    this.vlanIfs = vlanIfs;
  }

  public FcVlanIf addVlanIf(FcVlanIf vlanIf) throws MsfException {
    getVlanIfs().add(vlanIf);
    vlanIf.setIrbInstance(this);

    return vlanIf;
  }

  public FcVlanIf removeVlanIf(FcVlanIf vlanIf) throws MsfException {
    getVlanIfs().remove(vlanIf);
    vlanIf.setIrbInstance(null);

    return vlanIf;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "l2Slice", "vlanIfs" }).toString();
  }
}
