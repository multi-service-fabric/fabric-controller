package msf.fc.rest.ec.traffic.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.traffic.data.entity.TrafficInfoSwitchTrafficCollectEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;


public class TrafficInfoCollectTrafficEcResponseBody extends AbstractInternalResponseBody {

  
  @SerializedName("is_success")
  private Boolean isSuccess;

  
  @SerializedName("time")
  private String time;

  
  @SerializedName("interval")
  private Integer interval;

  
  @SerializedName("node_id")
  private String nodeId;

  
  @SerializedName("switch_traffic")
  private TrafficInfoSwitchTrafficCollectEcEntity switchTraffic;

  
  public Boolean getIsSuccess() {
    return isSuccess;
  }

  
  public void setIsSuccess(Boolean isSuccess) {
    this.isSuccess = isSuccess;
  }

  
  public String getTime() {
    return time;
  }

  
  public void setTime(String time) {
    this.time = time;
  }

  
  public Integer getInterval() {
    return interval;
  }

  
  public void setInterval(Integer interval) {
    this.interval = interval;
  }

  
  public String getNodeId() {
    return nodeId;
  }

  
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  
  public TrafficInfoSwitchTrafficCollectEcEntity getSwitchTraffic() {
    return switchTraffic;
  }

  
  public void setSwitchTraffic(TrafficInfoSwitchTrafficCollectEcEntity switchTraffic) {
    this.switchTraffic = switchTraffic;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
