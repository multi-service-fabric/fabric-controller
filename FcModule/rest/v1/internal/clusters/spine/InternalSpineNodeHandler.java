package msf.fc.rest.v1.internal.clusters.spine;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.node.nodes.spines.SpineNodeNotifyScenario;
import msf.fc.node.nodes.spines.data.InternalSpineNodeRequest;
import msf.fc.rest.common.AbstractRestHandler;

@Path("/v1/internal/clusters")
public class InternalSpineNodeHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(InternalSpineNodeHandler.class);

  @PUT
  @Path("/{cluster_id}/nodes/spines/{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response notify(@PathParam("cluster_id") String clusterId, @PathParam("node_id") String nodeId,
      String requestBody) {
    try {
      logger.methodStart();

      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      InternalSpineNodeRequest request = new InternalSpineNodeRequest();
      request.setClusterId(clusterId);
      request.setNodeId(nodeId);
      request.setRequestBody(requestBody);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      SpineNodeNotifyScenario scenario = new SpineNodeNotifyScenario(OperationType.NORMAL,
          SystemInterfaceType.INTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
