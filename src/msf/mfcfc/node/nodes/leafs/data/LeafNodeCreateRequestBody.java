
package msf.mfcfc.node.nodes.leafs.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.LeafType;
import msf.mfcfc.common.constant.PlaneBelongsTo;
import msf.mfcfc.common.constant.VpnType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeBaseIfEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeBreakoutEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeBreakoutIfCreateEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeInternalBreakoutIfEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeInternalLinkEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeInternalLinkIfEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeInternalLocalEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeInternalOppositeEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeLagLinkEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeMemberIfEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodeOppositeEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodePhysicalIfCreateEntity;
import msf.mfcfc.node.nodes.leafs.data.entity.LeafNodePhysicalLinkEntity;
import msf.mfcfc.rest.common.RestRequestValidator;

public class LeafNodeCreateRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(LeafNodeCreateRequestBody.class);

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

  @SerializedName("password")
  private String password;

  @SerializedName("provisioning")
  private Boolean provisioning;

  @SerializedName("vpn_type")
  private String vpnType;

  @SerializedName("plane")
  private Integer plane;

  @SerializedName("snmp_community")
  private String snmpCommunity;

  @SerializedName("ntp_server_address")
  private String ntpServerAddress;

  @SerializedName("breakout")
  private LeafNodeBreakoutEntity breakout;

  @SerializedName("internal_links")
  private LeafNodeInternalLinkEntity internalLinks;

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

  public String getVpnType() {
    return vpnType;
  }

  public void setVpnType(String vpnType) {
    this.vpnType = vpnType;
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

  public LeafNodeBreakoutEntity getBreakout() {
    return breakout;
  }

  public void setBreakout(LeafNodeBreakoutEntity breakout) {
    this.breakout = breakout;
  }

  public LeafNodeInternalLinkEntity getInternalLinks() {
    return internalLinks;
  }

  public void setInternalLinks(LeafNodeInternalLinkEntity internalLinks) {
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

  public LeafType getLeafTypeEnum() {
    return LeafType.getEnumFromMessage(leafType);
  }

  public void setLeafTypeEnum(LeafType leafType) {
    this.leafType = leafType.getMessage();
  }

  public VpnType getVpnTypeEnum() {
    return VpnType.getEnumFromMessage(vpnType);
  }

  public void setVpnTypeEnum(VpnType vpnType) {
    this.vpnType = vpnType.getMessage();
  }

  public PlaneBelongsTo getPlaneEnum() {
    return PlaneBelongsTo.getEnumFromMessage(plane);
  }

  public void setPlaneEnum(PlaneBelongsTo plane) {
    this.plane = plane.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNumericId(nodeId, ErrorCode.PARAMETER_VALUE_ERROR);

      ParameterCheckUtil.checkNumericId(equipmentTypeId, ErrorCode.RELATED_RESOURCE_NOT_FOUND);

      ParameterCheckUtil.checkNotNull(getLeafTypeEnum());

      ParameterCheckUtil.checkNotNullAndLength(hostName);

      macAddress = ParameterCheckUtil.checkMacAddress(macAddress);

      ParameterCheckUtil.checkNotNullAndLength(username);

      ParameterCheckUtil.checkNotNullAndLength(password);

      ParameterCheckUtil.checkNotNull(provisioning);

      ParameterCheckUtil.checkNotNull(getVpnTypeEnum());

      ParameterCheckUtil.checkNotNull(getPlaneEnum());

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

    for (LeafNodeOppositeEntity tempOpposite : breakout.getOppositeList()) {

      ParameterCheckUtil.checkNumericId(tempOpposite.getOppositeNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

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

  private void validateBreakoutIfId(List<LeafNodeBreakoutIfCreateEntity> breakoutIfList) throws MsfException {

    for (LeafNodeBreakoutIfCreateEntity tempBIf : breakoutIfList) {

      ParameterCheckUtil.checkNotNullAndLength(tempBIf.getBreakoutIfIdList());

      ParameterCheckUtil.checkNotNull(tempBIf.getBaseIf());

      ParameterCheckUtil.checkNotNull(tempBIf.getDivisionNumber());

      ParameterCheckUtil.checkNotNullAndLength(tempBIf.getBreakoutIfSpeed());

      validateBaseIf(tempBIf.getBaseIf());
    }
  }

  private void validateBaseIf(LeafNodeBaseIfEntity baseIf) throws MsfException {

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

    for (LeafNodePhysicalLinkEntity tempPLink : internalLinks.getPhysicalLinkList()) {

      ParameterCheckUtil.checkNumericId(tempPLink.getOppositeNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

      ParameterCheckUtil.checkNotNull(tempPLink.getInternalLinkIf());

      validateInternalLinkIf(tempPLink.getInternalLinkIf());
    }
  }

  private void validateLagLinkList() throws MsfException {
    for (LeafNodeLagLinkEntity tempLLink : internalLinks.getLagLinkList()) {

      ParameterCheckUtil.checkNumericId(tempLLink.getOppositeNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

      ParameterCheckUtil.checkNotNullAndLength(tempLLink.getMemberIfList());

      validateMemberIfList(tempLLink.getMemberIfList());
    }
  }

  private void validateInternalLinkIf(LeafNodeInternalLinkIfEntity internalLinkIf) throws MsfException {

    ParameterCheckUtil.checkNotNull(internalLinkIf.getLocal());

    ParameterCheckUtil.checkNotNull(internalLinkIf.getOpposite());

    validateLocal(internalLinkIf.getLocal());
    validateOpposite(internalLinkIf.getOpposite());
  }

  private void validateMemberIfList(List<LeafNodeMemberIfEntity> memberIfList) throws MsfException {

    for (LeafNodeMemberIfEntity tempMIf : memberIfList) {

      ParameterCheckUtil.checkNotNull(tempMIf.getLocal());

      ParameterCheckUtil.checkNotNull(tempMIf.getOpposite());

      validateLocal(tempMIf.getLocal());
      validateOpposite(tempMIf.getOpposite());
    }
  }

  private void validateLocal(LeafNodeInternalLocalEntity local) throws MsfException {

    if (local.getPhysicalIf() == null) {

      ParameterCheckUtil.checkNotNull(local.getBreakoutIf());
      validateBreakoutIf(local.getBreakoutIf());
    }
    if (local.getBreakoutIf() == null) {

      ParameterCheckUtil.checkNotNull(local.getPhysicalIf());
      validatePhysicalIf(local.getPhysicalIf());
    }
  }

  private void validateOpposite(LeafNodeInternalOppositeEntity opposite) throws MsfException {

    if (opposite.getPhysicalIf() == null) {

      ParameterCheckUtil.checkNotNull(opposite.getBreakoutIf());
      validateBreakoutIf(opposite.getBreakoutIf());
    }
    if (opposite.getBreakoutIf() == null) {

      ParameterCheckUtil.checkNotNull(opposite.getPhysicalIf());
      validatePhysicalIf(opposite.getPhysicalIf());
    }
  }

  private void validatePhysicalIf(LeafNodePhysicalIfCreateEntity physicalIf) throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(physicalIf.getPhysicalIfId());

    ParameterCheckUtil.checkNotNullAndLength(physicalIf.getPhysicalIfSpeed());
  }

  private void validateBreakoutIf(LeafNodeInternalBreakoutIfEntity breakoutIf) throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(breakoutIf.getBreakoutIfId());
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
