
package msf.mfcfc.services.ctrlstsnotify.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class FailureInfoCpuEntity {

  @SerializedName("use_rate")
  private Float useRate;

  public Float getUseRate() {
    return useRate;
  }

  public void setUseRate(Float useRate) {
    this.useRate = useRate;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
