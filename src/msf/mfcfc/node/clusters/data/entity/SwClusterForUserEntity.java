
package msf.mfcfc.node.clusters.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SwClusterForUserEntity {

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("edge_points")
  private SwClusterEdgePointEntity edgePoints;

  @SerializedName("uni_support_protocols")
  private SwClusterUniSupportProtocolEntity uniSupportProtocols;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public SwClusterEdgePointEntity getEdgePoints() {
    return edgePoints;
  }

  public void setEdgePoints(SwClusterEdgePointEntity edgePoints) {
    this.edgePoints = edgePoints;
  }

  public SwClusterUniSupportProtocolEntity getUniSupportProtocols() {
    return uniSupportProtocols;
  }

  public void setUniSupportProtocols(SwClusterUniSupportProtocolEntity uniSupportProtocols) {
    this.uniSupportProtocols = uniSupportProtocols;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
