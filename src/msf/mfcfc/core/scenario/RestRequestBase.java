
package msf.mfcfc.core.scenario;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.HttpMethod;

/**
 * Base class for REST request.
 *
 * @author NTT
 *
 */
public class RestRequestBase {

  private String requestBody;

  private String requestUri;

  private String requestMethod;

  private String requestQueryString;

  private String notificationAddress;

  private String notificationPort;

  private String sourceIpAddress;

  /**
   * Default constructor.
   */
  public RestRequestBase() {
  }

  /**
   * Constructor with arguments that can be set in a handler as parameters.
   *
   * @param requestBody
   *          Data set in the HTTP request body (JSON format)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   */
  public RestRequestBase(String requestBody, String notificationAddress, String notificationPort) {
    this.requestBody = requestBody;
    this.notificationAddress = notificationAddress;
    this.notificationPort = notificationPort;
  }

  /**
   * Get the data to set in HTTP request body (JSON format).
   *
   * @return Data set in the HTTP request body (JSON format)
   */
  public String getRequestBody() {
    return requestBody;
  }

  /**
   * Set the data to set in HTTP request body (JSON format).
   *
   * @param requestBody
   *          Data set in the HTTP request body (JSON format)
   */
  public void setRequestBody(String requestBody) {
    this.requestBody = requestBody;
  }

  /**
   * Get the HTTP request URI.
   *
   * @return Request URI
   */
  public String getRequestUri() {
    return requestUri;
  }

  /**
   * Set the HTTP request URI.
   *
   * @param requestUri
   *          Request URI
   */
  public void setRequestUri(String requestUri) {
    this.requestUri = requestUri;
  }

  /**
   * Get the HTTP request method.
   *
   * @return HTTP request method
   */
  public String getRequestMethod() {
    return requestMethod;
  }

  /**
   * Set the HTTP request method.
   *
   * @param requestMethod
   *          HTTP request method
   */
  public void setRequestMethod(String requestMethod) {
    this.requestMethod = requestMethod;
  }

  /**
   * Get the HTTP Request method enumeration.
   *
   * @return HTTP Request method enumeration
   */
  public HttpMethod getRequestMethodEnum() {
    return HttpMethod.getEnumFromMessage(requestMethod);
  }

  /**
   * Set the HTTP Request method from the enumeration value.
   *
   * @param httpMethod
   *          HTTP Request method enumeration
   */
  public void setRequestMethodEnum(HttpMethod httpMethod) {
    this.requestMethod = httpMethod.getMessage();
  }

  /**
   * Get the HTTP query string.
   *
   * @return HTTP query string
   */
  public String getRequestQueryString() {
    return requestQueryString;
  }

  /**
   * Set the HTTP query string.
   *
   * @param requestQueryString
   *          HTTP query string
   */
  public void setRequestQueryString(String requestQueryString) {
    this.requestQueryString = requestQueryString;
  }

  /**
   * Get the operation completion notification address.
   *
   * @return notificationAddress オペレーション完了通知先アドレス
   */
  public String getNotificationAddress() {
    return notificationAddress;
  }

  /**
   * Set the operation completion notification address.
   *
   * @param notificationAddress
   *          Operation completion notification address
   */
  public void setNotificationAddress(String notificationAddress) {
    this.notificationAddress = notificationAddress;
  }

  /**
   * Get the operation completion notification port.
   *
   * @return notificationPort Operation completion notification port
   */
  public String getNotificationPort() {
    return notificationPort;
  }

  /**
   * Set the operation completion notification port.
   *
   * @param notificationPort
   *          Operation completion notification port
   */
  public void setNotificationPort(String notificationPort) {
    this.notificationPort = notificationPort;
  }

  /**
   * Get the request source IP address.
   *
   * @return Request source IP address
   */
  public String getSourceIpAddress() {
    return sourceIpAddress;
  }

  /**
   * Set the request source IP address.
   *
   * @param sourceIpAddress
   *          Request source IP address
   */
  public void setSourceIpAddress(String sourceIpAddress) {
    this.sourceIpAddress = sourceIpAddress;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
