
package msf.mfcfc.core.log.scenario.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LogConditionsEntity {

  @SerializedName("log_type")
  private String logType;

  @SerializedName("log_levels")
  private List<String> logLevelList;

  @SerializedName("controllers")
  private List<String> controllerList;

  @SerializedName("clusters")
  private List<String> clusterList;

  @SerializedName("start_date")
  private String startDate;

  @SerializedName("end_date")
  private String endDate;

  @SerializedName("limit_number")
  private Integer limitNumber;

  @SerializedName("search_string")
  private String searchString;

  @SerializedName("merge_type")
  private String mergeType;

  public String getLogType() {
    return logType;
  }

  public void setLogType(String logType) {
    this.logType = logType;
  }

  public List<String> getLogLevelList() {
    return logLevelList;
  }

  public void setLogLevelList(List<String> logLevelList) {
    this.logLevelList = logLevelList;
  }

  public List<String> getControllerList() {
    return controllerList;
  }

  public void setControllerList(List<String> controllerList) {
    this.controllerList = controllerList;
  }

  public List<String> getClusterList() {
    return clusterList;
  }

  public void setClusterList(List<String> clusterList) {
    this.clusterList = clusterList;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public Integer getLimitNumber() {
    return limitNumber;
  }

  public void setLimitNumber(Integer limitNumber) {
    this.limitNumber = limitNumber;
  }

  public String getSearchString() {
    return searchString;
  }

  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  public String getMergeType() {
    return mergeType;
  }

  public void setMergeType(String mergeType) {
    this.mergeType = mergeType;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
