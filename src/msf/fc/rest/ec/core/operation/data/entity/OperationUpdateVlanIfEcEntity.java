package msf.fc.rest.ec.core.operation.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class OperationUpdateVlanIfEcEntity {

  
  @SerializedName("vlan_if_id")
  private String vlanIfId;

  
  @SerializedName("node_id")
  private String nodeId;

  
  @SerializedName("esi")
  private String esi;

  
  @SerializedName("lacp_system_id")
  private String lacpSystemId;

  
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

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
