
package msf.mfcfc.node.interfaces.edgepoints.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class EdgePointForOwnerEntity {

  @SerializedName("edge_point_id")
  private String edgePointId;

  @SerializedName("base_if")
  private EdgePointBaseIfEntity baseIf;

  @SerializedName("support_protocols")
  private EdgePointSupportProtocolForOwnerEntity supportProtocols;

  @SerializedName("qos")
  private EdgePointForOwnerQosEntity qos;

  @SerializedName("traffic_threshold")
  private Double trafficThreshold;

  public String getEdgePointId() {
    return edgePointId;
  }

  public void setEdgePointId(String edgePointId) {
    this.edgePointId = edgePointId;
  }

  public EdgePointBaseIfEntity getBaseIf() {
    return baseIf;
  }

  public void setBaseIf(EdgePointBaseIfEntity baseIf) {
    this.baseIf = baseIf;
  }

  public EdgePointSupportProtocolForOwnerEntity getSupportProtocols() {
    return supportProtocols;
  }

  public void setSupportProtocols(EdgePointSupportProtocolForOwnerEntity supportProtocols) {
    this.supportProtocols = supportProtocols;
  }

  public EdgePointForOwnerQosEntity getQos() {
    return qos;
  }

  public void setQos(EdgePointForOwnerQosEntity qos) {
    this.qos = qos;
  }

  public Double getTrafficThreshold() {
    return trafficThreshold;
  }

  public void setTrafficThreshold(Double trafficThreshold) {
    this.trafficThreshold = trafficThreshold;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
