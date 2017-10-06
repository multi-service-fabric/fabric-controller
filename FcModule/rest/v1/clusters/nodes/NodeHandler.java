package msf.fc.rest.v1.clusters.nodes;

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
import msf.fc.node.nodes.NodeReadListScenario;
import msf.fc.node.nodes.data.NodeRequest;
import msf.fc.rest.common.AbstractRestHandler;

@Path("/v1/clusters")
public class NodeHandler extends AbstractRestHandler {
  private static final MsfLogger logger = MsfLogger.getInstance(NodeHandler.class);

  @GET
  @Path("/{cluster_id}/nodes")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @QueryParam("format") String format) {
    try {
      logger.methodStart(new String[] { "clusterId", "format" }, new Object[] { clusterId, format });

      loggingRequestReceived();

      logger.debug("cluster_id = " + clusterId);
      logger.debug("format = " + format);

      NodeRequest request = new NodeRequest();
      request.setClusterId(clusterId);
      request.setFormat(format);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      NodeReadListScenario scenario = new NodeReadListScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

}
