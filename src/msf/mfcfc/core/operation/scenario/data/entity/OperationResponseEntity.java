
package msf.mfcfc.core.operation.scenario.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationResponseEntity {

  @SerializedName("status_code")
  private Integer statusCode;

  @SerializedName("body")
  private String body;

  public OperationResponseEntity() {
  }

  public OperationResponseEntity(Integer statusCode, String body) {
    super();
    this.statusCode = statusCode;
    this.body = body;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
