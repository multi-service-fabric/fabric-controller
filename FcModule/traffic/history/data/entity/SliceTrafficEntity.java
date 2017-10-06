package msf.fc.traffic.history.data.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SliceTrafficEntity {
  @SerializedName("slice_type")
  private String sliceType;

  @SerializedName("slice_id")
  private String sliceId;

  @SerializedName("start_time")
  private String startTime;

  @SerializedName("end_time")
  private String endTime;

  private Integer interval;

  @SerializedName("total_traffic")
  private Integer totalTraffic;

  @SerializedName("total_data_num")
  private Integer totalDataNum;
  @SerializedName("traffic_data")
  private List<TrafficDataEntity> trafficDataList = new ArrayList<>();

  public String getSliceType() {
    return sliceType;
  }

  public void setSliceType(String sliceType) {
    this.sliceType = sliceType;
  }

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

  public Integer getTotalTraffic() {
    return totalTraffic;
  }

  public void setTotalTraffic(Integer totalTraffic) {
    this.totalTraffic = totalTraffic;
  }

  public Integer getTotalDataNum() {
    return totalDataNum;
  }

  public void setTotalDataNum(Integer totalDataNum) {
    this.totalDataNum = totalDataNum;
  }

  public List<TrafficDataEntity> getTrafficDataList() {
    return trafficDataList;
  }

  public void setTrafficDataList(List<TrafficDataEntity> trafficDataList) {
    this.trafficDataList = trafficDataList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
