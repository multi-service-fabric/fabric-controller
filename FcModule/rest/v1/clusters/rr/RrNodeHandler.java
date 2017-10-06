package msf.fc.rest.v1.clusters.rr;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import msf.fc.node.nodes.rrs.RrNodeReadListScenario;
import msf.fc.node.nodes.rrs.RrNodeReadScenario;
import msf.fc.node.nodes.rrs.data.RrNodeRequest;
import msf.fc.rest.common.AbstractRestHandler;

@Path("/v1/clusters")
public class RrNodeHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(RrNodeHandler.class);

  @GET
  @Path("/{cluster_id}/nodes/rrs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @QueryParam("format") String format) {
    try {
      logger.methodStart(new String[] { "clusterId", "format" }, new Object[] { clusterId, format });

      loggingRequestReceived();
      RrNodeRequest request = new RrNodeRequest();
      request.setClusterId(clusterId);
      request.setFormat(format);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      RrNodeReadListScenario scenario = new RrNodeReadListScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  @GET
  @Path("/{cluster_id}/nodes/rrs/{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("node_id") String nodeId) {
    try {

      logger.methodStart(new String[] { "clusterId", "nodeId" }, new Object[] { clusterId, nodeId });

      loggingRequestReceived();

      RrNodeRequest request = new RrNodeRequest();
      request.setClusterId(clusterId);
      request.setNodeId(nodeId);
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      RrNodeReadScenario scenario = new RrNodeReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

}
