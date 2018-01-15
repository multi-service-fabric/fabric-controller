package msf.mfcfc.node.interfaces.lagifs.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.NodeType;


public class LagIfOppositeEntity {

  
  @SerializedName("fabric_type")
  private String fabricType;

  
  @SerializedName("node_id")
  private String nodeId;

  
  @SerializedName("lag_if_id")
  private String laglIfId;

  
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

  
  public String getLaglIfId() {
    return laglIfId;
  }

  
  public void setLaglIfId(String laglIfId) {
    this.laglIfId = laglIfId;
  }

  
  public NodeType getFabricTypeEnum() {
    return NodeType.getEnumFromSingularMessage(fabricType);
  }

  
  public void setFabricTypeEnum(NodeType fabricType) {
    this.fabricType = fabricType.getSingularMessage();
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
