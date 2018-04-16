
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
@Table(name = "physical_ifs")
@NamedQuery(name = "FcPhysicalIf.findAll", query = "SELECT f FROM FcPhysicalIf f")
public class FcPhysicalIf implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "physical_if_info_id")
  private Long physicalIfInfoId;

  @Column(name = "physical_if_id")
  private String physicalIfId;

  @OneToMany(mappedBy = "physicalIf", cascade = CascadeType.REMOVE)
  private List<FcClusterLinkIf> clusterLinkIfs;

  @OneToMany(mappedBy = "physicalIf", cascade = CascadeType.REMOVE)
  private List<FcEdgePoint> edgePoints;

  @OneToMany(mappedBy = "physicalIf", cascade = CascadeType.REMOVE)
  private List<FcInternalLinkIf> internalLinkIfs;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "node_info_id")
  private FcNode node;

  public FcPhysicalIf() {
  }

  public Long getPhysicalIfInfoId() {
    return this.physicalIfInfoId;
  }

  public void setPhysicalIfInfoId(Long physicalIfInfoId) {
    this.physicalIfInfoId = physicalIfInfoId;
  }

  public String getPhysicalIfId() {
    return this.physicalIfId;
  }

  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  public List<FcClusterLinkIf> getClusterLinkIfs() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.clusterLinkIfs);
  }

  public void setClusterLinkIfs(List<FcClusterLinkIf> clusterLinkIfs) {
    this.clusterLinkIfs = clusterLinkIfs;
  }

  public FcClusterLinkIf addClusterLinkIf(FcClusterLinkIf clusterLinkIf) throws MsfException {
    getClusterLinkIfs().add(clusterLinkIf);
    clusterLinkIf.setPhysicalIf(this);

    return clusterLinkIf;
  }

  public FcClusterLinkIf removeClusterLinkIf(FcClusterLinkIf clusterLinkIf) throws MsfException {
    getClusterLinkIfs().remove(clusterLinkIf);
    clusterLinkIf.setPhysicalIf(null);

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
    edgePoint.setPhysicalIf(this);

    return edgePoint;
  }

  public FcEdgePoint removeEdgePoint(FcEdgePoint edgePoint) throws MsfException {
    getEdgePoints().remove(edgePoint);
    edgePoint.setPhysicalIf(null);

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
    internalLinkIf.setPhysicalIf(this);

    return internalLinkIf;
  }

  public FcInternalLinkIf removeInternalLinkIf(FcInternalLinkIf internalLinkIf) throws MsfException {
    getInternalLinkIfs().remove(internalLinkIf);
    internalLinkIf.setPhysicalIf(null);

    return internalLinkIf;
  }

  public FcNode getNode() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.node);
  }

  public void setNode(FcNode node) {
    this.node = node;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "clusterLinkIfs", "edgePoints", "internalLinkIfs", "node" }).toString();
  }

}
