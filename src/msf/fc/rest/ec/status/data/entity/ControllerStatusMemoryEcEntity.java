
package msf.fc.rest.ec.status.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class ControllerStatusMemoryEcEntity {

  @SerializedName("used")
  private Integer used;

  @SerializedName("free")
  private Integer free;

  @SerializedName("buff_cache")
  private Integer buffCache;

  @SerializedName("swpd")
  private Integer swpd;

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

  public Integer getBuffCache() {
    return buffCache;
  }

  public void setBuffCache(Integer buffCache) {
    this.buffCache = buffCache;
  }

  public Integer getSwpd() {
    return swpd;
  }

  public void setSwpd(Integer swpd) {
    this.swpd = swpd;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
