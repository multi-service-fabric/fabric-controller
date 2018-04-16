
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

  @SerializedName("esi")
  private String esi;

  @SerializedName("lacp_system_id")
  private String lacpSystemId;

  @SerializedName("qos")
  private L2CpQosEntity qos;

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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
