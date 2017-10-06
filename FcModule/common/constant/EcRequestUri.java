package msf.fc.common.constant;

import org.eclipse.jetty.http.HttpMethod;

public enum EcRequestUri {

  OPERATION_REQUEST(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/ElementController/operations";
    }
  }),

  EQUIPMENT_CREATE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/equipment-types";
    }
  }),

  EQUIPMENT_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/equipment-types";
    }
  }),

  EQUIPMENT_READ(HttpMethod.GET, new RequestUriInterface() {

    @Override
    public String makeUri(String... args) {
      return "/v1/internal/equipment-types/" + args[0];
    }
  }),

  EQUIPMENT_DELETE(HttpMethod.DELETE, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/equipment-types/" + args[0];
    }
  }),

  NODE_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes";
    }
  }),

  LEAF_NODE_CREATE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/leafs";
    }
  }),

  LEAF_NODE_UPDATE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/leafs/" + args[0];
    }
  }),

  LEAF_NODE_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/leafs";
    }
  }),

  LEAF_NODE_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/leafs/" + args[0];
    }
  }),

  SPINE_NODE_CREATE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/spines";
    }
  }),

  SPINE_NODE_UPDATE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/spines/" + args[0];
    }
  }),

  SPINE_NODE_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/spines";
    }
  }),

  SPINE_NODE_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/spines/" + args[0];
    }
  }),

  IF_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/" + args[1] + "/interfaces";
    }
  }),

  PHYSICAL_IF_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/" + args[1] + "/interfaces/physical-ifs";
    }
  }),

  PHYSICAL_IF_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/" + args[1] + "/interfaces/physical-ifs/" + args[2];
    }
  }),

  PHYSICAL_IF_UPDATE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/" + args[1] + "/interfaces/physical-ifs/" + args[2];
    }
  }),

  INTERNAL_LINK_IF_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/" + args[1] + "/interfaces/internal-link-ifs";
    }
  }),

  INTERNAL_LINK_IF_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/" + args[1] + "/interfaces/internal-link-ifs/" + args[2];
    }
  }),

  LAG_IF_CREATE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/" + args[1] + "/interfaces/lag-ifs";
    }
  }),

  LAG_IF_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/" + args[1] + "/interfaces/lag-ifs";
    }
  }),

  LAG_IF_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/" + args[1] + "/interfaces/lag-ifs/" + args[2];
    }
  }),

  LAG_IF_DELETE(HttpMethod.DELETE, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/" + args[1] + "/interfaces/lag-ifs/" + args[2];
    }
  }),

  L3CP_UPDATE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/slices/l3vpn/" + args[0] + "/cps/" + args[1];
    }
  }),

  TRAFFIC_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/traffic/node_traffic";
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
