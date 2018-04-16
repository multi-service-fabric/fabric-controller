
package msf.mfcfc.core.scenario;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.HttpMethod;

public class RestRequestBase {

  public RestRequestBase() {
  }

  public RestRequestBase(String requestBody, String notificationAddress, String notificationPort) {
    this.requestBody = requestBody;
    this.notificationAddress = notificationAddress;
    this.notificationPort = notificationPort;
  }

  private String requestBody;

  private String requestUri;

  private String requestMethod;

  private String requestQueryString;

  private String notificationAddress;

  private String notificationPort;

  private String sourceIpAddress;

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

  public HttpMethod getRequestMethodEnum() {
    return HttpMethod.getEnumFromMessage(requestMethod);
  }

  public void setRequestMethodEnum(HttpMethod httpMethod) {
    this.requestMethod = httpMethod.getMessage();
  }

  public String getRequestQueryString() {
    return requestQueryString;
  }

  public void setRequestQueryString(String requestQueryString) {
    this.requestQueryString = requestQueryString;
  }

  public String getNotificationAddress() {
    return notificationAddress;
  }

  public void setNotificationAddress(String notificationAddress) {
    this.notificationAddress = notificationAddress;
  }

  public String getNotificationPort() {
    return notificationPort;
  }

  public void setNotificationPort(String notificationPort) {
    this.notificationPort = notificationPort;
  }

  public String getSourceIpAddress() {
    return sourceIpAddress;
  }

  public void setSourceIpAddress(String sourceIpAddress) {
    this.sourceIpAddress = sourceIpAddress;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
