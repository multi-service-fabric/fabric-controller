
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
@Table(name = "physical_links")
@NamedQuery(name = "MfcPhysicalLink.findAll", query = "SELECT m FROM MfcPhysicalLink m")
public class MfcPhysicalLink implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "cluster_link_if_id")
  private Integer clusterLinkIfId;

  @Column(name = "breakout_if_id")
  private String breakoutIfId;

  @Column(name = "node_id")
  private Integer nodeId;

  @Column(name = "opposite_breakout_if_id")
  private String oppositeBreakoutIfId;

  @Column(name = "opposite_node_id")
  private Integer oppositeNodeId;

  @Column(name = "opposite_physical_if_id")
  private String oppositePhysicalIfId;

  @Column(name = "physical_if_id")
  private String physicalIfId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cluster_link_if_id")
  private MfcClusterLinkIf clusterLinkIf;

  public MfcPhysicalLink() {
  }

  public Integer getClusterLinkIfId() {
    return this.clusterLinkIfId;
  }

  public void setClusterLinkIfId(Integer clusterLinkIfId) {
    this.clusterLinkIfId = clusterLinkIfId;
  }

  public String getBreakoutIfId() {
    return this.breakoutIfId;
  }

  public void setBreakoutIfId(String breakoutIfId) {
    this.breakoutIfId = breakoutIfId;
  }

  public Integer getNodeId() {
    return this.nodeId;
  }

  public void setNodeId(Integer nodeId) {
    this.nodeId = nodeId;
  }

  public String getOppositeBreakoutIfId() {
    return this.oppositeBreakoutIfId;
  }

  public void setOppositeBreakoutIfId(String oppositeBreakoutIfId) {
    this.oppositeBreakoutIfId = oppositeBreakoutIfId;
  }

  public Integer getOppositeNodeId() {
    return this.oppositeNodeId;
  }

  public void setOppositeNodeId(Integer oppositeNodeId) {
    this.oppositeNodeId = oppositeNodeId;
  }

  public String getOppositePhysicalIfId() {
    return this.oppositePhysicalIfId;
  }

  public void setOppositePhysicalIfId(String oppositePhysicalIfId) {
    this.oppositePhysicalIfId = oppositePhysicalIfId;
  }

  public String getPhysicalIfId() {
    return this.physicalIfId;
  }

  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
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
