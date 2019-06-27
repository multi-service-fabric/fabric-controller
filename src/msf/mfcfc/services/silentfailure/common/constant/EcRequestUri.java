
package msf.mfcfc.services.silentfailure.common.constant;

import msf.mfcfc.common.constant.HttpMethod;

public enum EcRequestUri {

  PING_BETWEEN_DEVICES(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/ec_ctrl/link-status/ping";
    }
  }),

  OSPF_NEIGHBOR_READ(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/ec_ctrl/link-status/ospf-neighbor";
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
