package msf.fc.common.constant;

import org.eclipse.jetty.http.HttpMethod;

public enum FcRequestUri {

  OPERATION_REQUEST(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/FabricController/operations";
    }
  }),

  LEAF_NODE_NOTIFY(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/clusters/" + args[0] + "/nodes/leafs/" + args[1];
    }
  }),

  SPINE_NODE_NOTIFY(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/clusters/" + args[0] + "/nodes/spines/" + args[1];
    }
  });

  private HttpMethod httpMethod;
  private RequestUriInterface requestUri;

  private FcRequestUri(HttpMethod httpMethod, RequestUriInterface requestUri) {
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
