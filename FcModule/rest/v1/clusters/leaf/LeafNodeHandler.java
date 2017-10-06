package msf.fc.rest.v1.clusters.leaf;

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
import msf.fc.node.nodes.leafs.LeafNodeCreateScenario;
import msf.fc.node.nodes.leafs.LeafNodeDeleteScenario;
import msf.fc.node.nodes.leafs.LeafNodeReadListScenario;
import msf.fc.node.nodes.leafs.LeafNodeReadScenario;
import msf.fc.node.nodes.leafs.data.LeafNodeRequest;
import msf.fc.rest.common.AbstractRestHandler;


@Path("/v1/clusters")
public class LeafNodeHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(LeafNodeHandler.class);

  @POST
  @Path("/{cluster_id}/nodes/leafs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(@PathParam("cluster_id") String clusterId, String requestBody) {

    try {
      logger.methodStart(new String[] { "clusterId", "requestBody" }, new Object[] { clusterId, requestBody });

      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      LeafNodeRequest request = new LeafNodeRequest();
      request.setClusterId(clusterId);
      request.setRequestBody(requestBody);
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      LeafNodeCreateScenario scenario = new LeafNodeCreateScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  @GET
  @Path("/{cluster_id}/nodes/leafs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @QueryParam("format") String format) {

    try {
      logger.methodStart(new String[] { "clusterId", "format" }, new Object[] { clusterId, format });

      loggingRequestReceived();

      LeafNodeRequest request = new LeafNodeRequest();
      request.setClusterId(clusterId);
      request.setFormat(format);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      LeafNodeReadListScenario scenario = new LeafNodeReadListScenario(OperationType.NORMAL,
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
  @Path("/{cluster_id}/nodes/leafs/{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("node_id") String nodeId) {

    try {
      logger.methodStart(new String[] { "clusterId", "nodeId" }, new Object[] { clusterId, nodeId });

      loggingRequestReceived();

      LeafNodeRequest request = new LeafNodeRequest();
      request.setClusterId(clusterId);
      request.setNodeId(nodeId);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      LeafNodeReadScenario scenario = new LeafNodeReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }


  @DELETE
  @Path("/{cluster_id}/nodes/leafs/{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("cluster_id") String clusterId, @PathParam("node_id") String nodeId) {

    try {
      logger.methodStart(new String[] { "clusterId", "nodeId" }, new Object[] { clusterId, nodeId });

      loggingRequestReceived();

      LeafNodeRequest request = new LeafNodeRequest();
      request.setClusterId(clusterId);
      request.setNodeId(nodeId);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      LeafNodeDeleteScenario scenario = new LeafNodeDeleteScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
