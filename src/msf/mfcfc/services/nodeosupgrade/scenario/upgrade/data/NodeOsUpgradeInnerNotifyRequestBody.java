
package msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.rest.common.RestRequestValidator;
import msf.mfcfc.services.nodeosupgrade.common.constant.NodeOsUpgradeOperationType;
import msf.mfcfc.services.nodeosupgrade.common.constant.OsUpgradeResultType;

public class NodeOsUpgradeInnerNotifyRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(NodeOsUpgradeInnerNotifyRequestBody.class);

  @SerializedName("fabric_type")
  private String fabricType;

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("operation_type")
  private String operationType;

  @SerializedName("os_upgrade_result")
  private String osUpgradeResult;

  public String getFabricType() {
    return fabricType;
  }

  public void setFabricType(String fabricType) {
    this.fabricType = fabricType;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getOperationType() {
    return operationType;
  }

  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }

  public String getOsUpgradeResult() {
    return osUpgradeResult;
  }

  public void setOsUpgradeResult(String osUpgradeResult) {
    this.osUpgradeResult = osUpgradeResult;
  }

  public NodeType getFabricTypeEnum() {
    return NodeType.getEnumFromSingularMessage(fabricType);
  }

  public void setFabricTypeEnum(NodeType fabricType) {
    if (fabricType != null) {
      this.fabricType = fabricType.getSingularMessage();
    } else {
      this.fabricType = null;
    }
  }

  public NodeOsUpgradeOperationType getOperationTypeEnum() {
    return NodeOsUpgradeOperationType.getEnumFromMessage(operationType);
  }

  public void setOperationTypeEnum(NodeOsUpgradeOperationType operationType) {
    this.operationType = operationType.getMessage();
  }

  public OsUpgradeResultType getOsUpgradeResultEnum() {
    return OsUpgradeResultType.getEnumFromMessage(osUpgradeResult);
  }

  public void setOsUpgradeResultEnum(OsUpgradeResultType osUpgradeResult) {
    this.osUpgradeResult = osUpgradeResult.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNullAndLength(fabricType);

      ParameterCheckUtil.checkNotNullAndLength(nodeId);

      ParameterCheckUtil.checkNotNull(getOperationTypeEnum());

      ParameterCheckUtil.checkNotNull(getOsUpgradeResultEnum());

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
