package msf.fc.rest.v1.clusters.internalif;

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
import msf.fc.node.interfaces.internalifs.InternalInterfaceReadListScenario;
import msf.fc.node.interfaces.internalifs.InternalInterfaceReadScenario;
import msf.fc.node.interfaces.internalifs.data.InternalIfRequest;
import msf.fc.rest.common.AbstractRestHandler;

@Path("/v1/clusters")
public class InternalIfHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(InternalIfHandler.class);

  @GET
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/internal-link-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @QueryParam("format") String format) {
    try {
      logger.methodStart(new String[] { "clusterId", "fabricType", "nodeId", "format" },
          new Object[] { clusterId, fabricType, nodeId, format });

      loggingRequestReceived();

      InternalIfRequest request = new InternalIfRequest();
      request.setClusterId(clusterId);
      request.setFabricType(fabricType);
      request.setNodeId(nodeId);
      request.setFormat(format);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      InternalInterfaceReadListScenario scenario = new InternalInterfaceReadListScenario(OperationType.NORMAL,
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
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/internal-link-ifs/{internal_link_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("internal_link_if_id") String internalLinkIfId) {

    try {
      logger.methodStart(new String[] { "clusterId", "fabricType", "nodeId", "internalLinkIfId" },
          new Object[] { clusterId, fabricType, nodeId, internalLinkIfId });

      loggingRequestReceived();

      InternalIfRequest request = new InternalIfRequest();
      request.setClusterId(clusterId);
      request.setFabricType(fabricType);
      request.setNodeId(nodeId);
      request.setInternalIfId(internalLinkIfId);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      InternalInterfaceReadScenario scenario = new InternalInterfaceReadScenario(OperationType.NORMAL,
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
