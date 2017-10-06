package msf.fc.rest.ec.node.nodes.spine.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class NodeEcEntity {
  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("host_name")
  private String hostName;

  @SerializedName("mac_addr")
  private String macAddr;

  @SerializedName("ntp_server_address")
  private String ntpServerAddress;

  @SerializedName("management_interface")
  private ManagementInterfaceEcEntity managementInterface;

  @SerializedName("snmp_community")
  private String snmpCommunity;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
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

  public ManagementInterfaceEcEntity getManagementInterface() {
    return managementInterface;
  }

  public void setManagementInterface(ManagementInterfaceEcEntity managementInterface) {
    this.managementInterface = managementInterface;
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
