package msf.fc.rest.v1.clusters.spine;

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
import msf.fc.node.nodes.spines.SpineNodeCreateScenario;
import msf.fc.node.nodes.spines.SpineNodeDeleteScenario;
import msf.fc.node.nodes.spines.SpineNodeReadListScenario;
import msf.fc.node.nodes.spines.SpineNodeReadScenario;
import msf.fc.node.nodes.spines.data.SpineNodeRequest;
import msf.fc.rest.common.AbstractRestHandler;

@Path("/v1/clusters")

public class SpineNodeHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(SpineNodeHandler.class);

  @POST
  @Path("/{cluster_id}/nodes/spines")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(@PathParam("cluster_id") String clusterId, String requestBody) {

    try {
      logger.methodStart(new String[] { "clusterId", "requestBody" }, new Object[] { clusterId, requestBody });

      loggingRequestReceived();

      logger.debug("cluster_id = " + clusterId);
      loggingRequestJsonBody(requestBody);

      SpineNodeRequest request = new SpineNodeRequest();
      request.setClusterId(clusterId);
      request.setRequestBody(requestBody);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      SpineNodeCreateScenario scenario = new SpineNodeCreateScenario(OperationType.NORMAL,
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
  @Path("/{cluster_id}/nodes/spines")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @QueryParam("format") String format) {

    try {
      logger.methodStart(new String[] { "clusterId", "format" }, new Object[] { clusterId, format });

      loggingRequestReceived();

      SpineNodeRequest request = new SpineNodeRequest();
      request.setClusterId(clusterId);
      request.setFormat(format);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      SpineNodeReadListScenario scenario = new SpineNodeReadListScenario(OperationType.NORMAL,
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
  @Path("/{cluster_id}/nodes/spines/{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("node_id") String nodeId) {

    try {
      logger.methodStart(new String[] { "clusterId", "nodeId" }, new Object[] { clusterId, nodeId });

      loggingRequestReceived();

      SpineNodeRequest request = new SpineNodeRequest();
      request.setClusterId(clusterId);
      request.setNodeId(nodeId);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      SpineNodeReadScenario scenario = new SpineNodeReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }


  @DELETE
  @Path("/{cluster_id}/nodes/spines/{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("cluster_id") String clusterId, @PathParam("node_id") String nodeId) {

    try {
      logger.methodStart(new String[] { "clusterId", "nodeId" }, new Object[] { clusterId, nodeId });

      loggingRequestReceived();

      logger.debug("cluster_id = " + clusterId);
      logger.debug("node_id = " + nodeId);

      SpineNodeRequest request = new SpineNodeRequest();
      request.setClusterId(clusterId);
      request.setNodeId(nodeId);
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      SpineNodeDeleteScenario scenario = new SpineNodeDeleteScenario(OperationType.NORMAL,
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
