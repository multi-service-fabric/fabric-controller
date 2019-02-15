
package msf.fc.rest.ec.core.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationCreateVlanIfEcEntity {

  @SerializedName("vlan_if_id")
  private String vlanIfId;

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("base_if")
  private OperationBaseIfEcEntity baseIf;

  @SerializedName("vlan_id")
  private Integer vlanId;

  @SerializedName("port_mode")
  private String portMode;

  @SerializedName("esi")
  private String esi;

  @SerializedName("lacp_system_id")
  private String lacpSystemId;

  @SerializedName("clag_id")
  private Integer clagId;

  @SerializedName("qos")
  private OperationQosEcEntity qos;

  @SerializedName("irb")
  private OperationIrbEcEntity irb;

  @SerializedName("route_distinguisher")
  private String routeDistinguisher;

  @SerializedName("is_dummy")
  private Boolean isDummy;

  @SerializedName("multi_homing")
  private OperationMultiHomingEcEntity multiHoming;

  public String getVlanIfId() {
    return vlanIfId;
  }

  public void setVlanIfId(String vlanIfId) {
    this.vlanIfId = vlanIfId;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public OperationBaseIfEcEntity getBaseIf() {
    return baseIf;
  }

  public void setBaseIf(OperationBaseIfEcEntity baseIf) {
    this.baseIf = baseIf;
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

  public Integer getClagId() {
    return clagId;
  }

  public void setClagId(Integer clagId) {
    this.clagId = clagId;
  }

  public OperationQosEcEntity getQos() {
    return qos;
  }

  public void setQos(OperationQosEcEntity qos) {
    this.qos = qos;
  }

  public OperationIrbEcEntity getIrb() {
    return irb;
  }

  public void setIrb(OperationIrbEcEntity irb) {
    this.irb = irb;
  }

  public String getRouteDistinguisher() {
    return routeDistinguisher;
  }

  public void setRouteDistinguisher(String routeDistinguisher) {
    this.routeDistinguisher = routeDistinguisher;
  }

  public Boolean getIsDummy() {
    return isDummy;
  }

  public void setIsDummy(Boolean isDummy) {
    this.isDummy = isDummy;
  }

  public OperationMultiHomingEcEntity getMultiHoming() {
    return multiHoming;
  }

  public void setMultiHoming(OperationMultiHomingEcEntity multiHoming) {
    this.multiHoming = multiHoming;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
