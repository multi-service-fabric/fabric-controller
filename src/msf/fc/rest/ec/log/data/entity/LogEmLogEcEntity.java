package msf.fc.rest.ec.log.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class LogEmLogEcEntity {

  
  @SerializedName("data_number")
  private Integer dataNumber;

  
  @SerializedName("over_limit_number")
  private Boolean overLimitNumber;

  
  @SerializedName("server_name")
  private String serverName;

  
  @SerializedName("log_data")
  private List<LogMessageEcEntity> logDataList;

  
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

  
  public String getServerName() {
    return serverName;
  }

  
  public void setServerName(String serverName) {
    this.serverName = serverName;
  }

  
  public List<LogMessageEcEntity> getLogDataList() {
    return logDataList;
  }

  
  public void setLogDataList(List<LogMessageEcEntity> logDataList) {
    this.logDataList = logDataList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
