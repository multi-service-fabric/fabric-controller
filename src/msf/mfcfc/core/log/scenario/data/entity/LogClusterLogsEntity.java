
package msf.mfcfc.core.log.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LogClusterLogsEntity {

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("fc_log")
  private LogFcLogEntity fcLog;

  @SerializedName("ec_log")
  private LogEcLogEntity ecLog;

  @SerializedName("em_log")
  private LogEmLogEntity emLog;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public LogFcLogEntity getFcLog() {
    return fcLog;
  }

  public void setFcLog(LogFcLogEntity fcLog) {
    this.fcLog = fcLog;
  }

  public LogEcLogEntity getEcLog() {
    return ecLog;
  }

  public void setEcLog(LogEcLogEntity ecLog) {
    this.ecLog = ecLog;
  }

  public LogEmLogEntity getEmLog() {
    return emLog;
  }

  public void setEmLog(LogEmLogEntity emLog) {
    this.emLog = emLog;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
