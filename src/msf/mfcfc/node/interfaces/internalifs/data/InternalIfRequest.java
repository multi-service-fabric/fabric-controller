
package msf.mfcfc.node.interfaces.internalifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.core.scenario.RestRequestBase;

public class InternalIfRequest extends RestRequestBase {

  private String clusterId;

  private String fabricType;

  private String nodeId;

  private String internalLinkIfId;

  private String format;

  public InternalIfRequest(String requestBody, String notificationAddress, String notificationPort, String clusterId,
      String fabricType, String nodeId, String internalLinkIfId, String format) {
    super(requestBody, notificationAddress, notificationPort);
    this.clusterId = clusterId;
    this.fabricType = fabricType;
    this.nodeId = nodeId;
    this.internalLinkIfId = internalLinkIfId;
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

  public String getInternalLinkIfId() {
    return internalLinkIfId;
  }

  public void setInternalLinkIfId(String internalLinkIfId) {
    this.internalLinkIfId = internalLinkIfId;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public NodeType getFabricTypeEnum() {
    return NodeType.getEnumFromPluralMessage(fabricType);
  }

  public void setFabricTypeEnum(NodeType fabricType) {
    this.fabricType = fabricType.getPluralMessage();
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
