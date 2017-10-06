package msf.fc.node.interfaces.internalifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.RestFormatOption;
import msf.fc.core.scenario.RestRequestBase;

public class InternalIfRequest extends RestRequestBase {

  private String clusterId;

  private String fabricType;

  private String nodeId;

  private String internalIfId;

  private String format;

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
    this.fabricType = fabricType.getSingularMessage();
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getInternalIfId() {
    return internalIfId;
  }

  public void setInternalIfId(String internalIfId) {
    this.internalIfId = internalIfId;
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
