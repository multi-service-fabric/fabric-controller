
package msf.fc.rest.ec.node.recovernode.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class RecoverNodeEcEntity {

  @SerializedName("node_type")
  private String nodeType;

  @SerializedName("username")
  private String username;

  @SerializedName("password")
  private String password;

  @SerializedName("mac_addr")
  private String macAddr;

  public String getNodeType() {
    return nodeType;
  }

  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
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
