
package msf.mfcfc.core.scenario;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.MsfExclude;

/**
 * Base class for REST response.
 *
 * @author NTT
 *
 */
public class RestResponseBase {

  protected int httpStatusCode;

  protected String responseBody;

  @MsfExclude
  protected boolean failedSendRequest = false;

  @MsfExclude
  protected boolean failedRegistRecord = false;

  @MsfExclude
  protected boolean failedRollback = false;

  /**
   * Default constructor.
   */
  public RestResponseBase() {

  }

  /**
   * Constructor to initialize the status code and the body with a JSON string.
   *
   * @param httpStatusCode
   *          HTTP status code to set in HTTP response.
   * @param responseBodyString
   *          Data to be set in the HTTP response body (JSON format)
   */
  public RestResponseBase(int httpStatusCode, String responseBodyString) {
    this.httpStatusCode = httpStatusCode;
    this.responseBody = responseBodyString;
  }

  /**
   * Constructor to initialize the status code and the body with a body object.
   *
   * @param httpStatusCode
   *          HTTP status code to set in HTTP response.
   * @param responseBody
   *          Data to be set in the HTTP response body (Response body object)
   */
  public RestResponseBase(int httpStatusCode, AbstractResponseBody responseBody) {
    this.httpStatusCode = httpStatusCode;
    this.responseBody = JsonUtil.toJson(responseBody);
  }

  /**
   * Get the HTTP status code.
   *
   * @return HTTP status code.
   */
  public int getHttpStatusCode() {
    return httpStatusCode;
  }

  /**
   * Set the HTTP status code.
   * 
   * @param httpStatusCode
   *          HTTP status code.
   */
  public void setHttpStatusCode(int httpStatusCode) {
    this.httpStatusCode = httpStatusCode;
  }

  /**
   * Get the data to be set in the HTTP response body (JSON format).
   *
   * @return Data to set in HTTP response body (JSON format)
   */
  public String getResponseBody() {
    return responseBody;
  }

  /**
   * Set the data in the HTTP response body (JSON format).
   *
   * @param responseBody
   *          Data in HTTP response body (JSON format).
   */
  public void setResponseBody(String responseBody) {
    this.responseBody = responseBody;
  }

  public boolean isFailedSendRequest() {
    return failedSendRequest;
  }

  public void setFailedSendRequest(boolean failedSendRequest) {
    this.failedSendRequest = failedSendRequest;
  }

  public boolean isFailedRegistRecord() {
    return failedRegistRecord;
  }

  public void setFailedRegistRecord(boolean failedRegistRecord) {
    this.failedRegistRecord = failedRegistRecord;
  }

  public boolean isFailedRollback() {
    return failedRollback;
  }

  public void setFailedRollback(boolean failedRollback) {
    this.failedRollback = failedRollback;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
