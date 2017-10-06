package msf.fc.rest.ec.node.interfaces.lag.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class ReadLagIfEcEntity {

  @SerializedName("lag_if_id")
  private String lagIfId;

  @SerializedName("if_name")
  private String ifName;

  public String getLagIfId() {
    return lagIfId;
  }

  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  public String getIfName() {
    return ifName;
  }

  public void setIfName(String ifName) {
    this.ifName = ifName;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
