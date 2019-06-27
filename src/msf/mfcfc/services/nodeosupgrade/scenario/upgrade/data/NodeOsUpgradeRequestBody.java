
package msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.services.nodeosupgrade.scenario.detour.data.NodeDetourUpdateRequestBody;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.entity.NodeOsUpgradeListEntity;

public class NodeOsUpgradeRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(NodeDetourUpdateRequestBody.class);

  @SerializedName("reservation_time")
  private String reservationTime;

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("nodes")
  private List<NodeOsUpgradeListEntity> nodeList;

  public String getReservationTime() {
    return reservationTime;
  }

  public void setReservationTime(String reservationTime) {
    this.reservationTime = reservationTime;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public List<NodeOsUpgradeListEntity> getNodeList() {
    return nodeList;
  }

  public void setNodeList(List<NodeOsUpgradeListEntity> nodeList) {
    this.nodeList = nodeList;
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();
      if (reservationTime != null) {

        ParameterCheckUtil.checkDatetime(reservationTime);
      }

      ParameterCheckUtil.checkNumericId(clusterId, ErrorCode.PARAMETER_VALUE_ERROR);

      ParameterCheckUtil.checkNotNullAndLength(nodeList);
      validateNodeOsUpgradeList();

    } finally {
      logger.methodEnd();
    }
  }

  private void validateNodeOsUpgradeList() throws MsfException {
    HashSet<String> leafNodeList = new HashSet<String>();
    HashSet<String> spineNodeList = new HashSet<String>();

    for (NodeOsUpgradeListEntity nouEntity : nodeList) {

      NodeType fabricTypeEnum = nouEntity.getFabricTypeEnum();
      ParameterCheckUtil.checkNotNull(fabricTypeEnum);

      String nodeId = nouEntity.getNodeId();
      ParameterCheckUtil.checkNumericId(nodeId, ErrorCode.PARAMETER_VALUE_ERROR);

      switch (fabricTypeEnum) {
        case LEAF:
          if (leafNodeList.contains(nodeId)) {
            throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, MessageFormat
                .format("target resource is already exist. fabricType = {0}, nodeId = {1}.", fabricTypeEnum, nodeId));
          }
          leafNodeList.add(nodeId);
          break;

        case SPINE:
          if (spineNodeList.contains(nodeId)) {
            throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST, MessageFormat
                .format("target resource is already exist. fabricType = {0}, nodeId = {1}.", fabricTypeEnum, nodeId));
          }
          spineNodeList.add(nodeId);
          break;

        default:

          throw new MsfException(ErrorCode.UNDEFINED_ERROR, "Illegal parameter. nodeType = " + fabricTypeEnum);
      }

      ParameterCheckUtil.checkNumericId(nouEntity.getEquipmentTypeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

      ParameterCheckUtil.checkNotNull(nouEntity.getOsUpgrade());

      ParameterCheckUtil.checkNotNullAndLength(nouEntity.getOsUpgrade().getUpgradeScriptPath());

      ParameterCheckUtil.checkNotNull(nouEntity.getOsUpgrade().getZtpFlag());

      ParameterCheckUtil.checkNotNullAndLength(nouEntity.getOsUpgrade().getUpgradeCompleteMsg());

      ParameterCheckUtil.checkNotNull(nouEntity.getOperatorCheck());
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
