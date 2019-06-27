
package msf.mfcfc.services.silentfailure.common.constant;

import java.util.regex.Pattern;

import msf.mfcfc.common.constant.HttpMethod;

public enum MfcFcRequestUri {

  SILENT_FAULT_NOTIFY(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/MSFcontroller/silent_failure";
    }
  }, Pattern.compile("^/v1/MSFcontroller/silent_failure$"));

  private HttpMethod httpMethod;

  private RequestUriInterface requestUri;

  private Pattern uriPattern;

  private MfcFcRequestUri(HttpMethod httpMethod, RequestUriInterface requestUri, Pattern uriPattern) {
    this.httpMethod = httpMethod;
    this.requestUri = requestUri;
    this.uriPattern = uriPattern;
  }

  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  public String getUri(String... uriParams) {
    return requestUri.makeUri(uriParams);
  }

  public Pattern getUriPattern() {
    return uriPattern;
  }

  private interface RequestUriInterface {
    public String makeUri(String... args);
  }
}
