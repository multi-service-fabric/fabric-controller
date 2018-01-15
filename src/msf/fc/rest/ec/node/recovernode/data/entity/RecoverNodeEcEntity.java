package msf.fc.rest.ec.node.recovernode.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class RecoverNodeEcEntity {

  
  @SerializedName("node_id")
  private String nodeId;

  
  @SerializedName("fabric_type")
  private String fabricType;

  
  @SerializedName("username")
  private String username;

  
  @SerializedName("password")
  private String password;

  
  @SerializedName("mac_addr")
  private String macAddr;

  
  public String getNodeId() {
    return nodeId;
  }

  
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  
  public String getFabricType() {
    return fabricType;
  }

  
  public void setFabricType(String fabricType) {
    this.fabricType = fabricType;
  }

  
  public String getUsername() {
    return username;
  }

  
  public void setUsername(String username) {
    this.username = username;
  }

  
  public String getPassword() {
    return password;
  }

  
  public void setPassword(String password) {
    this.password = password;
  }

  
  public String getMacAddr() {
    return macAddr;
  }

  
  public void setMacAddr(String macAddr) {
    this.macAddr = macAddr;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
