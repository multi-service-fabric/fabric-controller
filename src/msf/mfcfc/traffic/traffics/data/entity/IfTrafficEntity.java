package msf.mfcfc.traffic.traffics.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class IfTrafficEntity {

  
  @SerializedName("fabric_type")
  private String fabricType;

  
  @SerializedName("node_id")
  private String nodeId;

  
  @SerializedName("traffic_value")
  private IfTrafficValueNodeEntity trafficValue;

  
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

  
  public IfTrafficValueNodeEntity getTrafficValue() {
    return trafficValue;
  }

  
  public void setTrafficValue(IfTrafficValueNodeEntity trafficValue) {
    this.trafficValue = trafficValue;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
