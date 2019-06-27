
package msf.mfcfc.services.ctrlstsnotify.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class FailureInfoMemoryEntity {

  @SerializedName("used")
  private Integer used;

  @SerializedName("free")
  private Integer free;

  public Integer getUsed() {
    return used;
  }

  public void setUsed(Integer used) {
    this.used = used;
  }

  public Integer getFree() {
    return free;
  }

  public void setFree(Integer free) {
    this.free = free;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
