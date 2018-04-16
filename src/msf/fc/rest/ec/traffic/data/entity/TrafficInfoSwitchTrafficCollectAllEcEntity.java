
package msf.fc.rest.ec.traffic.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class TrafficInfoSwitchTrafficCollectAllEcEntity {

  @SerializedName("node_id")
  private String nodeId;

  @SerializedName("traffic_values")
  private List<TrafficInfoTrafficValueEcEntity> trafficValueList;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public List<TrafficInfoTrafficValueEcEntity> getTrafficValueList() {
    return trafficValueList;
  }

  public void setTrafficValueList(List<TrafficInfoTrafficValueEcEntity> trafficValueList) {
    this.trafficValueList = trafficValueList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
