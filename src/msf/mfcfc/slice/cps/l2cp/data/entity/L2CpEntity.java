
package msf.mfcfc.slice.cps.l2cp.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.PortMode;

public class L2CpEntity {

  @SerializedName("cp_id")
  private String cpId;

  @SerializedName("pair_cp_id")
  private String pairCpId;

  @SerializedName("slice_id")
  private String sliceId;

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("edge_point_id")
  private String edgePointId;

  @SerializedName("vlan_id")
  private Integer vlanId;

  @SerializedName("port_mode")
  private String portMode;

  @SerializedName("qos")
  private L2CpQosEntity qos;

  @SerializedName("irb")
  private L2CpIrbEntity irb;

  @SerializedName("traffic_threshold")
  private Double trafficThreshold;

  public String getCpId() {
    return cpId;
  }

  public void setCpId(String cpId) {
    this.cpId = cpId;
  }

  public String getPairCpId() {
    return pairCpId;
  }

  public void setPairCpId(String pairCpId) {
    this.pairCpId = pairCpId;
  }

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getEdgePointId() {
    return edgePointId;
  }

  public void setEdgePointId(String edgePointId) {
    this.edgePointId = edgePointId;
  }

  public Integer getVlanId() {
    return vlanId;
  }

  public void setVlanId(Integer vlanId) {
    this.vlanId = vlanId;
  }

  public String getPortMode() {
    return portMode;
  }

  public void setPortMode(String portMode) {
    this.portMode = portMode;
  }

  public L2CpQosEntity getQos() {
    return qos;
  }

  public void setQos(L2CpQosEntity qos) {
    this.qos = qos;
  }

  public PortMode getPortModeEnum() {
    return PortMode.getEnumFromMessage(portMode);
  }

  public void setPortModeEnum(PortMode portMode) {
    this.portMode = portMode.getMessage();
  }

  public L2CpIrbEntity getIrb() {
    return irb;
  }

  public void setIrb(L2CpIrbEntity irb) {
    this.irb = irb;
  }

  public Double getTrafficThreshold() {
    return trafficThreshold;
  }

  public void setTrafficThreshold(Double trafficThreshold) {
    this.trafficThreshold = trafficThreshold;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
