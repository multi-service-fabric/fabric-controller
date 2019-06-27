
package msf.mfcfc.services.renewal.common.constant;

import msf.mfcfc.common.constant.HttpMethod;

public enum EcRequestUri {

  SWITCH_SYSTEM_TYPE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/ec_ctrl/ctrl-switch?" + args[0];
    }
  });

  private HttpMethod httpMethod;

  private RequestUriInterface requestUri;

  private EcRequestUri(HttpMethod httpMethod, RequestUriInterface requestUri) {
    this.httpMethod = httpMethod;
    this.requestUri = requestUri;
  }

  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  public String getUri(String... uriParams) {
    return requestUri.makeUri(uriParams);
  }

  private interface RequestUriInterface {
    public String makeUri(String... args);
  }
}
