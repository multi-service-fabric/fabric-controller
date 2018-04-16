
package msf.fc.common.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
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
@Table(name = "breakout_ifs")
@NamedQuery(name = "FcBreakoutIf.findAll", query = "SELECT f FROM FcBreakoutIf f")
public class FcBreakoutIf implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "breakout_if_info_id")
  private Long breakoutIfInfoId;

  @Column(name = "breakout_if_id")
  private String breakoutIfId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "node_info_id")
  private FcNode node;

  @OneToMany(mappedBy = "breakoutIf", cascade = CascadeType.REMOVE)
  private List<FcClusterLinkIf> clusterLinkIfs;

  @OneToMany(mappedBy = "breakoutIf", cascade = CascadeType.REMOVE)
  private List<FcEdgePoint> edgePoints;

  @OneToMany(mappedBy = "breakoutIf", cascade = CascadeType.REMOVE)
  private List<FcInternalLinkIf> internalLinkIfs;

  public FcBreakoutIf() {
  }

  public Long getBreakoutIfInfoId() {
    return this.breakoutIfInfoId;
  }

  public void setBreakoutIfInfoId(Long breakoutIfInfoId) {
    this.breakoutIfInfoId = breakoutIfInfoId;
  }

  public String getBreakoutIfId() {
    return this.breakoutIfId;
  }

  public void setBreakoutIfId(String breakoutIfId) {
    this.breakoutIfId = breakoutIfId;
  }

  public FcNode getNode() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.node);
  }

  public void setNode(FcNode node) {
    this.node = node;
  }

  public List<FcClusterLinkIf> getClusterLinkIfs() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.clusterLinkIfs);
  }

  public void setClusterLinkIfs(List<FcClusterLinkIf> clusterLinkIfs) {
    this.clusterLinkIfs = clusterLinkIfs;
  }

  public FcClusterLinkIf addClusterLinkIf(FcClusterLinkIf clusterLinkIf) throws MsfException {
    getClusterLinkIfs().add(clusterLinkIf);
    clusterLinkIf.setBreakoutIf(this);

    return clusterLinkIf;
  }

  public FcClusterLinkIf removeClusterLinkIf(FcClusterLinkIf clusterLinkIf) throws MsfException {
    getClusterLinkIfs().remove(clusterLinkIf);
    clusterLinkIf.setBreakoutIf(null);

    return clusterLinkIf;
  }

  public List<FcEdgePoint> getEdgePoints() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.edgePoints);
  }

  public void setEdgePoints(List<FcEdgePoint> edgePoints) {
    this.edgePoints = edgePoints;
  }

  public FcEdgePoint addEdgePoint(FcEdgePoint edgePoint) throws MsfException {
    getEdgePoints().add(edgePoint);
    edgePoint.setBreakoutIf(this);

    return edgePoint;
  }

  public FcEdgePoint removeEdgePoint(FcEdgePoint edgePoint) throws MsfException {
    getEdgePoints().remove(edgePoint);
    edgePoint.setBreakoutIf(null);

    return edgePoint;
  }

  public List<FcInternalLinkIf> getInternalLinkIfs() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.internalLinkIfs);
  }

  public void setInternalLinkIfs(List<FcInternalLinkIf> internalLinkIfs) {
    this.internalLinkIfs = internalLinkIfs;
  }

  public FcInternalLinkIf addInternalLinkIf(FcInternalLinkIf internalLinkIf) throws MsfException {
    getInternalLinkIfs().add(internalLinkIf);
    internalLinkIf.setBreakoutIf(this);

    return internalLinkIf;
  }

  public FcInternalLinkIf removeInternalLinkIf(FcInternalLinkIf internalLinkIf) throws MsfException {
    getInternalLinkIfs().remove(internalLinkIf);
    internalLinkIf.setBreakoutIf(null);

    return internalLinkIf;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "node", "clusterLinkIfs", "edgePoints", "internalLinkIfs" }).toString();
  }

}
