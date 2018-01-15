package msf.mfcfc.core.operation.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;


public class OperationResponseDetailEntity {

  
  @SerializedName("status_code")
  private Integer statusCode;

  
  public Integer getStatusCode() {
    return statusCode;
  }

  
  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
