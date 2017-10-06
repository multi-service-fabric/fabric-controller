package msf.fc.rest.v1.clusters.edgepoint;

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
import msf.fc.node.interfaces.edgepoints.EdgePointCreateScenario;
import msf.fc.node.interfaces.edgepoints.EdgePointDeleteScenario;
import msf.fc.node.interfaces.edgepoints.EdgePointReadListScenario;
import msf.fc.node.interfaces.edgepoints.EdgePointReadScenario;
import msf.fc.node.interfaces.edgepoints.data.EdgePointRequest;
import msf.fc.rest.common.AbstractRestHandler;

@Path("/v1/clusters")
public class EdgePointHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(EdgePointHandler.class);


  @POST
  @Path("/{cluster_id}/points/edge-points")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response create(@PathParam("cluster_id") String clusterId, String requestBody) {
    try {
      logger.methodStart(new String[] { "clusterId" }, new Object[] { clusterId });
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      EdgePointRequest request = new EdgePointRequest();
      request.setClusterId(clusterId);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      request.setRequestBody(requestBody);

      EdgePointCreateScenario scenario = new EdgePointCreateScenario(OperationType.NORMAL,
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
  @Path("/{cluster_id}/points/edge-points")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @QueryParam("format") String format,
      @QueryParam("user-type") String userType) {

    try {
      logger.methodStart(new String[] { "clusterId", "format" }, new Object[] { clusterId, format });
      loggingRequestReceived();

      EdgePointRequest request = new EdgePointRequest();

      request.setClusterId(clusterId);
      request.setFormat(format);
      request.setUserType(userType);
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      EdgePointReadListScenario scenario = new EdgePointReadListScenario(OperationType.NORMAL,
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
  @Path("/{cluster_id}/points/edge-points/{edge_point_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("edge_point_id") String edgePointId,
      @QueryParam("user-type") String userType) {
    try {
      logger.methodStart(new String[] { "clusterId", "edge_point_id", "user-type" },
          new Object[] { clusterId, edgePointId, userType });
      loggingRequestReceived();

      EdgePointRequest request = new EdgePointRequest();

      request.setClusterId(clusterId);
      request.setEdgePointId(edgePointId);
      request.setUserType(userType);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      EdgePointReadScenario scenario = new EdgePointReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  @DELETE
  @Path("/{cluster_id}/points/edge-points/{edge_point_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("cluster_id") String clusterId, @PathParam("edge_point_id") String edgePointId) {

    try {
      logger.methodStart(new String[] { "clusterId", "edge_point_id" }, new Object[] { clusterId, edgePointId });
      loggingRequestReceived();

      EdgePointRequest request = new EdgePointRequest();

      request.setClusterId(clusterId);
      request.setEdgePointId(edgePointId);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      EdgePointDeleteScenario scenario = new EdgePointDeleteScenario(OperationType.NORMAL,
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
