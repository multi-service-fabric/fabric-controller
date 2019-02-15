
package msf.fc.services.filter.rest.ec.filters.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class BreakoutIfIdEcEntity {

  @SerializedName("base_physical_if_id")
  private String basePhysicalIfId;

  @SerializedName("breakout_if_id")
  private String breakoutIfId;

  public String getBasePhysicalIfId() {
    return basePhysicalIfId;
  }

  public void setBasePhysicalIfId(String basePhysicalIfId) {
    this.basePhysicalIfId = basePhysicalIfId;
  }

  public String getBreakoutIfId() {
    return breakoutIfId;
  }

  public void setBreakoutIfId(String breakoutIfId) {
    this.breakoutIfId = breakoutIfId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
