
package msf.fc.rest.ec.traffic.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.traffic.data.entity.TrafficInfoSwitchTrafficCollectAllEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class TrafficInfoCollectAllTrafficEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("is_success")
  private Boolean isSuccess;

  @SerializedName("time")
  private String time;

  @SerializedName("interval")
  private Integer interval;

  @SerializedName("switch_traffics")
  private List<TrafficInfoSwitchTrafficCollectAllEcEntity> switchTrafficList;

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

  public List<TrafficInfoSwitchTrafficCollectAllEcEntity> getSwitchTrafficList() {
    return switchTrafficList;
  }

  public void setSwitchTrafficList(List<TrafficInfoSwitchTrafficCollectAllEcEntity> switchTrafficList) {
    this.switchTrafficList = switchTrafficList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
