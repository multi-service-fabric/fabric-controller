
package msf.fc.rest.ec.traffic.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class TrafficInfoSwitchTrafficCollectEcEntity {

  @SerializedName("traffic_values")
  private List<TrafficInfoTrafficValueEcEntity> trafficValueList;

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
