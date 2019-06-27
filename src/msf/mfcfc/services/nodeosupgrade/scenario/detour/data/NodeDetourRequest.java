
package msf.mfcfc.services.nodeosupgrade.scenario.detour.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.core.scenario.RestRequestBase;

public class NodeDetourRequest extends RestRequestBase {

  private String clusterId;

  private String fabricType;

  private String nodeId;

  public NodeDetourRequest(String requestBody, String notificationAddress, String notificationPort, String clusterId,
      String fabricType, String nodeId) {
    super(requestBody, notificationAddress, notificationPort);
    this.clusterId = clusterId;
    this.fabricType = fabricType;
    this.nodeId = nodeId;
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

  public NodeType getFabricTypeEnum() {
    return NodeType.getEnumFromPluralMessage(fabricType);
  }

  public void setFabricTypeEnum(NodeType fabricType) {
    this.fabricType = fabricType.getPluralMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
