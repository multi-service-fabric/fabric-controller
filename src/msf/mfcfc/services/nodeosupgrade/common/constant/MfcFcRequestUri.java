
package msf.mfcfc.services.nodeosupgrade.common.constant;

import static msf.mfcfc.common.util.ParameterCheckUtil.*;

import java.util.regex.Pattern;

import msf.mfcfc.common.constant.HttpMethod;

public enum MfcFcRequestUri {

  NODE_DETOUR_UPDATE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/detour/clusters/" + args[0] + "/nodes/" + args[1] + "/" + args[2];
    }
  }, Pattern.compile("^/v1/detour/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/"
      + URI_PATTERN_MATCHER + "$")),

  NODE_DETOUR_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/detour/clusters/" + args[0] + "/nodes";
    }
  }, Pattern.compile("^/v1/detour/clusters/" + URI_PATTERN_MATCHER + "/nodes$")),

  NODE_OS_UPGRADE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/upgrade_operations";
    }
  }, Pattern.compile("^/v1/upgrade_operations$")),

  NODE_OS_UPGRADE_CANCEL(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/upgrade_operations/" + args[0];
    }
  }, Pattern.compile("^/v1/upgrade_operations/" + URI_PATTERN_MATCHER + "$")),

  NOTIFY_NODE_OS_UPGRADE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/MSFcontroller/upgrade_operations";
    }
  }, Pattern.compile("^/v1/MSFcontroller/upgrade_operations$")),

  NOTIFY_NODE_OS_UPGRADE_COMPLETED(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/upgrade_operations";
    }
  }, Pattern.compile("^/v1/internal/upgrade_operations$"));

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
