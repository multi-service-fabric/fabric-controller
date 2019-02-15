
package msf.fc.rest.ec.core.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationUpdateVlanIfEcEntity {

  @SerializedName("vlan_if_id")
  private String vlanIfId;

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("base_if")
  private OperationBaseIfEcEntity baseIf;

  @SerializedName("esi")
  private String esi;

  @SerializedName("lacp_system_id")
  private String lacpSystemId;

  @SerializedName("clag_id")
  private Integer clagId;

  @SerializedName("port_mode")
  private String portMode;

  @SerializedName("qos")
  private OperationQosEcEntity qos;

  @SerializedName("irb")
  private OperationIrbEcEntity irb;

  @SerializedName("dummy_flag")
  private Boolean dummyFlag;

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

  public String getPortMode() {
    return portMode;
  }

  public void setPortMode(String portMode) {
    this.portMode = portMode;
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

  public Boolean getDummyFlag() {
    return dummyFlag;
  }

  public void setDummyFlag(Boolean dummyFlag) {
    this.dummyFlag = dummyFlag;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
