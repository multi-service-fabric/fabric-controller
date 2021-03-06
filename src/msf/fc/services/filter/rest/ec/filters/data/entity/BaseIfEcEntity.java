
package msf.fc.services.filter.rest.ec.filters.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class BaseIfEcEntity {

  @SerializedName("if_type")
  private String ifType;

  @SerializedName("if_id")
  private String ifId;

  public String getIfType() {
    return ifType;
  }

  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  public String getIfId() {
    return ifId;
  }

  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
