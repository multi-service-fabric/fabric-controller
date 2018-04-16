
package msf.mfc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "lag_links")
@NamedQuery(name = "MfcLagLink.findAll", query = "SELECT m FROM MfcLagLink m")
public class MfcLagLink implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "cluster_link_if_id")
  private Integer clusterLinkIfId;

  @Column(name = "lag_if_id")
  private Integer lagIfId;

  @Column(name = "node_id")
  private Integer nodeId;

  @Column(name = "opposite_lag_if_id")
  private Integer oppositeLagIfId;

  @Column(name = "opposite_node_id")
  private Integer oppositeNodeId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cluster_link_if_id")
  private MfcClusterLinkIf clusterLinkIf;

  public MfcLagLink() {
  }

  public Integer getClusterLinkIfId() {
    return this.clusterLinkIfId;
  }

  public void setClusterLinkIfId(Integer clusterLinkIfId) {
    this.clusterLinkIfId = clusterLinkIfId;
  }

  public Integer getLagIfId() {
    return this.lagIfId;
  }

  public void setLagIfId(Integer lagIfId) {
    this.lagIfId = lagIfId;
  }

  public Integer getNodeId() {
    return this.nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public Integer getOppositeLagIfId() {
    return this.oppositeLagIfId;
  }

  public void setOppositeLagIfId(Integer oppositeLagIfId) {
    this.oppositeLagIfId = oppositeLagIfId;
  }

  public Integer getOppositeNodeId() {
    return this.oppositeNodeId;
  }

  public void setOppositeNodeId(Integer oppositeNodeId) {
    this.oppositeNodeId = oppositeNodeId;
  }

  public MfcClusterLinkIf getClusterLinkIf() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.clusterLinkIf);
  }

  public void setClusterLinkIf(MfcClusterLinkIf clusterLinkIf) {
    this.clusterLinkIf = clusterLinkIf;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "clusterLinkIf" }).toString();
  }

}
