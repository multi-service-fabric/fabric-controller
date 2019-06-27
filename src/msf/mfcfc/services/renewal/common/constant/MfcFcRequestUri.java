
package msf.mfcfc.services.renewal.common.constant;

import java.util.regex.Pattern;

import msf.mfcfc.common.constant.HttpMethod;

public enum MfcFcRequestUri {

  CTL_CONSTRUCTION_EXECUTION(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/manage/renewal";
    }
  }, Pattern.compile("^/v1/manage/renewal$")),

  CTL_SWITCHOVER(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/manage/switchover";
    }
  }, Pattern.compile("^/v1/manage/switchover$"));

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
