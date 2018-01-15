
package msf.fc.common.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "edge_points")
@NamedQuery(name = "FcEdgePoint.findAll", query = "SELECT f FROM FcEdgePoint f")
public class FcEdgePoint implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "edge_point_id")
  private Integer edgePointId;

  @Column(name = "traffic_threshold")
  private Double trafficThreshold;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "breakout_if_info_id")
  private FcBreakoutIf breakoutIf;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lag_if_id")
  private FcLagIf lagIf;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "physical_if_info_id")
  private FcPhysicalIf physicalIf;

  @OneToMany(mappedBy = "edgePoint")
  private List<FcL2Cp> l2Cps;

  @OneToMany(mappedBy = "edgePoint")
  private List<FcL3Cp> l3Cps;

  public FcEdgePoint() {
  }

  public Integer getEdgePointId() {
    return this.edgePointId;
  }

  public void setEdgePointId(Integer edgePointId) {
    this.edgePointId = edgePointId;
  }

  public Double getTrafficThreshold() {
    return this.trafficThreshold;
  }

  public void setTrafficThreshold(Double trafficThreshold) {
    this.trafficThreshold = trafficThreshold;
  }

  public FcBreakoutIf getBreakoutIf() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.breakoutIf);
  }

  public void setBreakoutIf(FcBreakoutIf breakoutIf) {
    this.breakoutIf = breakoutIf;
  }

  public FcLagIf getLagIf() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.lagIf);
  }

  public void setLagIf(FcLagIf lagIf) {
    this.lagIf = lagIf;
  }

  public FcPhysicalIf getPhysicalIf() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.physicalIf);
  }

  public void setPhysicalIf(FcPhysicalIf physicalIf) {
    this.physicalIf = physicalIf;
  }

  public List<FcL2Cp> getL2Cps() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l2Cps);
  }

  public void setL2Cps(List<FcL2Cp> l2Cps) {
    this.l2Cps = l2Cps;
  }

  public FcL2Cp addL2Cp(FcL2Cp l2Cp) throws MsfException {
    getL2Cps().add(l2Cp);
    l2Cp.setEdgePoint(this);

    return l2Cp;
  }

  public FcL2Cp removeL2Cp(FcL2Cp l2Cp) throws MsfException {
    getL2Cps().remove(l2Cp);
    l2Cp.setEdgePoint(null);

    return l2Cp;
  }

  public List<FcL3Cp> getL3Cps() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.l3Cps);
  }

  public void setL3Cps(List<FcL3Cp> l3Cps) {
    this.l3Cps = l3Cps;
  }

  public FcL3Cp addL3Cp(FcL3Cp l3Cp) throws MsfException {
    getL3Cps().add(l3Cp);
    l3Cp.setEdgePoint(this);

    return l3Cp;
  }

  public FcL3Cp removeL3Cp(FcL3Cp l3Cp) throws MsfException {
    getL3Cps().remove(l3Cp);
    l3Cp.setEdgePoint(null);

    return l3Cp;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "breakoutIf", "lagIf", "physicalIf", "l2Cps", "l3Cps" }).toString();
  }

}