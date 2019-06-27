
package msf.mfcfc.node.interfaces.breakoutifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.core.scenario.RestRequestBase;

public class BreakoutIfRequest extends RestRequestBase {

  private String clusterId;

  private String fabricType;

  private String nodeId;

  private String breakoutIfId;

  private String format;

  public BreakoutIfRequest(String requestBody, String notificationAddress, String notificationPort, String clusterId,
      String fabricType, String nodeId, String breakoutIfId, String format) {
    super(requestBody, notificationAddress, notificationPort);
    this.clusterId = clusterId;
    this.fabricType = fabricType;
    this.nodeId = nodeId;
    this.breakoutIfId = breakoutIfId;
    this.format = format;
  }

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

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

  public String getBreakoutIfId() {
    return breakoutIfId;
  }

  public void setBreakoutIfId(String breakoutIfId) {
    this.breakoutIfId = breakoutIfId;
  }

  public String getFormat() {
    return format;
  }

  public NodeType getFabricTypeEnum() {
    return NodeType.getEnumFromPluralMessage(fabricType);
  }

  public void setFabricTypeEnum(NodeType fabricType) {
    this.fabricType = fabricType.getPluralMessage();
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public RestFormatOption getFormatEnum() {
    return RestFormatOption.getEnumFromMessage(format);
  }

  public void setFormatEnum(RestFormatOption format) {
    this.format = format.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
