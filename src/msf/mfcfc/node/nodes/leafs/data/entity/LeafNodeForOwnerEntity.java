
package msf.mfcfc.node.nodes.leafs.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.node.interfaces.internalifs.data.entity.InternalLinkIfEntity;

public class LeafNodeForOwnerEntity {

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("equipment_type_id")
  private String equipmentTypeId;

  @SerializedName("leaf_type")
  private String leafType;

  @SerializedName("host_name")
  private String hostName;

  @SerializedName("mac_address")
  private String macAddress;

  @SerializedName("username")
  private String username;

  @SerializedName("provisioning")
  private Boolean provisioning;

  @SerializedName("vpn_type")
  private String vpnType;

  @SerializedName("irb_type")
  private String irbType;

  @SerializedName("plane")
  private Integer plane;

  @SerializedName("snmp_community")
  private String snmpCommunity;

  @SerializedName("ntp_server_address")
  private String ntpServerAddress;

  @SerializedName("physical_ifs")
  private List<LeafNodePhysicalIfForOwnerEntity> physicalIfList;

  @SerializedName("breakout_ifs")
  private List<LeafNodeBreakoutIfForOwnerEntity> breakoutIfList;

  @SerializedName("internal_link_ifs")
  private List<InternalLinkIfEntity> internalLinkIfList;

  @SerializedName("lag_ifs")
  private List<LeafNodeLagIfForOwnerEntity> lagIfList;

  @SerializedName("router_id")
  private String routerId;

  @SerializedName("management_if_address")
  private String managementIfAddress;

  @SerializedName("provisioning_status")
  private String provisioningStatus;

  @SerializedName("registered_rr_node_ids")
  private List<String> registeredRrNodeIdList;

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

  public String getLeafType() {
    return leafType;
  }

  public void setLeafType(String leafType) {
    this.leafType = leafType;
  }

  public String getHostName() {
    return hostName;
  }

  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  public String getMacAddress() {
    return macAddress;
  }

  public void setMacAddress(String macAddress) {
    this.macAddress = macAddress;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Boolean getProvisioning() {
    return provisioning;
  }

  public void setProvisioning(Boolean provisioning) {
    this.provisioning = provisioning;
  }

  public String getVpnType() {
    return vpnType;
  }

  public void setVpnType(String vpnType) {
    this.vpnType = vpnType;
  }

  public String getIrbType() {
    return irbType;
  }

  public void setIrbType(String irbType) {
    this.irbType = irbType;
  }

  public Integer getPlane() {
    return plane;
  }

  public void setPlane(Integer plane) {
    this.plane = plane;
  }

  public String getSnmpCommunity() {
    return snmpCommunity;
  }

  public void setSnmpCommunity(String snmpCommunity) {
    this.snmpCommunity = snmpCommunity;
  }

  public String getNtpServerAddress() {
    return ntpServerAddress;
  }

  public void setNtpServerAddress(String ntpServerAddress) {
    this.ntpServerAddress = ntpServerAddress;
  }

  public List<LeafNodePhysicalIfForOwnerEntity> getPhysicalIfList() {
    return physicalIfList;
  }

  public void setPhysicalIfList(List<LeafNodePhysicalIfForOwnerEntity> physicalIfList) {
    this.physicalIfList = physicalIfList;
  }

  public List<LeafNodeBreakoutIfForOwnerEntity> getBreakoutIfList() {
    return breakoutIfList;
  }

  public void setBreakoutIfList(List<LeafNodeBreakoutIfForOwnerEntity> breakoutIfList) {
    this.breakoutIfList = breakoutIfList;
  }

  public List<InternalLinkIfEntity> getInternalLinkIfList() {
    return internalLinkIfList;
  }

  public void setInternalLinkIfList(List<InternalLinkIfEntity> internalLinkIfList) {
    this.internalLinkIfList = internalLinkIfList;
  }

  public List<LeafNodeLagIfForOwnerEntity> getLagIfList() {
    return lagIfList;
  }

  public void setLagIfList(List<LeafNodeLagIfForOwnerEntity> lagIfList) {
    this.lagIfList = lagIfList;
  }

  public String getRouterId() {
    return routerId;
  }

  public void setRouterId(String routerId) {
    this.routerId = routerId;
  }

  public String getManagementIfAddress() {
    return managementIfAddress;
  }

  public void setManagementIfAddress(String managementIfAddress) {
    this.managementIfAddress = managementIfAddress;
  }

  public String getProvisioningStatus() {
    return provisioningStatus;
  }

  public void setProvisioningStatus(String provisioningStatus) {
    this.provisioningStatus = provisioningStatus;
  }

  public List<String> getRegisteredRrNodeIdList() {
    return registeredRrNodeIdList;
  }

  public void setRegisteredRrNodeIdList(List<String> registeredRrNodeIdList) {
    this.registeredRrNodeIdList = registeredRrNodeIdList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
