package msf.fc.core.operation.scenario.data.entity;

import com.google.gson.annotations.SerializedName;

public class RequestDetailEntity {

  @SerializedName("uri")
  private String requestUri;

  @SerializedName("method")
  private String requestMethod;

  @SerializedName("body")
  private String requestBody;

  public String getRequestUri() {
    return requestUri;
  }

  public void setRequestUri(String requestUri) {
    this.requestUri = requestUri;
  }

  public String getRequestMethod() {
    return requestMethod;
  }

  public void setRequestMethod(String requestMethod) {
    this.requestMethod = requestMethod;
  }

  public String getRequestBody() {
    return requestBody;
  }

  public void setRequestBody(String requestBody) {
    this.requestBody = requestBody;
  }

  @Override
  public String toString() {
    return "RequestDetailEntity [requestUri=" + requestUri + ", requestMethod=" + requestMethod + ", requestBody="
        + requestBody + "]";
  }

}
