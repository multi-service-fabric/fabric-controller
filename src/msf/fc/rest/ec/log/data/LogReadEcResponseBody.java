
package msf.fc.rest.ec.log.data;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.fc.rest.ec.log.data.entity.LogReadEcEntity;
import msf.mfcfc.rest.common.AbstractInternalResponseBody;

public class LogReadEcResponseBody extends AbstractInternalResponseBody {

  @SerializedName("ecem_log")
  private LogReadEcEntity ecemLog;

  public LogReadEcEntity getEcemLog() {
    return ecemLog;
  }

  public void setEcemLog(LogReadEcEntity ecemLog) {
    this.ecemLog = ecemLog;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
