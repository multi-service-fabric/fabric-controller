
package msf.mfcfc.services.ctrlstsnotify.common.constant;

import java.util.regex.Pattern;

import msf.mfcfc.common.constant.HttpMethod;

public enum MfcFcRequestUri {

  INTERNAL_NOTIFY_CONTROLLER_FAILURE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/controller_status_notification/ec_em/failure";
    }
  }, Pattern.compile("^/v1/internal/controller_status_notification/ec_em/failure$")),

  INTERNAL_NOTIFY_CONTROLLER_LOG(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/controller_status_notification/ec_em/log";
    }
  }, Pattern.compile("^/v1/internal/controller_status_notification/ec_em/log$")),

  NOTEIFY_CONTOROLLER_FAILURE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/MSFcontroller/controller_status_notification/failure";
    }
  }, Pattern.compile("^/v1/MSFcontroller/controller_status_notification/failure$")),

  NOTIFY_CONTROLLER_LOG(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/MSFcontroller/controller_status_notification/log";
    }
  }, Pattern.compile("^/v1/MSFcontroller/controller_status_notification/log$"));

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
