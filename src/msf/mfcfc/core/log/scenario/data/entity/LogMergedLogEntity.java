package msf.mfcfc.core.log.scenario.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class LogMergedLogEntity {

  
  @SerializedName("data_number")
  private Integer dataNumber;

  
  @SerializedName("over_limit_number")
  private Boolean overLimitNumber;

  
  @SerializedName("log_data")
  private List<LogMergedDataEntity> logDataList;

  
  public Integer getDataNumber() {
    return dataNumber;
  }

  
  public void setDataNumber(Integer dataNumber) {
    this.dataNumber = dataNumber;
  }

  
  public Boolean getOverLimitNumber() {
    return overLimitNumber;
  }

  
  public void setOverLimitNumber(Boolean overLimitNumber) {
    this.overLimitNumber = overLimitNumber;
  }

  
  public List<LogMergedDataEntity> getLogDataList() {
    return logDataList;
  }

  
  public void setLogDataList(List<LogMergedDataEntity> logDataList) {
    this.logDataList = logDataList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
