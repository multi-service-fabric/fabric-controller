package msf.mfcfc.common.constant;

import static msf.mfcfc.common.util.ParameterCheckUtil.*;

import java.util.regex.Pattern;


public enum MfcFcRequestUri {

  
  
  OPERATION_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/operations";
    }
  }, Pattern.compile("^/v1/operations$")),

  
  OPERATION_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/operations/" + args[0];
    }
  }, Pattern.compile("^/v1/operations/" + URI_PATTERN_MATCHER + "$")),

  
  STATUS_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/MSFcontroller/status";
    }
  }, Pattern.compile("^/v1/MSFcontroller/status$")),

  
  CONTROLLOR_LOG_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/operations/log";
    }
  }, Pattern.compile("^/v1/operations/log$")),

  
  EQUIPMENT_CREATE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/equipment-types";
    }
  }, Pattern.compile("^/v1/equipment-types$")),

  
  EQUIPMENT_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/equipment-types";
    }
  }, Pattern.compile("^/v1/equipment-types$")),

  
  EQUIPMENT_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/equipment-types/" + args[0];
    }
  }, Pattern.compile("^/v1/equipment-types/" + URI_PATTERN_MATCHER + "$")),

  
  EQUIPMENT_DELETE(HttpMethod.DELETE, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/equipment-types/" + args[0];
    }
  }, Pattern.compile("^/v1/equipment-types/" + URI_PATTERN_MATCHER + "$")),

  
  SW_CLUSTER_CREATE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters";
    }
  }, Pattern.compile("^/v1/clusters$")),

  
  SW_CLUSTER_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters";
    }
  }, Pattern.compile("^/v1/clusters$")),

  
  SW_CLUSTER_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "$")),

  
  SW_CLUSTER_DELETE(HttpMethod.DELETE, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "$")),

  
  NODE_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes";
    }
  }, Pattern.compile("^/v1/clusters" + URI_PATTERN_MATCHER + "/nodes$")),

  
  LEAF_NODE_CREATE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/leafs";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/leafs$")),

  
  LEAF_NODE_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/leafs";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/leafs$")),

  
  LEAF_NODE_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/leafs/" + args[1];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/leafs/" + URI_PATTERN_MATCHER + "$")),

  
  LEAF_NODE_DELETE(HttpMethod.DELETE, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/leafs/" + args[1];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/leafs/" + URI_PATTERN_MATCHER + "$")),

  
  LEAF_NODE_UPDATE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/leafs/" + args[1];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/leafs/" + URI_PATTERN_MATCHER + "$")),

  
  SPINE_NODE_CREATE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/spines";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/spines$")),

  
  SPINE_NODE_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/spines";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/spines$")),

  
  SPINE_NODE_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/spines/" + args[1];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/spines/" + URI_PATTERN_MATCHER + "$")),

  
  SPINE_NODE_DELETE(HttpMethod.DELETE, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/spines/" + args[1];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/spines/" + URI_PATTERN_MATCHER + "$")),

  
  RR_NODE_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/rrs";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/rrs$")),

  
  RR_NODE_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/rrs/" + args[1];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/rrs/" + URI_PATTERN_MATCHER + "$")),

  
  IF_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/" + args[1] + "/" + args[2] + "/interfaces";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/"
      + URI_PATTERN_MATCHER + "/interfaces$")),

  
  PHYSICAL_IF_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/" + args[1] + "/" + args[2] + "/interfaces/physical-ifs";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/"
      + URI_PATTERN_MATCHER + "/interfaces/physical-ifs$")),

  
  PHYSICAL_IF_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/" + args[1] + "/" + args[2] + "/interfaces/physical-ifs/" + args[3];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/"
      + URI_PATTERN_MATCHER + "/interfaces/physical-ifs/" + URI_PATTERN_MATCHER + "$")),

  
  PHYSICAL_IF_UPDATE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/" + args[1] + "/" + args[2] + "/interfaces/physical-ifs/" + args[3];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/"
      + URI_PATTERN_MATCHER + "/interfaces/physical-ifs/" + URI_PATTERN_MATCHER + "$")),

  
  BREAKOUT_IF_CREATE_DELETE(HttpMethod.PATCH, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/" + args[1] + "/" + args[2] + "/interfaces/breakout-ifs";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/"
      + URI_PATTERN_MATCHER + "/interfaces/breakout-ifs$")),

  
  BREAKOUT_IF_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/" + args[1] + "/" + args[2] + "/interfaces/breakout-ifs";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/"
      + URI_PATTERN_MATCHER + "/interfaces/breakout-ifs$")),

  
  BREAKOUT_IF_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/" + args[1] + "/" + args[2] + "/interfaces/breakout-ifs/" + args[3];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/"
      + URI_PATTERN_MATCHER + "/interfaces/breakout-ifs/" + URI_PATTERN_MATCHER + "$")),

  
  INTERNAL_LINK_IF_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/" + args[1] + "/" + args[2] + "/interfaces/internal-link-ifs";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/"
      + URI_PATTERN_MATCHER + "/interfaces/internal-link-ifs$")),

  
  INTERNAL_LINK_IF_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/" + args[1] + "/" + args[2] + "/interfaces/internal-link-ifs/"
          + args[3];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/"
      + URI_PATTERN_MATCHER + "/interfaces/internal-link-ifs/" + URI_PATTERN_MATCHER + "$")),

  
  LAG_IF_CREATE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/" + args[1] + "/" + args[2] + "/interfaces/lag-ifs";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/"
      + URI_PATTERN_MATCHER + "/interfaces/lag-ifs$")),

  
  LAG_IF_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/" + args[1] + "/" + args[2] + "/interfaces/lag-ifs";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/"
      + URI_PATTERN_MATCHER + "/interfaces/lag-ifs$")),

  
  LAG_IF_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/" + args[1] + "/" + args[2] + "/interfaces/lag-ifs/" + args[3];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/"
      + URI_PATTERN_MATCHER + "/interfaces/lag-ifs/" + URI_PATTERN_MATCHER + "$")),

  
  LAG_IF_UPDATE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/" + args[1] + "/" + args[2] + "/interfaces/lag-ifs/" + args[3];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/"
      + URI_PATTERN_MATCHER + "/interfaces/lag-ifs/" + URI_PATTERN_MATCHER + "$")),

  
  LAG_IF_DELETE(HttpMethod.DELETE, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/nodes/" + args[1] + "/" + args[2] + "/interfaces/lag-ifs/" + args[3];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/"
      + URI_PATTERN_MATCHER + "/interfaces/lag-ifs/" + URI_PATTERN_MATCHER + "$")),

  
  CLUSTER_LINK_IF_CREATE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/interfaces/cluster-link-ifs";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/interfaces/cluster-link-ifs$")),

  
  CLUSTER_LINK_IF_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/interfaces/cluster-link-ifs";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/interfaces/cluster-link-ifs$")),

  
  CLUSTER_LINK_IF_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/interfaces/cluster-link-ifs/" + args[1];
    }
  }, Pattern
      .compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/interfaces/cluster-link-ifs/" + URI_PATTERN_MATCHER + "$")),

  
  CLUSTER_LINK_IF_DELETE(HttpMethod.DELETE, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/interfaces/cluster-link-ifs/" + args[1];
    }
  }, Pattern
      .compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/interfaces/cluster-link-ifs/" + URI_PATTERN_MATCHER + "$")),

  
  EDGE_POINT_CREATE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/points/edge-points";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/points/edge-points$")),

  
  EDGE_POINT_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/points/edge-points";
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/points/edge-points$")),

  
  EDGE_POINT_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/points/edge-points/" + args[1];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/points/edge-points/" + URI_PATTERN_MATCHER + "$")),

  
  EDGE_POINT_DELETE(HttpMethod.DELETE, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/clusters/" + args[0] + "/points/edge-points/" + args[1];
    }
  }, Pattern.compile("^/v1/clusters/" + URI_PATTERN_MATCHER + "/points/edge-points/" + URI_PATTERN_MATCHER + "$")),

  
  SLICE_CREATE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/slices/" + args[0];
    }
  }, Pattern.compile("^/v1/slices/" + URI_PATTERN_MATCHER + "$")),

  
  SLICE_UPDATE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/slices/" + args[0] + "/" + args[1];
    }
  }, Pattern.compile("^/v1/slices/" + URI_PATTERN_MATCHER + "/" + URI_PATTERN_MATCHER + "$")),

  
  SLICE_DELETE(HttpMethod.DELETE, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/slices/" + args[0] + "/" + args[1];
    }
  }, Pattern.compile("^/v1/slices/" + URI_PATTERN_MATCHER + "/" + URI_PATTERN_MATCHER + "$")),

  
  SLICE_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/slices/" + args[0] + "/" + args[1];
    }
  }, Pattern.compile("^/v1/slices/" + URI_PATTERN_MATCHER + "/" + URI_PATTERN_MATCHER + "$")),

  
  SLICE_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/slices/" + args[0];
    }
  }, Pattern.compile("^/v1/slices/" + URI_PATTERN_MATCHER + "$")),

  
  CP_CREATE_DELETE(HttpMethod.PATCH, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/slices/" + args[0] + "/" + args[1] + "/cps";
    }
  }, Pattern.compile("^/v1/slices/" + URI_PATTERN_MATCHER + "/" + URI_PATTERN_MATCHER + "/cps")),

  
  CP_CREATE(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/slices/" + args[0] + "/" + args[1] + "/cps";
    }
  }, Pattern.compile("^/v1/slices/" + URI_PATTERN_MATCHER + "/" + URI_PATTERN_MATCHER + "/cps$")),

  
  CP_UPDATE(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/slices/" + args[0] + "/" + args[1] + "/cps/" + args[2];
    }
  }, Pattern
      .compile("^/v1/slices/" + URI_PATTERN_MATCHER + "/" + URI_PATTERN_MATCHER + "/cps/" + URI_PATTERN_MATCHER + "$")),

  
  CP_DELETE(HttpMethod.DELETE, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/slices/" + args[0] + "/" + args[1] + "/cps/" + args[2];
    }
  }, Pattern
      .compile("^/v1/slices/" + URI_PATTERN_MATCHER + "/" + URI_PATTERN_MATCHER + "/cps/" + URI_PATTERN_MATCHER + "$")),

  
  CP_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/slices/" + args[0] + "/" + args[1] + "/cps/" + args[2];
    }
  }, Pattern
      .compile("^/v1/slices/" + URI_PATTERN_MATCHER + "/" + URI_PATTERN_MATCHER + "/cps/" + URI_PATTERN_MATCHER + "$")),

  
  CP_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/slices/" + args[0] + "/" + args[1] + "/cps";
    }
  }, Pattern.compile("^/v1/slices/" + URI_PATTERN_MATCHER + "/" + URI_PATTERN_MATCHER + "/cps$")),

  
  STATIC_ROUTE_CREATE_DELETE(HttpMethod.PATCH, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/slices/" + args[0] + "/" + args[1] + "/cps/" + args[2];
    }
  }, Pattern
      .compile("^/v1/slices/" + URI_PATTERN_MATCHER + "/" + URI_PATTERN_MATCHER + "/cps/" + URI_PATTERN_MATCHER + "$")),

  
  IF_TRAFFIC_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/traffic/clusters/" + args[0] + "/nodes/" + args[1] + "/interfaces";
    }
  }, Pattern
      .compile("^/v1/traffic/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/interfaces$")),

  
  IF_TRAFFIC_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/traffic/clusters/" + args[0] + "/nodes/" + args[1] + "/interfaces/" + args[2] + "/" + args[3];
    }
  }, Pattern.compile("^/v1/traffic/clusters/" + URI_PATTERN_MATCHER + "/nodes/" + URI_PATTERN_MATCHER + "/interfaces/"
      + URI_PATTERN_MATCHER + "/" + URI_PATTERN_MATCHER + "$")),

  
  CP_TRAFFIC_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/traffic/slices/" + args[0] + "/" + args[1] + "/cps";
    }
  }, Pattern.compile("^/v1/traffic/slices/" + URI_PATTERN_MATCHER + "/" + URI_PATTERN_MATCHER + "/cps$")),

  
  CP_TRAFFIC_READ(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/traffic/slices/" + args[0] + "/" + args[1] + "/cps/" + args[2];
    }
  }, Pattern.compile(
      "^/v1/traffic/slices/" + URI_PATTERN_MATCHER + "/" + URI_PATTERN_MATCHER + "/cps/" + URI_PATTERN_MATCHER + "$")),

  
  FAILURE_READ_LIST(HttpMethod.GET, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/failures/failure_status";
    }
  }, Pattern.compile("^/v1/failures/failure_status$")),

  
  
  OPERATION_RESULT_NOTIFY(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/operations/" + args[0];
    }
  }, Pattern.compile("^/v1/operations/" + URI_PATTERN_MATCHER + "$")),

  
  STATUS_NOTIFY(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/MSFcontroller/status";
    }
  }, Pattern.compile("^/v1/MSFcontroller/status$")),

  
  TRAFFIC_NOTIFY(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/MSFcontroller/traffic";
    }
  }, Pattern.compile("^/v1/MSFcontroller/traffic$")),

  
  FAILURE_NOTIFY(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/MSFcontroller/failure_status";
    }
  }, Pattern.compile("^/v1/MSFcontroller/failure_status$")),

  
  
  INTERNAL_OPERATION_REQUEST(HttpMethod.POST, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/FabricController/operations";
    }
  }, Pattern.compile("^/v1/internal/FabricController/operations$")),

  
  INTERNAL_STATUS_NOTIFY(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/controller/ec_em/status";
    }
  }, Pattern.compile("^/v1/internal/controller/ec_em/status$")),

  
  INTERNAL_NODE_NOTIFY(HttpMethod.PUT, new RequestUriInterface() {
    @Override
    public String makeUri(String... args) {
      return "/v1/internal/nodes/" + args[0];
    }
  }, Pattern.compile("^/v1/internal/nodes/" + URI_PATTERN_MATCHER + "$"));


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
