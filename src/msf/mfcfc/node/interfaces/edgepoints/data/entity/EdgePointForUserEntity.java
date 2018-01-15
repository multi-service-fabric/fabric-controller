package msf.mfcfc.node.interfaces.edgepoints.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class EdgePointForUserEntity {

  
  @SerializedName("edge_point_id")
  private String edgePointId;

  
  @SerializedName("support_protocols")
  private EdgePointSupportProtocolForUserEntity supportProtocols;

  
  @SerializedName("qos")
  private EdgePointForUserQosEntity qos;

  
  public String getEdgePointId() {
    return edgePointId;
  }

  
  public void setEdgePointId(String edgePointId) {
    this.edgePointId = edgePointId;
  }

  
  public EdgePointSupportProtocolForUserEntity getSupportProtocols() {
    return supportProtocols;
  }

  
  public void setSupportProtocols(EdgePointSupportProtocolForUserEntity supportProtocols) {
    this.supportProtocols = supportProtocols;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
