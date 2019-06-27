
package msf.mfcfc.services.priorityroutes.scenario.internalifs.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.core.scenario.RestRequestBase;

public class InternalLinkIfPriorityRequest extends RestRequestBase {

  private String clusterId;

  private String fabricType;

  private String nodeId;

  private String internalLinkIfId;

  public InternalLinkIfPriorityRequest(String requestBody, String notificationAddress, String notificationPort,
      String clusterId, String fabricType, String nodeId, String internalLinkIfId) {
    super(requestBody, notificationAddress, notificationPort);
    this.clusterId = clusterId;
    this.fabricType = fabricType;
    this.nodeId = nodeId;
    this.internalLinkIfId = internalLinkIfId;
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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
