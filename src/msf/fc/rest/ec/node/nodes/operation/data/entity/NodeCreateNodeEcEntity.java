
package msf.fc.rest.ec.node.nodes.operation.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class NodeCreateNodeEcEntity {

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("host_name")
  private String hostName;

  @SerializedName("node_type")
  private String nodeType;

  @SerializedName("username")
  private String username;

  @SerializedName("password")
  private String password;

  @SerializedName("mac_addr")
  private String macAddr;

  @SerializedName("provisioning")
  private Boolean provisioning;

  @SerializedName("ntp_server_address")
  private String ntpServerAddress;

  @SerializedName("management_interface")
  private NodeManagementInterfaceEcEntity managementInterface;

  @SerializedName("loopback_interface")
  private NodeLoopbackInterfaceEcEntity loopbackInterface;

  @SerializedName("plane")
  private String plane;

  @SerializedName("snmp_community")
  private String snmpCommunity;

  @SerializedName("if")
  private NodeCreateNodeIfEcEntity createNodeIf;

  @SerializedName("opposite_nodes")
  private List<NodeOppositeNodeCreateEcEntity> oppositeNodeList;

  @SerializedName("vpn")
  private NodeVpnEcEntity vpn;

  @SerializedName("cluster_area")
  private String clusterArea;

  @SerializedName("irb_type")
  private String irbType;

  @SerializedName("q_in_q_type")
  private String qInQType;

  @SerializedName("virtual_link")
  private NodeVirtualLinkEcEntity virtualLink;

  @SerializedName("range")
  private NodeRangeEcEntity range;

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

  public Boolean getProvisioning() {
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

  public NodeManagementInterfaceEcEntity getManagementInterface() {
    return managementInterface;
  }

  public void setManagementInterface(NodeManagementInterfaceEcEntity managementInterface) {
    this.managementInterface = managementInterface;
  }

  public NodeLoopbackInterfaceEcEntity getLoopbackInterface() {
    return loopbackInterface;
  }

  public void setLoopbackInterface(NodeLoopbackInterfaceEcEntity loopbackInterface) {
    this.loopbackInterface = loopbackInterface;
  }

  public String getPlane() {
    return plane;
  }

  public void setPlane(String plane) {
    this.plane = plane;
  }

  public String getSnmpCommunity() {
    return snmpCommunity;
  }

  public void setSnmpCommunity(String snmpCommunity) {
    this.snmpCommunity = snmpCommunity;
  }

  public NodeCreateNodeIfEcEntity getCreateNodeIf() {
    return createNodeIf;
  }

  public void setCreateNodeIf(NodeCreateNodeIfEcEntity createNodeIf) {
    this.createNodeIf = createNodeIf;
  }

  public List<NodeOppositeNodeCreateEcEntity> getOppositeNodeList() {
    return oppositeNodeList;
  }

  public void setOppositeNodeList(List<NodeOppositeNodeCreateEcEntity> oppositeNodeList) {
    this.oppositeNodeList = oppositeNodeList;
  }

  public NodeVpnEcEntity getVpn() {
    return vpn;
  }

  public void setVpn(NodeVpnEcEntity vpn) {
    this.vpn = vpn;
  }

  public String getClusterArea() {
    return clusterArea;
  }

  public void setClusterArea(String clusterArea) {
    this.clusterArea = clusterArea;
  }

  public NodeVirtualLinkEcEntity getVirtualLink() {
    return virtualLink;
  }

  public void setVirtualLink(NodeVirtualLinkEcEntity virtualLink) {
    this.virtualLink = virtualLink;
  }

  public NodeRangeEcEntity getRange() {
    return range;
  }

  public void setRange(NodeRangeEcEntity range) {
    this.range = range;
  }

  public String getIrbType() {
    return irbType;
  }

  public void setIrbType(String irbType) {
    this.irbType = irbType;
  }

  public String getQInQType() {
    return qInQType;
  }

  public void setQInQType(String qInQType) {
    this.qInQType = qInQType;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
