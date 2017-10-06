package msf.fc.node.nodes.spines.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.node.nodes.spines.data.entity.InternalLinkSpineEntity;
import msf.fc.node.nodes.spines.data.entity.PhysicalLinkSpineEntity;
import msf.fc.rest.common.RestRequestValidator;

public class SpineNodeCreateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(SpineNodeCreateRequestBody.class);


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

  @SerializedName("password")
  private String password;

  @SerializedName("provisioning")
  private Boolean provisioning;


  @SerializedName("snmp_community")
  private String snmpCommunity;

  @SerializedName("ntp_server_address")
  private String ntpServerAddress;

  @SerializedName("internal_link")
  private InternalLinkSpineEntity internalLink;

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

  public InternalLinkSpineEntity getInternalLink() {
    return internalLink;
  }

  public void setInternalLink(InternalLinkSpineEntity internalLink) {
    this.internalLink = internalLink;
  }

  @Override
  public void validate() throws MsfException {

    try {
      logger.methodStart();

      ParameterCheckUtil.checkNumericId(nodeId, ErrorCode.PARAMETER_VALUE_ERROR);

      ParameterCheckUtil.checkNumericId(equipmentTypeId, ErrorCode.RELATED_RESOURCE_NOT_FOUND);

      ParameterCheckUtil.checkNotNullAndLength(hostName);

      macAddr = ParameterCheckUtil.checkMacAddress(macAddr);

      ParameterCheckUtil.checkNotNullAndLength(username);

      ParameterCheckUtil.checkNotNullAndLength(password);

      ParameterCheckUtil.checkNotNull(provisioning);

      ParameterCheckUtil.checkNotNullAndLength(snmpCommunity);

      ntpServerAddress = ParameterCheckUtil.checkIpAddress(ntpServerAddress);

      ParameterCheckUtil.checkNotNull(internalLink);

      validateInternalLink();

    } finally {
      logger.methodEnd();
    }

  }

  private void validateInternalLink() throws MsfException {

    ParameterCheckUtil.checkNotNull(internalLink);

    ParameterCheckUtil.checkNotNull(internalLink.getPhysicalLinkList());

    validatePhysicalLinkList();

  }

  private void validatePhysicalLinkList() throws MsfException {

    for (PhysicalLinkSpineEntity tempPLink : internalLink.getPhysicalLinkList()) {

      ParameterCheckUtil.checkIdSpecifiedByUri(tempPLink.getLocalIfId());

      ParameterCheckUtil.checkNotNullAndLength(tempPLink.getSpeed());

      ParameterCheckUtil.checkNotNullAndLength(tempPLink.getRemoteLeafNodeId());

      ParameterCheckUtil.checkIdSpecifiedByUri(tempPLink.getRemoteIfId());

    }

  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
