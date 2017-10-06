package msf.fc.core.operation.scenario.data.entity;

import com.google.gson.annotations.SerializedName;

public class RequestEntity {

  @SerializedName("uri")
  private String requestUri;

  @SerializedName("method")
  private String requestMethod;

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

  @Override
  public String toString() {
    return "RequestEntity [requestUri=" + requestUri + ", requestMethod=" + requestMethod + "]";
  }

}
