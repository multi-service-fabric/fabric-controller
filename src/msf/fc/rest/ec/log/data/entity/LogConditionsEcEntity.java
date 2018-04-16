
package msf.fc.rest.ec.log.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class LogConditionsEcEntity {

  @SerializedName("start_date")
  private String startDate;

  @SerializedName("end_date")
  private String endDate;

  @SerializedName("limit_number")
  private Integer limitNumber;

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public Integer getLimitNumber() {
    return limitNumber;
  }

  public void setLimitNumber(Integer limitNumber) {
    this.limitNumber = limitNumber;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
