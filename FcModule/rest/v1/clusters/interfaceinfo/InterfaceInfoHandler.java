package msf.fc.rest.v1.clusters.interfaceinfo;

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
import msf.fc.node.interfaces.InterfaceReadListScenario;
import msf.fc.node.interfaces.data.InterfaceRequest;
import msf.fc.rest.common.AbstractRestHandler;


@Path("/v1/clusters")
public class InterfaceInfoHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(InterfaceInfoHandler.class);

  @GET
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @QueryParam("format") String format) {

    try {
      logger.methodStart(new String[] { "clusterId", "fabricType", "nodeId", "format" },
          new Object[] { clusterId, fabricType, nodeId, format });

      loggingRequestReceived();

      InterfaceRequest request = new InterfaceRequest();
      request.setClusterId(clusterId);
      request.setFabricType(fabricType);
      request.setNodeId(nodeId);
      request.setFormat(format);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      InterfaceReadListScenario scenario = new InterfaceReadListScenario(OperationType.NORMAL,
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
