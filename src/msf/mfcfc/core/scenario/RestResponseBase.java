
package msf.mfcfc.core.scenario;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.MsfExclude;

public class RestResponseBase {

  protected int httpStatusCode;

  protected String responseBody;

  @MsfExclude
  protected boolean failedSendRequest = false;

  @MsfExclude
  protected boolean failedRegistRecord = false;

  @MsfExclude
  protected boolean failedRollback = false;

  public RestResponseBase() {

  }

  public RestResponseBase(int httpStatusCode, String responseBodyString) {
    this.httpStatusCode = httpStatusCode;
    this.responseBody = responseBodyString;
  }

  public RestResponseBase(int httpStatusCode, AbstractResponseBody responseBody) {
    this.httpStatusCode = httpStatusCode;
    this.responseBody = JsonUtil.toJson(responseBody);
  }

  public int getHttpStatusCode() {
    return httpStatusCode;
  }

  public void setHttpStatusCode(int httpStatusCode) {
    this.httpStatusCode = httpStatusCode;
  }

  public String getResponseBody() {
    return responseBody;
  }

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
