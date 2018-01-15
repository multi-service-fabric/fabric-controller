
package msf.mfcfc.node.nodes.spines.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeBaseIfEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeBreakoutEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeBreakoutIfCreateEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeInternalBreakoutIfEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeInternalLinkEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeInternalLinkIfEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeInternalLocalEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeInternalOppositeEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeLagLinkEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeMemberIfEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodeOppositeEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodePhysicalIfCreateEntity;
import msf.mfcfc.node.nodes.spines.data.entity.SpineNodePhysicalLinkEntity;
import msf.mfcfc.rest.common.RestRequestValidator;


public class SpineNodeCreateRequestBody implements RestRequestValidator {
  
  private static final MsfLogger logger = MsfLogger.getInstance(SpineNodeCreateRequestBody.class);
  
  @SerializedName("node_id")
  private String nodeId;
  
  @SerializedName("equipment_type_id")
  private String equipmentTypeId;

  
  @SerializedName("host_name")
  private String hostName;

  
  @SerializedName("mac_address")
  private String macAddress;

  
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

  
  @SerializedName("breakout")
  private SpineNodeBreakoutEntity breakout;

  
  @SerializedName("internal_links")
  private SpineNodeInternalLinkEntity internalLinks;

  
  @SerializedName("management_if_address")
  private String managementIfAddress;

  
  @SerializedName("management_if_prefix")
  private Integer managementIfPrefix;

  
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

  
  public SpineNodeBreakoutEntity getBreakout() {
    return breakout;
  }

  
  public void setBreakout(SpineNodeBreakoutEntity breakout) {
    this.breakout = breakout;
  }

  
  public SpineNodeInternalLinkEntity getInternalLinks() {
    return internalLinks;
  }

  
  public void setInternalLinks(SpineNodeInternalLinkEntity internalLinks) {
    this.internalLinks = internalLinks;
  }

  
  public String getManagementIfAddress() {
    return managementIfAddress;
  }

  
  public void setManagementIfAddress(String managementIfAddress) {
    this.managementIfAddress = managementIfAddress;
  }

  
  public Integer getManagementIfPrefix() {
    return managementIfPrefix;
  }

  
  public void setManagementIfPrefix(Integer managementIfPrefix) {
    this.managementIfPrefix = managementIfPrefix;
  }

  @Override
  public void validate() throws MsfException {

    try {
      logger.methodStart();

      ParameterCheckUtil.checkNumericId(nodeId, ErrorCode.PARAMETER_VALUE_ERROR);

      ParameterCheckUtil.checkNumericId(equipmentTypeId, ErrorCode.RELATED_RESOURCE_NOT_FOUND);

      ParameterCheckUtil.checkNotNullAndLength(hostName);

      macAddress = ParameterCheckUtil.checkMacAddress(macAddress);

      ParameterCheckUtil.checkNotNullAndLength(username);

      ParameterCheckUtil.checkNotNullAndLength(password);

      ParameterCheckUtil.checkNotNull(provisioning);

      ParameterCheckUtil.checkNotNullAndLength(snmpCommunity);

      ntpServerAddress = ParameterCheckUtil.checkIpAddress(ntpServerAddress);

      validateBreakout();

      validateInternalLink();
      if (managementIfAddress != null) {

        managementIfAddress = ParameterCheckUtil.checkIpv4Address(managementIfAddress);

        ParameterCheckUtil.checkNotNull(managementIfPrefix);
        ParameterCheckUtil.checkNumberRange(managementIfPrefix, 0, 32);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void validateBreakout() throws MsfException {

    if (breakout != null) {
      validateBreakoutLocal();
      if (breakout.getOppositeList() != null) {

        ParameterCheckUtil.checkNotNullAndLength(breakout.getOppositeList());
        validateBreakoutOpposite();
      }
    }
  }

  private void validateBreakoutOpposite() throws MsfException {

    for (SpineNodeOppositeEntity tempOpposite : breakout.getOppositeList()) {


      ParameterCheckUtil.checkNotNullAndLength(tempOpposite.getOppositeNodeId());

      ParameterCheckUtil.checkNotNullAndLength(tempOpposite.getBreakoutIfList());

      validateBreakoutIfId(tempOpposite.getBreakoutIfList());
    }

  }

  private void validateBreakoutLocal() throws MsfException {

    if (breakout.getLocal() != null) {

      ParameterCheckUtil.checkNotNullAndLength(breakout.getLocal().getBreakoutIfList());

      validateBreakoutIfId(breakout.getLocal().getBreakoutIfList());
    }
  }

  private void validateBreakoutIfId(List<SpineNodeBreakoutIfCreateEntity> breakoutIfList) throws MsfException {

    for (SpineNodeBreakoutIfCreateEntity tempBIf : breakoutIfList) {

      ParameterCheckUtil.checkNotNullAndLength(tempBIf.getBreakoutIfIdList());

      ParameterCheckUtil.checkNotNull(tempBIf.getBaseIf());

      ParameterCheckUtil.checkNotNull(tempBIf.getDivisionNumber());

      ParameterCheckUtil.checkNotNullAndLength(tempBIf.getBreakoutIfSpeed());

      validateBaseIf(tempBIf.getBaseIf());
    }
  }

  private void validateBaseIf(SpineNodeBaseIfEntity baseIf) throws MsfException {


    ParameterCheckUtil.checkIdSpecifiedByUri(baseIf.getPhysicalIfId());
  }

  private void validateInternalLink() throws MsfException {


    if (internalLinks != null) {

      if (internalLinks.getPhysicalLinkList() != null) {

        ParameterCheckUtil.checkNotNullAndLength(internalLinks.getPhysicalLinkList());
        validatePhysicalLinkList();
      }

      if (internalLinks.getLagLinkList() != null) {

        ParameterCheckUtil.checkNotNullAndLength(internalLinks.getLagLinkList());
        validateLagLinkList();
      }
    }
  }

  private void validatePhysicalLinkList() throws MsfException {

    for (SpineNodePhysicalLinkEntity tempPLink : internalLinks.getPhysicalLinkList()) {

      ParameterCheckUtil.checkNotNullAndLength(tempPLink.getOppositeNodeId());



      ParameterCheckUtil.checkNotNull(tempPLink.getInternalLinkIf());

      validateInternalLinkIf(tempPLink.getInternalLinkIf());
    }
  }

  private void validateLagLinkList() throws MsfException {
    for (SpineNodeLagLinkEntity tempLLink : internalLinks.getLagLinkList()) {

      ParameterCheckUtil.checkNotNullAndLength(tempLLink.getOppositeNodeId());



      ParameterCheckUtil.checkNotNullAndLength(tempLLink.getMemberIfList());

      validateMemberIfList(tempLLink.getMemberIfList());
    }
  }

  private void validateInternalLinkIf(SpineNodeInternalLinkIfEntity internalLinkIf) throws MsfException {


    ParameterCheckUtil.checkNotNull(internalLinkIf.getLocal());

    ParameterCheckUtil.checkNotNull(internalLinkIf.getOpposite());

    validateLocal(internalLinkIf.getLocal());
    validateOpposite(internalLinkIf.getOpposite());
  }

  private void validateMemberIfList(List<SpineNodeMemberIfEntity> memberIfList) throws MsfException {

    for (SpineNodeMemberIfEntity tempMIf : memberIfList) {

      ParameterCheckUtil.checkNotNull(tempMIf.getLocal());

      ParameterCheckUtil.checkNotNull(tempMIf.getOpposite());

      validateLocal(tempMIf.getLocal());
      validateOpposite(tempMIf.getOpposite());
    }
  }

  private void validateLocal(SpineNodeInternalLocalEntity spineNodeInternalLocalEntity) throws MsfException {

    if (spineNodeInternalLocalEntity.getPhysicalIf() == null) {

      ParameterCheckUtil.checkNotNull(spineNodeInternalLocalEntity.getBreakoutIf());
      validateBreakoutIf(spineNodeInternalLocalEntity.getBreakoutIf());
    }
    if (spineNodeInternalLocalEntity.getBreakoutIf() == null) {

      ParameterCheckUtil.checkNotNull(spineNodeInternalLocalEntity.getPhysicalIf());
      validatePhysicalIf(spineNodeInternalLocalEntity.getPhysicalIf());
    }
  }

  private void validateOpposite(SpineNodeInternalOppositeEntity spineNodeInternalOppositeEntity) throws MsfException {

    if (spineNodeInternalOppositeEntity.getPhysicalIf() == null) {

      ParameterCheckUtil.checkNotNull(spineNodeInternalOppositeEntity.getBreakoutIf());
      validateBreakoutIf(spineNodeInternalOppositeEntity.getBreakoutIf());
    }
    if (spineNodeInternalOppositeEntity.getBreakoutIf() == null) {

      ParameterCheckUtil.checkNotNull(spineNodeInternalOppositeEntity.getPhysicalIf());
      validatePhysicalIf(spineNodeInternalOppositeEntity.getPhysicalIf());
    }
  }

  private void validatePhysicalIf(SpineNodePhysicalIfCreateEntity physicalIf) throws MsfException {


    ParameterCheckUtil.checkNotNullAndLength(physicalIf.getPhysicalIfId());

    ParameterCheckUtil.checkNotNullAndLength(physicalIf.getPhysicalIfSpeed());
  }

  private void validateBreakoutIf(SpineNodeInternalBreakoutIfEntity spineNodeInternalBreakoutIfEntity)
      throws MsfException {


    ParameterCheckUtil.checkNotNullAndLength(spineNodeInternalBreakoutIfEntity.getBreakoutIfId());
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
