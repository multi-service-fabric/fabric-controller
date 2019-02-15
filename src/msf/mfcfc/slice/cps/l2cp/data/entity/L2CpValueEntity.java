
package msf.mfcfc.slice.cps.l2cp.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.PortMode;

public class L2CpValueEntity {

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("edge_point_id")
  private String edgePointId;

  @SerializedName("vlan_id")
  private Integer vlanId;

  @SerializedName("pair_cp_id")
  private String pairCpId;

  @SerializedName("qos")
  private L2CpQosCreateEntity qos;

  @SerializedName("esi")
  private String esi;

  @SerializedName("lacp_system_id")
  private String lacpSystemId;

  @SerializedName("port_mode")
  private String portMode;

  @SerializedName("irb")
  private L2CpIrbEntity irb;

  @SerializedName("traffic_threshold")
  private Double trafficThreshold;

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

  public String getPairCpId() {
    return pairCpId;
  }

  public void setPairCpId(String pairCpId) {
    this.pairCpId = pairCpId;
  }

  public L2CpQosCreateEntity getQos() {
    return qos;
  }

  public void setQos(L2CpQosCreateEntity qos) {
    this.qos = qos;
  }

  public String getEsi() {
    return esi;
  }

  public void setEsi(String esi) {
    this.esi = esi;
  }

  public String getLacpSystemId() {
    return lacpSystemId;
  }

  public void setLacpSystemId(String lacpSystemId) {
    this.lacpSystemId = lacpSystemId;
  }

  public L2CpIrbEntity getIrb() {
    return irb;
  }

  public void setIrb(L2CpIrbEntity irb) {
    this.irb = irb;
  }

  public String getPortMode() {
    return portMode;
  }

  public void setPortMode(String portMode) {
    this.portMode = portMode;
  }

  public PortMode getPortModeEnum() {
    return PortMode.getEnumFromMessage(portMode);
  }

  public void setPortModeEnum(PortMode portMode) {
    this.portMode = portMode.getMessage();
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
