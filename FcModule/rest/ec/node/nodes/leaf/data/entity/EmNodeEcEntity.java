package msf.fc.rest.ec.node.nodes.leaf.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EmNodeEcEntity {
  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("username")
  private String username;

  @SerializedName("password")
  private String password;

  @SerializedName("provisioning")
  private Boolean provisioning;

  @SerializedName("ntp_server_address")
  private String ntpServerAddress;

  @SerializedName("management_interface")
  private ManagementInterfaceEcEntity managementInterface;

  @SerializedName("loopback_interface")
  private LoopbackInterfaceEcEntity loopbackInterface;

  @SerializedName("snmp_community")
  private String snmpCommunity;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
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

  public Boolean isProvisioning() {
    return provisioning;
  }

  public void setProvisioning(Boolean provisioning) {
    this.provisioning = provisioning;
  }

  public String getNtpServerAddress() {
    return ntpServerAddress;
  }

  public void setNtpServerAddress(String ntpServerAddress) {
    this.ntpServerAddress = ntpServerAddress;
  }

  public ManagementInterfaceEcEntity getManagementInterface() {
    return managementInterface;
  }

  public void setManagementInterface(ManagementInterfaceEcEntity managementInterface) {
    this.managementInterface = managementInterface;
  }

  public LoopbackInterfaceEcEntity getLoopbackInterface() {
    return loopbackInterface;
  }

  public void setLoopbackInterface(LoopbackInterfaceEcEntity loopbackInterface) {
    this.loopbackInterface = loopbackInterface;
  }

  public String getSnmpCommunity() {
    return snmpCommunity;
  }

  public void setSnmpCommunity(String snmpCommunity) {
    this.snmpCommunity = snmpCommunity;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
