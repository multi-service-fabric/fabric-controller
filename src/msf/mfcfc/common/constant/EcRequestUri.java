
package msf.mfcfc.common.constant;

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

  NODE_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0];
    }
  }),

  NODE_CREATE_DELETE_REQUEST(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/operations";
    }
  }),

  IF_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/interfaces";
    }
  }),

  PHYSICAL_IF_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/interfaces/physical-ifs";
    }
  }),

  PHYSICAL_IF_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/interfaces/physical-ifs/" + args[1];
    }
  }),

  PHYSICAL_IF_UPDATE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/interfaces/physical-ifs/" + args[1];
    }
  }),

  LAG_IF_CREATE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/interfaces/lag-ifs";
    }
  }),

  LAG_IF_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/interfaces/lag-ifs";
    }
  }),

  LAG_IF_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/interfaces/lag-ifs/" + args[1];
    }
  }),

  LAG_IF_DELETE(HttpMethod.DELETE, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/interfaces/lag-ifs/" + args[1];
    }
  }),

  LAG_IF_UPDATE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/interfaces/lag-ifs/" + args[1];
    }
  }),

  BREAKOUT_IF_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/interfaces/breakout-ifs";
    }
  }),

  BREAKOUT_IF_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/interfaces/breakout-ifs/" + args[1];
    }
  }),

  IF_OPERATION_REQUEST(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/intarfaces/operations";
    }
  }),

  SERVICE_RECOVERY_CREATE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/recover_node";
    }
  }),

  VLAN_IF_UPDATE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/interfaces/vlan-ifs/" + args[1];
    }
  }),

  VLAN_IF_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/interfaces/vlan-ifs/" + args[1];
    }
  }),

  VLAN_IF_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0] + "/interfaces/vlan-ifs";
    }
  }),

  TRAFFIC_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/traffic/node_traffic";
    }
  }),

  TRAFFIC_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/traffic/node_traffic/" + args[0];
    }
  }),

  STATUS_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/ec_ctrl/statusget";
    }
  }),

  LOG_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/ec_ctrl/log";
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
