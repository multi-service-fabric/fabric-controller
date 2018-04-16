
package msf.mfcfc.node.nodes.data;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.NodeBootStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.node.nodes.data.entity.NodeAsNodeNotifyEntity;
import msf.mfcfc.node.nodes.data.entity.NodeBgpNodeNotifyEntity;
import msf.mfcfc.node.nodes.data.entity.NodeBreakoutBaseIfEntity;
import msf.mfcfc.node.nodes.data.entity.NodeCreateNodeIfEntity;
import msf.mfcfc.node.nodes.data.entity.NodeInfoNodeNotifyEntity;
import msf.mfcfc.node.nodes.data.entity.NodeInternalLinkIfEntity;
import msf.mfcfc.node.nodes.data.entity.NodeInternalLinkIfInnerEntity;
import msf.mfcfc.node.nodes.data.entity.NodeL2VpnEntity;
import msf.mfcfc.node.nodes.data.entity.NodeL3VpnEntity;
import msf.mfcfc.node.nodes.data.entity.NodeLagMemberEntity;
import msf.mfcfc.node.nodes.data.entity.NodeLoopbackIfEntity;
import msf.mfcfc.node.nodes.data.entity.NodeManagementIfEntity;
import msf.mfcfc.node.nodes.data.entity.NodeNeighborEntity;
import msf.mfcfc.node.nodes.data.entity.NodeOppositeNodeEntity;
import msf.mfcfc.node.nodes.data.entity.NodeRangeEntity;
import msf.mfcfc.node.nodes.data.entity.NodeUnusedPhysicalEntity;
import msf.mfcfc.node.nodes.data.entity.NodeVirtualLinkEntity;
import msf.mfcfc.node.nodes.data.entity.NodeVpnEntity;
import msf.mfcfc.rest.common.RestRequestValidator;

public class NodeNotifyRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(NodeNotifyRequestBody.class);

  @SerializedName("status")
  private String status;

  @SerializedName("node_info")
  private NodeInfoNodeNotifyEntity nodeInfo;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public NodeInfoNodeNotifyEntity getNodeInfo() {
    return nodeInfo;
  }

  public void setNodeInfo(NodeInfoNodeNotifyEntity nodeInfo) {
    this.nodeInfo = nodeInfo;
  }

  public NodeBootStatus getStatusEnum() {
    return NodeBootStatus.getEnumFromMessage(status);
  }

  public void setStatusEnum(NodeBootStatus nodeBootStatus) {
    this.status = nodeBootStatus.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNull(getStatusEnum());
      switch (getStatusEnum()) {
        case SUCCESS:

          if (nodeInfo != null) {
            validateNodeInfo();
          }
          break;
        case CANCEL:
        case FAILED:
          break;
        default:

          throw new IllegalArgumentException(MessageFormat.format("status={0}", getStatusEnum()));
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void validateNodeInfo() throws MsfException {

    ParameterCheckUtil.checkNotNull(nodeInfo.getEquipment());

    ParameterCheckUtil.checkNotNull(nodeInfo.getCreateNode());

    validateEquipment();
    validateCreateNode();

    if (nodeInfo.getUpdateNode() != null) {
      validateUpdateNode();
    }
  }

  private void validateEquipment() throws MsfException {

    ParameterCheckUtil.checkNumericId(nodeInfo.getEquipment().getEquipmentTypeId(),
        ErrorCode.RELATED_RESOURCE_NOT_FOUND);
  }

  private void validateCreateNode() throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(nodeInfo.getCreateNode().getNodeId());

    ParameterCheckUtil.checkNotNullAndLength(nodeInfo.getCreateNode().getHostName());

    ParameterCheckUtil.checkNotNull(nodeInfo.getCreateNode().getNodeTypeEnum());

    ParameterCheckUtil.checkNotNullAndLength(nodeInfo.getCreateNode().getUsername());

    ParameterCheckUtil.checkNotNullAndLength(nodeInfo.getCreateNode().getPassword());

    ParameterCheckUtil.checkNotNullAndLength(nodeInfo.getCreateNode().getMacAddr());

    ParameterCheckUtil.checkNotNull(nodeInfo.getCreateNode().getProvisioning());

    ParameterCheckUtil.checkNotNullAndLength(nodeInfo.getCreateNode().getNtpServerAddress());

    ParameterCheckUtil.checkNotNull(nodeInfo.getCreateNode().getManagementInterface());

    ParameterCheckUtil.checkNotNull(nodeInfo.getCreateNode().getLoopbackInterface());

    ParameterCheckUtil.checkNotNullAndLength(nodeInfo.getCreateNode().getSnmpCommunity());

    ParameterCheckUtil.checkNotNull(nodeInfo.getCreateNode().getCreateNodeIf());

    ParameterCheckUtil.checkNotNullAndLength(nodeInfo.getCreateNode().getClusterArea());

    validateManagementInterface(nodeInfo.getCreateNode().getManagementInterface());
    validateLoopbackInterface(nodeInfo.getCreateNode().getLoopbackInterface());
    validateCreateNodeIf(nodeInfo.getCreateNode().getCreateNodeIf());

    if (nodeInfo.getCreateNode().getOppositeNodeList() != null) {
      validateOppositeNodeList(nodeInfo.getCreateNode().getOppositeNodeList());
    }

    switch (nodeInfo.getCreateNode().getNodeTypeEnum()) {
      case LEAF:

        ParameterCheckUtil.checkNotNullAndLength(nodeInfo.getCreateNode().getPlane());

        ParameterCheckUtil.checkNotNull(nodeInfo.getCreateNode().getVpn());

        validateLeafVpn(nodeInfo.getCreateNode().getVpn());
        break;
      case RR:

        ParameterCheckUtil.checkNotNull(nodeInfo.getCreateNode().getVpn());

        validateRrVpn(nodeInfo.getCreateNode().getVpn());
        break;
      case B_LEAF:

        ParameterCheckUtil.checkNotNull(nodeInfo.getCreateNode().getVpn());
        if (nodeInfo.getUpdateNode() != null) {

          ParameterCheckUtil.checkNotNull(nodeInfo.getCreateNode().getVirtualLink());
          validateVirtualLink(nodeInfo.getCreateNode().getVirtualLink());
        }

        ParameterCheckUtil.checkNotNull(nodeInfo.getCreateNode().getRange());

        validateBleafVpn(nodeInfo.getCreateNode().getVpn());
        validateRange(nodeInfo.getCreateNode().getRange());
        break;
      case SPINE:
        break;
      default:

        throw new IllegalArgumentException(
            MessageFormat.format("nodeType={0}", nodeInfo.getCreateNode().getNodeTypeEnum()));
    }
  }

  private void validateUpdateNode() throws MsfException {

    ParameterCheckUtil.checkNumericId(nodeInfo.getUpdateNode().getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

    ParameterCheckUtil.checkNotNullAndLength(nodeInfo.getUpdateNode().getNodeType());

    ParameterCheckUtil.checkNotNullAndLength(nodeInfo.getUpdateNode().getClusterArea());

    ParameterCheckUtil.checkNotNull(nodeInfo.getUpdateNode().getVirtualLink());

    validateVirtualLink(nodeInfo.getUpdateNode().getVirtualLink());
  }

  private void validateRange(NodeRangeEntity range) throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(range.getAddress());

    ParameterCheckUtil.checkNotNull(range.getPrefix());

  }

  private void validateVirtualLink(NodeVirtualLinkEntity virtualLink) throws MsfException {

    ParameterCheckUtil.checkNumericId(virtualLink.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
  }

  private void validateBleafVpn(NodeVpnEntity vpn) throws MsfException {

    ParameterCheckUtil.checkNotNull(vpn.getVpnTypeEnum());

    switch (vpn.getVpnTypeEnum()) {
      case L2VPN:
        break;
      case L3VPN:
        ParameterCheckUtil.checkNotNull(vpn.getL3vpn());
        validateL3Vpn(vpn.getL3vpn());
        break;
      default:

        throw new IllegalArgumentException(MessageFormat.format("vpnType={0}", vpn.getVpnTypeEnum()));
    }
  }

  private void validateRrVpn(NodeVpnEntity vpn) throws MsfException {

    ParameterCheckUtil.checkNotNull(vpn.getL3vpn());

    validateL3Vpn(vpn.getL3vpn());
  }

  private void validateLeafVpn(NodeVpnEntity vpn) throws MsfException {

    ParameterCheckUtil.checkNotNull(vpn.getVpnTypeEnum());

    switch (vpn.getVpnTypeEnum()) {
      case L2VPN:
        ParameterCheckUtil.checkNotNull(vpn.getL2vpn());
        validateLeafL2Vpn(vpn.getL2vpn());
        break;
      case L3VPN:
        ParameterCheckUtil.checkNotNull(vpn.getL3vpn());
        validateLeafL3Vpn(vpn.getL3vpn());
        break;
      default:

        throw new IllegalArgumentException(MessageFormat.format("vpnType={0}", vpn.getVpnTypeEnum()));
    }
  }

  private void validateLeafL2Vpn(NodeL2VpnEntity l2vpn) throws MsfException {

    ParameterCheckUtil.checkNotNull(l2vpn.getAs());

    validateAs(l2vpn.getAs());

    if (l2vpn.getBgp() != null) {
      validateLeafBgp(l2vpn.getBgp(), l2vpn.getAs().getAsNumber());
    }

  }

  private void validateLeafL3Vpn(NodeL3VpnEntity l3vpn) throws MsfException {

    ParameterCheckUtil.checkNotNull(l3vpn.getAs());

    validateAs(l3vpn.getAs());

    if (l3vpn.getBgp() != null) {
      validateLeafBgp(l3vpn.getBgp(), l3vpn.getAs().getAsNumber());
    }

  }

  private void validateL3Vpn(NodeL3VpnEntity l3vpn) throws MsfException {

    ParameterCheckUtil.checkNotNull(l3vpn.getAs());

    validateAs(l3vpn.getAs());

    if (l3vpn.getBgp() != null) {
      validateBgp(l3vpn.getBgp(), l3vpn.getAs().getAsNumber());
    }

  }

  private void validateAs(NodeAsNodeNotifyEntity as) throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(as.getAsNumber());
  }

  private void validateBgp(NodeBgpNodeNotifyEntity bgp, String asNumber) throws MsfException {

    ParameterCheckUtil.checkNotNull(bgp.getNeighbor());

    ParameterCheckUtil.checkNotNullAndLength(bgp.getCommunityWildcard());
    if (!bgp.getCommunityWildcard().matches(asNumber + ":.*")) {
      String logMsg = MessageFormat.format("param is not match.param = {0}, value = {1}",
          "bgp.getCommunityWildcard().matches(asNumber + ':.*'", bgp.getCommunityWildcard().matches(asNumber + ":.*"));
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    }

    validateNeighbor(bgp.getNeighbor());
  }

  private void validateLeafBgp(NodeBgpNodeNotifyEntity bgp, String asNumber) throws MsfException {

    ParameterCheckUtil.checkNotNull(bgp.getNeighbor());

    ParameterCheckUtil.checkNotNullAndLength(bgp.getCommunity());
    if (!bgp.getCommunity().matches(asNumber + ":" + nodeInfo.getCreateNode().getPlane())) {
      String logMsg = MessageFormat.format("param is not match.param = {0}, value = {1}",
          "bgp.getCommunity().matches(asNumber + ':' + nodeInfo.getCreateNode().getPlane()",
          bgp.getCommunity().matches(asNumber + ":" + nodeInfo.getCreateNode().getPlane()));
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    }

    ParameterCheckUtil.checkNotNullAndLength(bgp.getCommunityWildcard());
    if (!bgp.getCommunityWildcard().matches(asNumber + ":.*")) {
      String logMsg = MessageFormat.format("param is not match.param = {0}, value = {1}",
          "bgp.getCommunityWildcard().matches(asNumber + ':.*'", bgp.getCommunityWildcard().matches(asNumber + ":.*"));
      logger.error(logMsg);
      throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, logMsg);
    }

    validateNeighbor(bgp.getNeighbor());
  }

  private void validateNeighbor(NodeNeighborEntity neighbor) throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(neighbor.getAddressList());
  }

  private void validateManagementInterface(NodeManagementIfEntity managementInterface) throws MsfException {

    managementInterface.setAddress(ParameterCheckUtil.checkIpv4Address(managementInterface.getAddress()));

    ParameterCheckUtil.checkNotNull(managementInterface.getPrefix());
    ParameterCheckUtil.checkNumberRange(managementInterface.getPrefix(), 0, 32);
  }

  private void validateLoopbackInterface(NodeLoopbackIfEntity loopbackInterface) throws MsfException {

    loopbackInterface.setAddress(ParameterCheckUtil.checkIpv4Address(loopbackInterface.getAddress()));

    ParameterCheckUtil.checkNotNull(loopbackInterface.getPrefix());
    ParameterCheckUtil.checkNumberRange(loopbackInterface.getPrefix(), 32, 32);
  }

  private void validateCreateNodeIf(NodeCreateNodeIfEntity createNodeIf) throws MsfException {

    if (createNodeIf.getBreakoutBaseIfList() != null) {
      validateBreakoutBaseIfList(createNodeIf.getBreakoutBaseIfList());
    }
    if (createNodeIf.getInternalLinkIfList() != null) {
      validateInternalLinkIfList(createNodeIf.getInternalLinkIfList());
    }
    if (createNodeIf.getUnusedPhysicalIfList() != null) {
      validateUnusedPhysicalIfList(createNodeIf.getUnusedPhysicalIfList());
    }
  }

  private void validateOppositeNodeList(List<NodeOppositeNodeEntity> oppositeNodeList) throws MsfException {

    for (NodeOppositeNodeEntity tempOppositeNode : oppositeNodeList) {

      ParameterCheckUtil.checkNumericId(tempOppositeNode.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

      if (tempOppositeNode.getBreakoutBaseIfList() != null) {
        validateBreakoutBaseIfList(tempOppositeNode.getBreakoutBaseIfList());
      }
      if (tempOppositeNode.getInternalLinkIf() != null) {
        validateInternalLinkIf(tempOppositeNode.getInternalLinkIf());
      }
    }

  }

  private void validateBreakoutBaseIfList(List<NodeBreakoutBaseIfEntity> breakoutBaseIfList) throws MsfException {

    for (NodeBreakoutBaseIfEntity tempBreakoutBaseIf : breakoutBaseIfList) {

      ParameterCheckUtil.checkNotNullAndLength(tempBreakoutBaseIf.getBasePhysicalIfId());

      ParameterCheckUtil.checkNotNullAndLength(tempBreakoutBaseIf.getSpeed());

      ParameterCheckUtil.checkNotNullAndLength(tempBreakoutBaseIf.getBreakoutIfIdList());
    }
  }

  private void validateInternalLinkIfList(List<NodeInternalLinkIfEntity> internalLinkIfList) throws MsfException {

    for (NodeInternalLinkIfEntity tempInternalLinkIf : internalLinkIfList) {
      validateInternalLinkIfs(tempInternalLinkIf);
    }
  }

  private void validateInternalLinkIfs(NodeInternalLinkIfEntity internalLinkIf) throws MsfException {

    ParameterCheckUtil.checkNotNull(internalLinkIf.getInternalLinkIf());
    validateInternalLinkIf(internalLinkIf.getInternalLinkIf());
  }

  private void validateUnusedPhysicalIfList(List<NodeUnusedPhysicalEntity> unusedPhysicalIfList) throws MsfException {

    for (NodeUnusedPhysicalEntity tempUnusedPhysicalIf : unusedPhysicalIfList) {

      ParameterCheckUtil.checkNotNullAndLength(tempUnusedPhysicalIf.getPhysicalIfId());
    }
  }

  private void validateInternalLinkIf(NodeInternalLinkIfInnerEntity internalLinkIf) throws MsfException {

    ParameterCheckUtil.checkNotNull(internalLinkIf.getIfTypeEnum());

    ParameterCheckUtil.checkNotNullAndLength(internalLinkIf.getIfId());

    switch (internalLinkIf.getIfTypeEnum()) {
      case LAG_IF:
        if (internalLinkIf.getLagMemberList() == null) {
          break;
        }
        for (NodeLagMemberEntity lagMember : internalLinkIf.getLagMemberList()) {
          if (!lagMember.getIfTypeEnum().equals(InterfaceType.BREAKOUT_IF)) {

            ParameterCheckUtil.checkNotNullAndLength(internalLinkIf.getLinkSpeed());
            break;
          }
        }
        break;
      case PHYSICAL_IF:

        ParameterCheckUtil.checkNotNullAndLength(internalLinkIf.getLinkSpeed());
        break;
      case VLAN_IF:
      case BREAKOUT_IF:
        break;
      default:

        throw new IllegalArgumentException(MessageFormat.format("ifType={0}", internalLinkIf.getIfTypeEnum()));
    }

    internalLinkIf.setLinkIpAddress(ParameterCheckUtil.checkIpv4Address(internalLinkIf.getLinkIpAddress()));

    ParameterCheckUtil.checkNotNull(internalLinkIf.getPrefix());
    ParameterCheckUtil.checkNumberRange(internalLinkIf.getPrefix(), 30, 30);

    if (internalLinkIf.getLagMemberList() != null) {
      validateLagMember(internalLinkIf.getLagMemberList());
    }
  }

  private void validateLagMember(List<NodeLagMemberEntity> lagMemberList) throws MsfException {

    for (NodeLagMemberEntity tempLagMenber : lagMemberList) {

      ParameterCheckUtil.checkNotNull(tempLagMenber.getIfTypeEnum());

      ParameterCheckUtil.checkNotNullAndLength(tempLagMenber.getIfId());
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
