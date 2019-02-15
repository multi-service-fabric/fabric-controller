
package msf.fc.rest.v1.clusters.lagif;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.node.interfaces.lagifs.FcLagInterfaceCreateScenario;
import msf.fc.node.interfaces.lagifs.FcLagInterfaceDeleteScenario;
import msf.fc.node.interfaces.lagifs.FcLagInterfaceReadListScenario;
import msf.fc.node.interfaces.lagifs.FcLagInterfaceReadScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Configuration management Interface management (LagIF).
 *
 * @author NTT
 */
@Path("/v1/clusters")
public class FcLagIfHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLagIfHandler.class);

  /**
   * LagIF addition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Fabric type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message (Body part)
   * @return response data
   */
  @POST
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      LagIfRequest request = new LagIfRequest(requestBody, notificationAddress, notificationPort, clusterId, fabricType,
          nodeId, null, null);

      setCommonData(request);

      FcLagInterfaceCreateScenario scenario = new FcLagInterfaceCreateScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * LagIF information list acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Fabric type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param format
   *          Information type to acquire (optional parameter)
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @QueryParam("format") String format) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      LagIfRequest request = new LagIfRequest(null, null, null, clusterId, fabricType, nodeId, null, format);

      setCommonData(request);

      FcLagInterfaceReadListScenario scenario = new FcLagInterfaceReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * LagIF information acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Fabric type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param lagIfId
   *          LagIF ID (URI parameter)
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs/{lag_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("lag_if_id") String lagIfId) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      LagIfRequest request = new LagIfRequest(null, null, null, clusterId, fabricType, nodeId, lagIfId, null);

      setCommonData(request);

      FcLagInterfaceReadScenario scenario = new FcLagInterfaceReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * LagIF information modification.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Fabric type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param lagIfId
   *          LagIF ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message (Body part)
   * @return response data
   */
  @PUT
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs/{lag_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("lag_if_id") String lagIfId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    return Response.status(Response.Status.NOT_FOUND).build();
  }

  /**
   * LagIF deletion.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Fabric type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param lagIfId
   *          LagIF ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @return response data
   */
  @DELETE
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs/{lag_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("lag_if_id") String lagIfId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      LagIfRequest request = new LagIfRequest(null, notificationAddress, notificationPort, clusterId, fabricType,
          nodeId, lagIfId, null);

      setCommonData(request);

      FcLagInterfaceDeleteScenario scenario = new FcLagInterfaceDeleteScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
