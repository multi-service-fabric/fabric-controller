package msf.fc.core.operation.scenario.data.entity;

import com.google.gson.annotations.SerializedName;

public class ResponseDetailEntity {

  @SerializedName("status_code")
  private Integer responseStatusCode;

  @SerializedName("body")
  private String responseBody;

  public Integer getResponseStatusCode() {
    return responseStatusCode;
  }

  public void setResponseStatusCode(Integer responseStatusCode) {
    this.responseStatusCode = responseStatusCode;
  }

  public String getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(String responseBody) {
    this.responseBody = responseBody;
  }

  @Override
  public String toString() {
    return "ResponseDetailEntity [responseStatusCode=" + responseStatusCode + ", responseBody=" + responseBody + "]";
  }

}
