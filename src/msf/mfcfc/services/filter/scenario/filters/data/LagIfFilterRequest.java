
package msf.mfcfc.services.filter.scenario.filters.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.core.scenario.RestRequestBase;

public class LagIfFilterRequest extends RestRequestBase {

  private String clusterId;

  private String fabricType;

  private String nodeId;

  private String lagIfId;

  private String format;

  public LagIfFilterRequest(String requestBody, String notificationAddress, String notificationPort, String clusterId,
      String fabricType, String nodeId, String lagIfId, String format) {
    super(requestBody, notificationAddress, notificationPort);
    this.clusterId = clusterId;
    this.fabricType = fabricType;
    this.nodeId = nodeId;
    this.lagIfId = lagIfId;
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

  public NodeType getFabricTypeEnum() {
    if (NodeType.getEnumFromSingularMessage(fabricType) != null) {
      return NodeType.getEnumFromSingularMessage(fabricType);
    } else {
      return NodeType.getEnumFromPluralMessage(fabricType);
    }
  }

  public void setFabricTypeEnum(NodeType fabricType) {
    this.fabricType = fabricType.getPluralMessage();
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getLagIfId() {
    return lagIfId;
  }

  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  public String getFormat() {
    return format;
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
