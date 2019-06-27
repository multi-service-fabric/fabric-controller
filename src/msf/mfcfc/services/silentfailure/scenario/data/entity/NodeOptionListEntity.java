
package msf.mfcfc.services.silentfailure.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.services.silentfailure.common.constant.BlockadeResultType;
import msf.mfcfc.services.silentfailure.common.constant.MonitoringResultType;

public class NodeOptionListEntity {

  @SerializedName("fabric_type")
  private String fabricType;

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("internal_link_if_id")
  private String internalLinkIfId;

  @SerializedName("if_type")
  private String ifType;

  @SerializedName("if_id")
  private String ifId;

  @SerializedName("monitoring_result")
  private String monitoringResult;

  @SerializedName("blockade_result")
  private String blockadeResult;

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

  public String getInternalLinkIfId() {
    return internalLinkIfId;
  }

  public void setInternalLinkIfId(String internalLinkIfId) {
    this.internalLinkIfId = internalLinkIfId;
  }

  public String getIfType() {
    return ifType;
  }

  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  public String getIfId() {
    return ifId;
  }

  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  public String getMonitoringResult() {
    return monitoringResult;
  }

  public void setMonitoringResult(String monitoringResult) {
    this.monitoringResult = monitoringResult;
  }

  public String getBlockadeResult() {
    return blockadeResult;
  }

  public void setBlockadeResult(String blockadeResult) {
    this.blockadeResult = blockadeResult;
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

  public InterfaceType getIfTypeEnum() {
    return InterfaceType.getEnumFromMessage(ifType);
  }

  public void setIfTypeEnum(InterfaceType ifType) {
    this.ifType = ifType.getMessage();
  }

  public MonitoringResultType getMonitoringResultEnum() {
    return MonitoringResultType.getEnumFromMessage(monitoringResult);
  }

  public void setMonitoringResultEnum(MonitoringResultType monitoringResult) {
    if (monitoringResult != null) {
      this.monitoringResult = monitoringResult.getMessage();
    } else {
      this.monitoringResult = null;
    }
  }

  public BlockadeResultType getBlockadeResultEnum() {
    return BlockadeResultType.getEnumFromMessage(blockadeResult);
  }

  public void setBlockadeResultEnum(BlockadeResultType blockadeResult) {
    if (blockadeResult != null) {
      this.blockadeResult = blockadeResult.getMessage();
    } else {
      this.blockadeResult = null;
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
