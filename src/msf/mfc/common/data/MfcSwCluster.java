
package msf.mfc.common.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
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
@Table(name = "sw_clusters")
@NamedQuery(name = "MfcSwCluster.findAll", query = "SELECT m FROM MfcSwCluster m")
public class MfcSwCluster implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "sw_cluster_id")
  private Integer swClusterId;

  @Column(name = "cluster_status")
  private Integer clusterStatus;

  @OneToMany(mappedBy = "swCluster")
  private List<MfcClusterLinkIf> clusterLinkIfs;

  @OneToMany(mappedBy = "swCluster", cascade = CascadeType.REMOVE)
  private List<MfcL2Cp> l2Cps;

  @OneToMany(mappedBy = "swCluster", cascade = CascadeType.REMOVE)
  private List<MfcL3Cp> l3Cps;

  public MfcSwCluster() {
  }

  public Integer getSwClusterId() {
    return this.swClusterId;
  }

  public void setSwClusterId(Integer swClusterId) {
    this.swClusterId = swClusterId;
  }

  public Integer getClusterStatus() {
    return this.clusterStatus;
  }

  public void setClusterStatus(Integer clusterStatus) {
    this.clusterStatus = clusterStatus;
  }

  public List<MfcClusterLinkIf> getClusterLinkIfs() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.clusterLinkIfs);
  }

  public void setClusterLinkIfs(List<MfcClusterLinkIf> clusterLinkIfs) {
    this.clusterLinkIfs = clusterLinkIfs;
  }

  public MfcClusterLinkIf addClusterLinkIf(MfcClusterLinkIf clusterLinkIf) throws MsfException {
    getClusterLinkIfs().add(clusterLinkIf);
    clusterLinkIf.setSwCluster(this);

    return clusterLinkIf;
  }

  public MfcClusterLinkIf removeClusterLinkIf(MfcClusterLinkIf clusterLinkIf) throws MsfException {
    getClusterLinkIfs().remove(clusterLinkIf);
    clusterLinkIf.setSwCluster(null);

    return clusterLinkIf;
  }

  public List<MfcL2Cp> getL2Cps() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l2Cps);
  }

  public void setL2Cps(List<MfcL2Cp> l2Cps) {
    this.l2Cps = l2Cps;
  }

  public MfcL2Cp addL2Cp(MfcL2Cp l2Cp) throws MsfException {
    getL2Cps().add(l2Cp);
    l2Cp.setSwCluster(this);

    return l2Cp;
  }

  public MfcL2Cp removeL2Cp(MfcL2Cp l2Cp) throws MsfException {
    getL2Cps().remove(l2Cp);
    l2Cp.setSwCluster(null);

    return l2Cp;
  }

  public List<MfcL3Cp> getL3Cps() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l3Cps);
  }

  public void setL3Cps(List<MfcL3Cp> l3Cps) {
    this.l3Cps = l3Cps;
  }

  public MfcL3Cp addL3Cp(MfcL3Cp l3Cp) throws MsfException {
    getL3Cps().add(l3Cp);
    l3Cp.setSwCluster(this);

    return l3Cp;
  }

  public MfcL3Cp removeL3Cp(MfcL3Cp l3Cp) throws MsfException {
    getL3Cps().remove(l3Cp);
    l3Cp.setSwCluster(null);

    return l3Cp;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "clusterLinkIfs", "l2Cps", "l3Cps" })
        .toString();
  }

}
