package msf.fc.common.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.fc.common.exception.MsfException;
import msf.fc.db.SessionWrapper;

@Entity
@Table(name = "edge_points")
@NamedQuery(name = "EdgePoint.findAll", query = "SELECT e FROM EdgePoint e")
public class EdgePoint implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private EdgePointPK id;

  @Column(name = "lag_if_info_id")
  private Long lagIfInfoId;

  @Column(name = "physical_if_info_id")
  private Long physicalIfInfoId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sw_cluster_id", insertable = false, updatable = false)
  private SwCluster swCluster;

  @OneToMany(mappedBy = "edgePoint")
  private List<L2Cp> l2Cps;

  @OneToMany(mappedBy = "edgePoint")
  private List<L3Cp> l3Cps;

  public EdgePoint() {
  }

  public EdgePointPK getId() {
    return this.id;
  }

  public void setId(EdgePointPK id) {
    this.id = id;
  }

  public Long getLagIfInfoId() {
    return this.lagIfInfoId;
  }

  public void setLagIfInfoId(Long lagIfInfoId) {
    this.lagIfInfoId = lagIfInfoId;
  }

  public Long getPhysicalIfInfoId() {
    return this.physicalIfInfoId;
  }

  public void setPhysicalIfInfoId(Long physicalIfInfoId) {
    this.physicalIfInfoId = physicalIfInfoId;
  }

  public SwCluster getSwCluster() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.swCluster);
  }

  public void setSwCluster(SwCluster swCluster) {
    this.swCluster = swCluster;
  }

  public List<L2Cp> getL2Cps() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l2Cps);
  }

  public void setL2Cps(List<L2Cp> l2Cps) {
    this.l2Cps = l2Cps;
  }

  public L2Cp addL2Cp(L2Cp l2Cp) throws MsfException {
    getL2Cps().add(l2Cp);
    l2Cp.setEdgePoint(this);

    return l2Cp;
  }

  public L2Cp removeL2Cp(L2Cp l2Cp) throws MsfException {
    getL2Cps().remove(l2Cp);
    l2Cp.setEdgePoint(null);

    return l2Cp;
  }

  public List<L3Cp> getL3Cps() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l3Cps);
  }

  public void setL3Cps(List<L3Cp> l3Cps) {
    this.l3Cps = l3Cps;
  }

  public L3Cp addL3Cp(L3Cp l3Cp) throws MsfException {
    getL3Cps().add(l3Cp);
    l3Cp.setEdgePoint(this);

    return l3Cp;
  }

  public L3Cp removeL3Cp(L3Cp l3Cp) throws MsfException {
    getL3Cps().remove(l3Cp);
    l3Cp.setEdgePoint(null);

    return l3Cp;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "swCluster", "l2Cps", "l3Cps" })
        .toString();
  }

}