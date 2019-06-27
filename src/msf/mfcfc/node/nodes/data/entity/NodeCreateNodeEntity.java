
package msf.mfcfc.node.nodes.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.InternalNodeType;
import msf.mfcfc.common.constant.IrbType;
import msf.mfcfc.common.constant.QInQType;

public class NodeCreateNodeEntity {

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
  private NodeManagementIfEntity managementInterface;

  @SerializedName("loopback_interface")
  private NodeLoopbackIfEntity loopbackInterface;

  @SerializedName("plane")
  private String plane;

  @SerializedName("snmp_community")
  private String snmpCommunity;

  @SerializedName("if")
  private NodeCreateNodeIfEntity createNodeIf;

  @SerializedName("opposite_nodes")
  private List<NodeOppositeNodeEntity> oppositeNodeList;

  @SerializedName("vpn")
  private NodeVpnEntity vpn;

  @SerializedName("irb_type")
  private String irbType;

  @SerializedName("q_in_q_type")
  private String qInQType;

  @SerializedName("cluster_area")
  private String clusterArea;

  @SerializedName("virtual_link")
  private NodeVirtualLinkEntity virtualLink;

  @SerializedName("range")
  private NodeRangeEntity range;

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

  public NodeManagementIfEntity getManagementInterface() {
    return managementInterface;
  }

  public void setManagementInterface(NodeManagementIfEntity managementInterface) {
    this.managementInterface = managementInterface;
  }

  public NodeLoopbackIfEntity getLoopbackInterface() {
    return loopbackInterface;
  }

  public void setLoopbackInterface(NodeLoopbackIfEntity loopbackInterface) {
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

  public NodeCreateNodeIfEntity getCreateNodeIf() {
    return createNodeIf;
  }

  public void setCreateNodeIf(NodeCreateNodeIfEntity createNodeIf) {
    this.createNodeIf = createNodeIf;
  }

  public List<NodeOppositeNodeEntity> getOppositeNodeList() {
    return oppositeNodeList;
  }

  public void setOppositeNodeList(List<NodeOppositeNodeEntity> oppositeNodeList) {
    this.oppositeNodeList = oppositeNodeList;
  }

  public NodeVpnEntity getVpn() {
    return vpn;
  }

  public void setVpn(NodeVpnEntity vpn) {
    this.vpn = vpn;
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

  public String getClusterArea() {
    return clusterArea;
  }

  public void setClusterArea(String clusterArea) {
    this.clusterArea = clusterArea;
  }

  public NodeVirtualLinkEntity getVirtualLink() {
    return virtualLink;
  }

  public void setVirtualLink(NodeVirtualLinkEntity virtualLink) {
    this.virtualLink = virtualLink;
  }

  public NodeRangeEntity getRange() {
    return range;
  }

  public void setRange(NodeRangeEntity range) {
    this.range = range;
  }

  public InternalNodeType getNodeTypeEnum() {
    return InternalNodeType.getEnumFromMessage(nodeType);
  }

  public void setNodeTypeEnum(InternalNodeType internalNodeType) {
    this.nodeType = internalNodeType.getMessage();
  }

  public IrbType getIrbTypeEnum() {
    return IrbType.getEnumFromMessage(irbType);
  }

  public void setIrbTypeEnum(IrbType irbType) {
    this.irbType = irbType.getMessage();
  }

  public QInQType getQInQTypeEnum() {
    return QInQType.getEnumFromMessage(qInQType);
  }

  public void setQInQTypeEnum(QInQType qInQType) {
    this.qInQType = qInQType.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
