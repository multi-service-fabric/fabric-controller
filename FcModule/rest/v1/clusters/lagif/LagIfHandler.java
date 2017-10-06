package msf.fc.rest.v1.clusters.lagif;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.node.interfaces.lagifs.LagInterfaceCreateScenario;
import msf.fc.node.interfaces.lagifs.LagInterfaceDeleteScenario;
import msf.fc.node.interfaces.lagifs.LagInterfaceReadListScenario;
import msf.fc.node.interfaces.lagifs.LagInterfaceReadScenario;
import msf.fc.node.interfaces.lagifs.data.LagIfRequest;
import msf.fc.rest.common.AbstractRestHandler;

@Path("/v1/clusters")
public class LagIfHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(LagIfHandler.class);

  @POST
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, String requestBody) {

    try {
      logger.methodStart(new String[] { "clusterId", "fabricType", "nodeId", "requestBody" },
          new Object[] { clusterId, fabricType, nodeId, requestBody });

      loggingRequestReceived();

      LagIfRequest request = new LagIfRequest();
      request.setClusterId(clusterId);
      request.setFabricType(fabricType);
      request.setNodeId(nodeId);
      request.setRequestBody(requestBody);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      LagInterfaceCreateScenario scenario = new LagInterfaceCreateScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  @GET
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @QueryParam("format") String format) {

    try {
      logger.methodStart(new String[] { "clusterId", "fabricType", "nodeId", "format" },
          new Object[] { clusterId, fabricType, nodeId, format });

      loggingRequestReceived();

      LagIfRequest request = new LagIfRequest();
      request.setClusterId(clusterId);
      request.setFabricType(fabricType);
      request.setNodeId(nodeId);
      request.setFormat(format);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      LagInterfaceReadListScenario scenario = new LagInterfaceReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  @GET
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs/{lag_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("lag_if_id") String lagIfId) {

    try {
      logger.methodStart(new String[] { "clusterId", "fabricType", "nodeId", "lagIfId" },
          new Object[] { clusterId, fabricType, nodeId, lagIfId });

      loggingRequestReceived();

      LagIfRequest request = new LagIfRequest();
      request.setClusterId(clusterId);
      request.setFabricType(fabricType);
      request.setNodeId(nodeId);
      request.setLagIfId(lagIfId);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      LagInterfaceReadScenario scenario = new LagInterfaceReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  @DELETE
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs/{lag_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("lag_if_id") String lagIfId) {

    try {
      logger.methodStart(new String[] { "clusterId", "fabricType", "nodeId", "lagIfId" },
          new Object[] { clusterId, fabricType, nodeId, lagIfId });

      loggingRequestReceived();

      logger.debug("cluster_id = " + clusterId);
      logger.debug("fablic_type = " + fabricType);
      logger.debug("node_id = " + nodeId);
      logger.debug("lag_if_id = " + lagIfId);

      LagIfRequest request = new LagIfRequest();
      request.setClusterId(clusterId);
      request.setFabricType(fabricType);
      request.setNodeId(nodeId);
      request.setLagIfId(lagIfId);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      LagInterfaceDeleteScenario scenario = new LagInterfaceDeleteScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
