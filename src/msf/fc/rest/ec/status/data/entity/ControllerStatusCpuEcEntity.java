
package msf.fc.rest.ec.status.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class ControllerStatusCpuEcEntity {

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
