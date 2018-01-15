package msf.fc.rest.ec.log.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class LogReadEcEntity {

  
  @SerializedName("conditions")
  private LogConditionsEcEntity conditions;

  
  @SerializedName("ec_log")
  private LogEcLogEcEntity ecLog;

  
  @SerializedName("em_log")
  private LogEmLogEcEntity emLog;

  
  public LogConditionsEcEntity getConditions() {
    return conditions;
  }

  
  public void setConditions(LogConditionsEcEntity conditions) {
    this.conditions = conditions;
  }

  
  public LogEcLogEcEntity getEcLog() {
    return ecLog;
  }

  
  public void setEcLog(LogEcLogEcEntity ecLog) {
    this.ecLog = ecLog;
  }

  
  public LogEmLogEcEntity getEmLog() {
    return emLog;
  }

  
  public void setEmLog(LogEmLogEcEntity emLog) {
    this.emLog = emLog;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
