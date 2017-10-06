package msf.fc.traffic.history.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.core.scenario.RestRequestBase;

public class L2TrafficRequest extends RestRequestBase {

  private String sliceId;
  private String startTime;
  private String endTime;
  private Integer interval;

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public Integer getInterval() {
    return interval;
  }

  public void setInterval(Integer interval) {
    this.interval = interval;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
