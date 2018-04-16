
package msf.mfcfc.core.log.scenario.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.core.log.scenario.data.entity.LogMsfLogEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

public class LogReadResponseBody extends AbstractResponseBody {

  @SerializedName("msf_log")
  private LogMsfLogEntity msfLog;

  public LogMsfLogEntity getMsfLog() {
    return msfLog;
  }

  public void setMsfLog(LogMsfLogEntity msfLog) {
    this.msfLog = msfLog;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
