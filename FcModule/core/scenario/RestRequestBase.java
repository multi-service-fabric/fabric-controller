package msf.fc.core.scenario;

public class RestRequestBase {

  private String requestBody;

  private String requestUri;

  private String requestMethod;

  public String getRequestBody() {
    return requestBody;
  }

  public void setRequestBody(String requestBody) {
    this.requestBody = requestBody;
  }

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
    return "RestRequestBase [requestBody=" + requestBody + ", requestUri=" + requestUri + ", requestMethod="
        + requestMethod + "]";
  }

}
