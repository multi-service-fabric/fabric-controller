
package msf.fc.rest.ec.node.nodes.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class NodeEcEntity {

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("equipment_type_id")
  private String equipmentTypeId;

  @SerializedName("node_state")
  private String nodeState;

  @SerializedName("management_if_address")
  private String managementIfAddress;

  @SerializedName("loopback_if_address")
  private String loopbackIfAddress;

  @SerializedName("snmp_community")
  private String snmpCommunity;

  @SerializedName("host_name")
  private String hostName;

  @SerializedName("mac_addr")
  private String macAddr;

  @SerializedName("ntp_server_address")
  private String ntpServerAddress;

  @SerializedName("vpn_type")
  private String vpnType;

  @SerializedName("username")
  private String username;

  @SerializedName("password")
  private String password;

  @SerializedName("provisioning")
  private Boolean provisioning;

  @SerializedName("plane")
  private String plane;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getEquipmentTypeId() {
    return equipmentTypeId;
  }

  public void setEquipmentTypeId(String equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  public String getNodeState() {
    return nodeState;
  }

  public void setNodeState(String nodeState) {
    this.nodeState = nodeState;
  }

  public String getManagementIfAddress() {
    return managementIfAddress;
  }

  public void setManagementIfAddress(String managementIfAddress) {
    this.managementIfAddress = managementIfAddress;
  }

  public String getLoopbackIfAddress() {
    return loopbackIfAddress;
  }

  public void setLoopbackIfAddress(String loopbackIfAddress) {
    this.loopbackIfAddress = loopbackIfAddress;
  }

  public String getSnmpCommunity() {
    return snmpCommunity;
  }

  public void setSnmpCommunity(String snmpCommunity) {
    this.snmpCommunity = snmpCommunity;
  }

  public String getHostName() {
    return hostName;
  }

  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  public String getMacAddr() {
    return macAddr;
  }

  public void setMacAddr(String macAddr) {
    this.macAddr = macAddr;
  }

  public String getNtpServerAddress() {
    return ntpServerAddress;
  }

  public void setNtpServerAddress(String ntpServerAddress) {
    this.ntpServerAddress = ntpServerAddress;
  }

  public String getVpnType() {
    return vpnType;
  }

  public void setVpnType(String vpnType) {
    this.vpnType = vpnType;
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

  public Boolean getProvisioning() {
    return provisioning;
  }

  public void setProvisioning(Boolean provisioning) {
    this.provisioning = provisioning;
  }

  public String getPlane() {
    return plane;
  }

  public void setPlane(String plane) {
    this.plane = plane;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
