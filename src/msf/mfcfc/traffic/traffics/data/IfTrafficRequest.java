package msf.mfcfc.traffic.traffics.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.core.scenario.RestRequestBase;


public class IfTrafficRequest extends RestRequestBase {

  
  public IfTrafficRequest(String requestBody, String notificationAddress, String notificationPort, String clusterId,
      String fabricType, String nodeId, String ifType, String ifId) {
    super(requestBody, notificationAddress, notificationPort);
    this.clusterId = clusterId;
    this.fabricType = fabricType;
    this.nodeId = nodeId;
    this.ifType = ifType;
    this.ifId = ifId;
  }

  
  private String clusterId;

  
  private String fabricType;

  
  private String nodeId;

  
  private String ifType;

  
  private String ifId;

  
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

  
  public InterfaceType getIfTypeEnum() {
    return InterfaceType.getEnumFromMessage(ifType);
  }

  
  public void setIfTypeEnum(InterfaceType ifType) {
    this.ifType = ifType.getMessage();
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
