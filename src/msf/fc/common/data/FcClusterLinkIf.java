
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "cluster_link_ifs")
@NamedQuery(name = "FcClusterLinkIf.findAll", query = "SELECT f FROM FcClusterLinkIf f")
public class FcClusterLinkIf implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "cluster_link_if_id")
  private Long clusterLinkIfId;

  @Column(name = "igp_cost")
  private Integer igpCost;

  @Column(name = "ipv4_addr")
  private String ipv4Addr;

  @Column(name = "traffic_threshold")
  private Double trafficThreshold;

  @Column(name = "port_status")
  private Boolean portStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "breakout_if_info_id")
  private FcBreakoutIf breakoutIf;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lag_if_info_id")
  private FcLagIf lagIf;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "physical_if_info_id")
  private FcPhysicalIf physicalIf;

  public FcClusterLinkIf() {
  }

  public Long getClusterLinkIfId() {
    return this.clusterLinkIfId;
  }

  public void setClusterLinkIfId(Long clusterLinkIfId) {
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

  public Double getTrafficThreshold() {
    return this.trafficThreshold;
  }

  public void setTrafficThreshold(Double trafficThreshold) {
    this.trafficThreshold = trafficThreshold;
  }

  public Boolean getPortStatus() {
    return this.portStatus;
  }

  public void setPortStatus(Boolean portStatus) {
    this.portStatus = portStatus;
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

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "breakoutIf", "lagIf", "physicalIf" }).toString();
  }

}
