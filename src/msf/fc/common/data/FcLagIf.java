
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
@Table(name = "lag_ifs")
@NamedQuery(name = "FcLagIf.findAll", query = "SELECT f FROM FcLagIf f")
public class FcLagIf implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "lag_if_info_id")
  private Long lagIfInfoId;

  @Column(name = "lag_if_id")
  private Integer lagIfId;

  @OneToMany(mappedBy = "lagIf", cascade = CascadeType.REMOVE)
  private List<FcClusterLinkIf> clusterLinkIfs;

  @OneToMany(mappedBy = "lagIf", cascade = CascadeType.REMOVE)
  private List<FcEdgePoint> edgePoints;

  @OneToMany(mappedBy = "lagIf", cascade = CascadeType.REMOVE)
  private List<FcInternalLinkIf> internalLinkIfs;

  @OneToMany(mappedBy = "lagIf")
  private List<FcLagIfFilterInfo> lagIfFilterInfos;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "node_info_id")
  private FcNode node;

  public FcLagIf() {
  }

  public Long getLagIfInfoId() {
    return this.lagIfInfoId;
  }

  public void setLagIfInfoId(Long lagIfInfoId) {
    this.lagIfInfoId = lagIfInfoId;
  }

  public Integer getLagIfId() {
    return this.lagIfId;
  }

  public void setLagIfId(Integer lagIfId) {
    this.lagIfId = lagIfId;
  }

  public List<FcClusterLinkIf> getClusterLinkIfs() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.clusterLinkIfs);
  }

  public void setClusterLinkIfs(List<FcClusterLinkIf> clusterLinkIfs) {
    this.clusterLinkIfs = clusterLinkIfs;
  }

  public FcClusterLinkIf addClusterLinkIf(FcClusterLinkIf clusterLinkIf) throws MsfException {
    getClusterLinkIfs().add(clusterLinkIf);
    clusterLinkIf.setLagIf(this);

    return clusterLinkIf;
  }

  public FcClusterLinkIf removeClusterLinkIf(FcClusterLinkIf clusterLinkIf) throws MsfException {
    getClusterLinkIfs().remove(clusterLinkIf);
    clusterLinkIf.setLagIf(null);

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
    edgePoint.setLagIf(this);

    return edgePoint;
  }

  public FcEdgePoint removeEdgePoint(FcEdgePoint edgePoint) throws MsfException {
    getEdgePoints().remove(edgePoint);
    edgePoint.setLagIf(null);

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
    internalLinkIf.setLagIf(this);

    return internalLinkIf;
  }

  public FcInternalLinkIf removeInternalLinkIf(FcInternalLinkIf internalLinkIf) throws MsfException {
    getInternalLinkIfs().remove(internalLinkIf);
    internalLinkIf.setLagIf(null);

    return internalLinkIf;
  }

  public List<FcLagIfFilterInfo> getLagIfFilterInfos() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.lagIfFilterInfos);
  }

  public void setLagIfFilterInfos(List<FcLagIfFilterInfo> lagIfFilterInfos) {
    this.lagIfFilterInfos = lagIfFilterInfos;
  }

  public FcLagIfFilterInfo addLagIfFilterInfo(FcLagIfFilterInfo lagIfFilterInfo) throws MsfException {
    getLagIfFilterInfos().add(lagIfFilterInfo);
    lagIfFilterInfo.setLagIf(this);

    return lagIfFilterInfo;
  }

  public FcLagIfFilterInfo removeLagIfFilterInfo(FcLagIfFilterInfo lagIfFilterInfo) throws MsfException {
    getLagIfFilterInfos().remove(lagIfFilterInfo);
    lagIfFilterInfo.setLagIf(null);

    return lagIfFilterInfo;
  }

  public FcNode getNode() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.node);
  }

  public void setNode(FcNode node) {
    this.node = node;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(
        new String[] { "clusterLinkIfs", "edgePoints", "internalLinkIfs", "internalLinkIfs", "node" }).toString();
  }

}
