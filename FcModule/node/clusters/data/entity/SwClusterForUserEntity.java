package msf.fc.node.clusters.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SwClusterForUserEntity {

  @SerializedName("sw_cluster_id")
  private String swClusterId;

  @SerializedName("edge_points")
  private EdgePointEntity edgePointList;
  @SerializedName("uni_support_protocols")
  private UniSupportProtocolEntity uniSupportProtocol;

  public String getSwClusterId() {
    return swClusterId;
  }

  public void setSwClusterId(String swClusterId) {
    this.swClusterId = swClusterId;
  }

  public EdgePointEntity getEdgePoint() {
    return edgePointList;
  }

  public void setEdgePoint(EdgePointEntity edgePointList) {
    this.edgePointList = edgePointList;
  }

  public UniSupportProtocolEntity getUniSupportProtocol() {
    return uniSupportProtocol;
  }

  public void setUniSupportProtocol(UniSupportProtocolEntity uniSupportProtocol) {
    this.uniSupportProtocol = uniSupportProtocol;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
