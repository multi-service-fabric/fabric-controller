package msf.fc.core.operation.scenario.data.entity;

import com.google.gson.annotations.SerializedName;

public class ResponseEntity {

  @SerializedName("status_code")
  private Integer responseStatusCode;

  public Integer getResponseStatusCode() {
    return responseStatusCode;
  }

  public void setResponseStatusCode(Integer responseStatusCode) {
    this.responseStatusCode = responseStatusCode;
  }

  @Override
  public String toString() {
    return "ResponseEntity [responseStatusCode=" + responseStatusCode + "]";
  }

}
