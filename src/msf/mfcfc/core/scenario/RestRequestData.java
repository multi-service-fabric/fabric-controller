
package msf.mfcfc.core.scenario;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.HttpMethod;

/**
 * REST request information with the destination address.
 *
 * @author NTT
 *
 */
public class RestRequestData {

  private int clusterId;

  private String ipAddress;

  private int port;

  private HttpMethod httpMethod;

  private String targetUri;

  private RestRequestBase request;

  private int expectHttpStatusCode;

  private String lowerOperationId;

  /**
   * Constructor.
   */
  public RestRequestData() {

  }

  /**
   * Constructor.
   *
   * @param clusterId
   *          Destination SW cluster ID
   * @param ipAddress
   *          Destination IP address
   * @param port
   *          Destination port number
   * @param httpMethod
   *          HTTP method to send
   * @param targetUri
   *          URI path for the HTTP request target (including URI parameter,
   *          optional parameter, but excluding address, port)
   * @param request
   *          Data to send
   * @param expectHttpStatusCode
   *          The expected HTTP response code for this request
   */
  public RestRequestData(int clusterId, String ipAddress, int port, HttpMethod httpMethod, String targetUri,
      RestRequestBase request, int expectHttpStatusCode) {
    super();
    this.clusterId = clusterId;
    this.ipAddress = ipAddress;
    this.port = port;
    this.httpMethod = httpMethod;
    this.targetUri = targetUri;
    this.request = request;
    this.expectHttpStatusCode = expectHttpStatusCode;
  }

  public int getClusterId() {
    return clusterId;
  }

  public void setClusterId(int clusterId) {
    this.clusterId = clusterId;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  public void setHttpMethod(HttpMethod httpMethod) {
    this.httpMethod = httpMethod;
  }

  public String getTargetUri() {
    return targetUri;
  }

  public void setTargetUri(String targetUri) {
    this.targetUri = targetUri;
  }

  public RestRequestBase getRequest() {
    return request;
  }

  public void setRequest(RestRequestBase request) {
    this.request = request;
  }

  public int getExpectHttpStatusCode() {
    return expectHttpStatusCode;
  }

  public void setExpectHttpStatusCode(int expectHttpStatusCode) {
    this.expectHttpStatusCode = expectHttpStatusCode;
  }

  public String getLowerOperationId() {
    return lowerOperationId;
  }

  public void setLowerOperationId(String lowerOperationId) {
    this.lowerOperationId = lowerOperationId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
