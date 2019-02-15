
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "sw_clusters")
@NamedQuery(name = "FcSwCluster.findAll", query = "SELECT f FROM FcSwCluster f")
public class FcSwCluster implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "sw_cluster_id")
  private Integer swClusterId;

  @Column(name = "as_num")
  private Integer asNum;

  @Column(name = "ec_control_address")
  private String ecControlAddress;

  @Column(name = "ec_control_port")
  private Integer ecControlPort;

  @Column(name = "ec_start_pos")
  private Integer ecStartPos;

  @Column(name = "em_start_pos")
  private Integer emStartPos;

  @Column(name = "fc_start_pos")
  private Integer fcStartPos;

  @Column(name = "inchannel_start_address")
  private String inchannelStartAddress;

  @Column(name = "internal_link_normal_igp_cost")
  private Integer internalLinkNormalIgpCost;

  @Column(name = "leaf_start_pos")
  private Integer leafStartPos;

  @Column(name = "max_leaf_num")
  private Integer maxLeafNum;

  @Column(name = "max_rr_num")
  private Integer maxRrNum;

  @Column(name = "max_spine_num")
  private Integer maxSpineNum;

  @Column(name = "ospf_area")
  private Integer ospfArea;

  @Column(name = "outchannel_start_address")
  private String outchannelStartAddress;

  @Column(name = "route_aggregation_address_prefix")
  private Integer routeAggregationAddressPrefix;

  @Column(name = "route_aggregation_start_address")
  private String routeAggregationStartAddress;

  @Column(name = "rr_start_pos")
  private Integer rrStartPos;

  @Column(name = "spine_start_pos")
  private Integer spineStartPos;

  public FcSwCluster() {
  }

  public Integer getSwClusterId() {
    return this.swClusterId;
  }

  public void setSwClusterId(Integer swClusterId) {
    this.swClusterId = swClusterId;
  }

  public Integer getAsNum() {
    return this.asNum;
  }

  public void setAsNum(Integer asNum) {
    this.asNum = asNum;
  }

  public String getEcControlAddress() {
    return this.ecControlAddress;
  }

  public void setEcControlAddress(String ecControlAddress) {
    this.ecControlAddress = ecControlAddress;
  }

  public Integer getEcControlPort() {
    return this.ecControlPort;
  }

  public void setEcControlPort(Integer ecControlPort) {
    this.ecControlPort = ecControlPort;
  }

  public Integer getEcStartPos() {
    return this.ecStartPos;
  }

  public void setEcStartPos(Integer ecStartPos) {
    this.ecStartPos = ecStartPos;
  }

  public Integer getEmStartPos() {
    return this.emStartPos;
  }

  public void setEmStartPos(Integer emStartPos) {
    this.emStartPos = emStartPos;
  }

  public Integer getFcStartPos() {
    return this.fcStartPos;
  }

  public void setFcStartPos(Integer fcStartPos) {
    this.fcStartPos = fcStartPos;
  }

  public String getInchannelStartAddress() {
    return this.inchannelStartAddress;
  }

  public void setInchannelStartAddress(String inchannelStartAddress) {
    this.inchannelStartAddress = inchannelStartAddress;
  }

  public Integer getInternalLinkNormalIgpCost() {
    return this.internalLinkNormalIgpCost;
  }

  public void setInternalLinkNormalIgpCost(Integer internalLinkNormalIgpCost) {
    this.internalLinkNormalIgpCost = internalLinkNormalIgpCost;
  }

  public Integer getLeafStartPos() {
    return this.leafStartPos;
  }

  public void setLeafStartPos(Integer leafStartPos) {
    this.leafStartPos = leafStartPos;
  }

  public Integer getMaxLeafNum() {
    return this.maxLeafNum;
  }

  public void setMaxLeafNum(Integer maxLeafNum) {
    this.maxLeafNum = maxLeafNum;
  }

  public Integer getMaxRrNum() {
    return this.maxRrNum;
  }

  public void setMaxRrNum(Integer maxRrNum) {
    this.maxRrNum = maxRrNum;
  }

  public Integer getMaxSpineNum() {
    return this.maxSpineNum;
  }

  public void setMaxSpineNum(Integer maxSpineNum) {
    this.maxSpineNum = maxSpineNum;
  }

  public Integer getOspfArea() {
    return this.ospfArea;
  }

  public void setOspfArea(Integer ospfArea) {
    this.ospfArea = ospfArea;
  }

  public String getOutchannelStartAddress() {
    return this.outchannelStartAddress;
  }

  public void setOutchannelStartAddress(String outchannelStartAddress) {
    this.outchannelStartAddress = outchannelStartAddress;
  }

  public Integer getRouteAggregationAddressPrefix() {
    return this.routeAggregationAddressPrefix;
  }

  public void setRouteAggregationAddressPrefix(Integer routeAggregationAddressPrefix) {
    this.routeAggregationAddressPrefix = routeAggregationAddressPrefix;
  }

  public String getRouteAggregationStartAddress() {
    return this.routeAggregationStartAddress;
  }

  public void setRouteAggregationStartAddress(String routeAggregationStartAddress) {
    this.routeAggregationStartAddress = routeAggregationStartAddress;
  }

  public Integer getRrStartPos() {
    return this.rrStartPos;
  }

  public void setRrStartPos(Integer rrStartPos) {
    this.rrStartPos = rrStartPos;
  }

  public Integer getSpineStartPos() {
    return this.spineStartPos;
  }

  public void setSpineStartPos(Integer spineStartPos) {
    this.spineStartPos = spineStartPos;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
