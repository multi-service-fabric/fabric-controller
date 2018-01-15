package msf.mfcfc.core.log.scenario.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class LogMsfLogEntity {

  
  @SerializedName("conditions")
  private LogConditionsEntity conditions;

  
  @SerializedName("merged_log")
  private LogMergedLogEntity mergedLog;

  
  @SerializedName("mfc_logs")
  private List<LogMfcLogsEntity> mfcLogList;

  
  @SerializedName("cluster_logs")
  private List<LogClusterLogsEntity> clusterLogList;

  
  public LogConditionsEntity getConditions() {
    return conditions;
  }

  
  public void setConditions(LogConditionsEntity conditions) {
    this.conditions = conditions;
  }

  
  public LogMergedLogEntity getMergedLog() {
    return mergedLog;
  }

  
  public void setMergedLog(LogMergedLogEntity mergedLog) {
    this.mergedLog = mergedLog;
  }

  
  public List<LogMfcLogsEntity> getMfcLogList() {
    return mfcLogList;
  }

  
  public void setMfcLogList(List<LogMfcLogsEntity> mfcLogList) {
    this.mfcLogList = mfcLogList;
  }

  
  public List<LogClusterLogsEntity> getClusterLogList() {
    return clusterLogList;
  }

  
  public void setClusterLogList(List<LogClusterLogsEntity> clusterLogList) {
    this.clusterLogList = clusterLogList;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
