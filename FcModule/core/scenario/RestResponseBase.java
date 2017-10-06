package msf.fc.core.scenario;

import msf.fc.rest.common.AbstractResponseBody;
import msf.fc.rest.common.JsonUtil;

public class RestResponseBase {

  protected int httpStatusCode;

  protected String responseBody;

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

  public String getResponseBody() {
    return responseBody;
  }

  @Override
  public String toString() {
    return "RestResponseBase [httpStatusCode=" + httpStatusCode + ", responseBody=" + responseBody + "]";
  }

}
