package msf.fc.rest.v1.clusters.physicalif;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
import msf.fc.node.interfaces.physicalifs.PhysicalInterfaceReadListScenario;
import msf.fc.node.interfaces.physicalifs.PhysicalInterfaceReadScenario;
import msf.fc.node.interfaces.physicalifs.PhysicalInterfaceUpdateScenario;
import msf.fc.node.interfaces.physicalifs.data.PhysicalIfRequest;
import msf.fc.rest.common.AbstractRestHandler;

@Path("/v1/clusters")
public class PhysicalIfHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(PhysicalIfHandler.class);

  @GET
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/physical-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @QueryParam("format") String format) {
    try {
      logger.methodStart(new String[] { "clusterId", "fabricType", "nodeId", "format" },
          new Object[] { clusterId, fabricType, nodeId, format });

      loggingRequestReceived();
      PhysicalIfRequest request = new PhysicalIfRequest();
      request.setClusterId(clusterId);
      request.setFabricType(fabricType);
      request.setNodeId(nodeId);
      request.setFormat(format);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      PhysicalInterfaceReadListScenario scenario = new PhysicalInterfaceReadListScenario(OperationType.NORMAL,
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
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/physical-ifs/{physical_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("physical_if_id") String physicalIfId) {
    try {
      logger.methodStart(new String[] { "clusterId", "fabricType", "nodeId", "physicalIfId" },
          new Object[] { clusterId, fabricType, nodeId, physicalIfId });

      loggingRequestReceived();
      PhysicalIfRequest request = new PhysicalIfRequest();
      request.setClusterId(clusterId);
      request.setFabricType(fabricType);
      request.setNodeId(nodeId);
      request.setIfId(physicalIfId);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      PhysicalInterfaceReadScenario scenario = new PhysicalInterfaceReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  @PUT
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/physical-ifs/{physical_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("physical_if_id") String physicalIfId, String requestBody) {
    try {
      logger.methodStart(new String[] { "clusterId", "fabricType", "nodeId", "physicalIfId", "requestBody" },
          new Object[] { clusterId, fabricType, nodeId, physicalIfId, requestBody });

      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      PhysicalIfRequest request = new PhysicalIfRequest();
      request.setClusterId(clusterId);
      request.setFabricType(fabricType);
      request.setNodeId(nodeId);
      request.setIfId(physicalIfId);
      request.setRequestBody(requestBody);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      PhysicalInterfaceUpdateScenario scenario = new PhysicalInterfaceUpdateScenario(OperationType.NORMAL,
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
