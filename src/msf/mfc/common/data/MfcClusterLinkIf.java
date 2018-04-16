
package msf.mfc.common.data;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "cluster_link_ifs")
@NamedQuery(name = "MfcClusterLinkIf.findAll", query = "SELECT m FROM MfcClusterLinkIf m")
public class MfcClusterLinkIf implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "cluster_link_if_id")
  private Integer clusterLinkIfId;

  @Column(name = "igp_cost")
  private Integer igpCost;

  @Column(name = "ipv4_addr")
  private String ipv4Addr;

  @Column(name = "opposite_sw_cluster_id")
  private Integer oppositeSwClusterId;

  @Column(name = "port_status")
  private Boolean portStatus;

  @Column(name = "traffic_threshold")
  private Double trafficThreshold;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sw_cluster_id")
  private MfcSwCluster swCluster;

  @OneToOne(mappedBy = "clusterLinkIf", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private MfcLagLink lagLink;

  @OneToOne(mappedBy = "clusterLinkIf", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private MfcPhysicalLink physicalLink;

  public MfcClusterLinkIf() {
  }

  public Integer getClusterLinkIfId() {
    return this.clusterLinkIfId;
  }

  public void setClusterLinkIfId(Integer clusterLinkIfId) {
    this.clusterLinkIfId = clusterLinkIfId;
  }

  public Integer getIgpCost() {
    return this.igpCost;
  }

  public void setIgpCost(Integer igpCost) {
    this.igpCost = igpCost;
  }

  public String getIpv4Addr() {
    return this.ipv4Addr;
  }

  public void setIpv4Addr(String ipv4Addr) {
    this.ipv4Addr = ipv4Addr;
  }

  public Integer getOppositeSwClusterId() {
    return this.oppositeSwClusterId;
  }

  public void setOppositeSwClusterId(Integer oppositeSwClusterId) {
    this.oppositeSwClusterId = oppositeSwClusterId;
  }

  public Boolean getPortStatus() {
    return this.portStatus;
  }

  public void setPortStatus(Boolean portStatus) {
    this.portStatus = portStatus;
  }

  public Double getTrafficThreshold() {
    return this.trafficThreshold;
  }

  public void setTrafficThreshold(Double trafficThreshold) {
    this.trafficThreshold = trafficThreshold;
  }

  public MfcSwCluster getSwCluster() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.swCluster);
  }

  public void setSwCluster(MfcSwCluster swCluster) {
    this.swCluster = swCluster;
  }

  public MfcLagLink getLagLink() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.lagLink);
  }

  public void setLagLink(MfcLagLink lagLink) {
    this.lagLink = lagLink;
  }

  public MfcPhysicalLink getPhysicalLink() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.physicalLink);
  }

  public void setPhysicalLink(MfcPhysicalLink physicalLink) {
    this.physicalLink = physicalLink;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "swCluster", "lagLink", "physicalLink" }).toString();
  }

}
