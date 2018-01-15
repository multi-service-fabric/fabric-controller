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

  
  @SerializedName("qos")
  private OperationQosEcEntity qos;

  
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

  
  public OperationQosEcEntity getQos() {
    return qos;
  }

  
  public void setQos(OperationQosEcEntity qos) {
    this.qos = qos;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
