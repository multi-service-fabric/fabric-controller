package msf.fc.node.nodes.spines.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.ProvisioningStatus;
import msf.fc.node.interfaces.internalifs.data.entity.InternalIfEntity;

public class SpineEntity {
  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("equipment_type_id")
  private String equipmentTypeId;

  @SerializedName("host_name")
  private String hostName;

  @SerializedName("mac_addr")
  private String macAddr;

  @SerializedName("username")
  private String username;

  @SerializedName("provisioning")
  private Boolean provisioning;

  @SerializedName("snmp_community")
  private String snmpCommunity;

  @SerializedName("ntp_server_address")
  private String ntpServerAddress;

  @SerializedName("physical_ifs")
  private List<PhysicalIfSpineEntity> physicalIfList;

  @SerializedName("internal_link_ifs")
  private List<InternalIfEntity> internalLinkIfList;

  @SerializedName("lag_ifs")
  private List<LagIfSpineEntity> lagIfList;

  @SerializedName("rp_flag")
  private Boolean rpFlag;

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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Boolean isProvisioning() {
    return provisioning;
  }

  public void setProvisioning(Boolean provisioning) {
    this.provisioning = provisioning;
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

  public List<PhysicalIfSpineEntity> getPhysicalIfList() {
    return physicalIfList;
  }

  public void setPhysicalIfList(List<PhysicalIfSpineEntity> physicalIfList) {
    this.physicalIfList = physicalIfList;
  }

  public List<InternalIfEntity> getInternalLinkIfList() {
    return internalLinkIfList;
  }

  public void setInternalLinkIfList(List<InternalIfEntity> internalLinkIfList) {
    this.internalLinkIfList = internalLinkIfList;
  }

  public List<LagIfSpineEntity> getLagIfList() {
    return lagIfList;
  }

  public void setLagIfList(List<LagIfSpineEntity> lagIfList) {
    this.lagIfList = lagIfList;
  }

  public Boolean isRpFlag() {
    return rpFlag;
  }

  public void setRpFlag(Boolean rpFlag) {
    this.rpFlag = rpFlag;
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

  public ProvisioningStatus getProvisioningStatusEnum() {
    return ProvisioningStatus.getEnumFromMessage(provisioningStatus);
  }

  public void setProvisioningStatusEnum(ProvisioningStatus provisioningStatus) {
    this.provisioningStatus = provisioningStatus.getMessage();
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
