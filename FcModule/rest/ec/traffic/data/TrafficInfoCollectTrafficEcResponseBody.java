package msf.fc.rest.ec.traffic.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.common.AbstractInternalResponseBody;
import msf.fc.rest.ec.traffic.data.entity.SwitchTrafficEcEntity;


public class TrafficInfoCollectTrafficEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("is_success")
  private Boolean isSuccess;

  @SerializedName("time")
  private String time;

  @SerializedName("interval")
  private Integer interval;

  @SerializedName("switch_traffic")
  private List<SwitchTrafficEcEntity> switchTrafficList;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public Boolean isSuccess() {
    return isSuccess;
  }

  public void setSuccess(Boolean isSuccess) {
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

  public List<SwitchTrafficEcEntity> getSwitchTrafficList() {
    return switchTrafficList;
  }

  public void setSwitchTrafficList(List<SwitchTrafficEcEntity> switchTrafficList) {
    this.switchTrafficList = switchTrafficList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
