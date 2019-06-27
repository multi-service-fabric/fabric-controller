
package msf.mfcfc.services.priorityroutes.common.constant;

import msf.mfcfc.common.constant.HttpMethod;

public enum EcRequestUri {

  INTERNAL_LINK_IF_UPDATE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/intarfaces/internal-link-ifs";
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
