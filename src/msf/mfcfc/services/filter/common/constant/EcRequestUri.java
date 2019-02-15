
package msf.mfcfc.services.filter.common.constant;

import msf.mfcfc.common.constant.HttpMethod;

public enum EcRequestUri {

  FILTER_CREATE_DELETE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/filter/nodes/" + args[0] + "/interfaces/" + args[1] + "/" + args[2];
    }
  }),

  VLAN_IF_FILTER_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/filter/nodes/" + args[0] + "/interfaces/vlan-ifs";
    }
  }),

  VLAN_IF_FILTER_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/filter/nodes/" + args[0] + "/interfaces/vlan-ifs/" + args[1];
    }
  }),

  PHYSICAL_IF_FILTER_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/filter/nodes/" + args[0] + "/interfaces/physical-ifs";
    }
  }),

  PHYSICAL_IF_FILTER_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/filter/nodes/" + args[0] + "/interfaces/physical-ifs/" + args[1];
    }
  }),

  LAG_IF_FILTER_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/filter/nodes/" + args[0] + "/interfaces/lag-ifs";
    }
  }),

  LAG_IF_FILTER_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/filter/nodes/" + args[0] + "/interfaces/lag-ifs/" + args[1];
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
