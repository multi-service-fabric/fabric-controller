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

import msf.fc.node.nodes.spines.FcSpineNodeCreateScenario;
import msf.fc.node.nodes.spines.FcSpineNodeDeleteScenario;
import msf.fc.node.nodes.spines.FcSpineNodeReadListScenario;
import msf.fc.node.nodes.spines.FcSpineNodeReadScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.nodes.spines.data.SpineNodeRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Configuration management Node management (Spine).
 *
 * @author NTT
 */
@Path("/v1/clusters")

public class FcSpineNodeHandler extends AbstractRestHandler {
  
  private static final MsfLogger logger = MsfLogger.getInstance(FcSpineNodeHandler.class);

  /**
   * Spine addition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message body
   * @return response data
   */
  @POST
  @Path("/{cluster_id}/nodes/spines")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(@PathParam("cluster_id") String clusterId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      SpineNodeRequest request = new SpineNodeRequest(requestBody, notificationAddress, notificationPort, clusterId,
          null, null, null);

      setCommonData(request);

      FcSpineNodeCreateScenario scenario = new FcSpineNodeCreateScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  
  /**
   * Spine information list acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param format
   *          Information type to acquire (optional parameter)
   * @param userType
   *          User type (optional parameter)
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes/spines")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @QueryParam("format") String format,
      @QueryParam("user-type") String userType) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      SpineNodeRequest request = new SpineNodeRequest(null, null, null, clusterId, null, format, userType);

      setCommonData(request);

      FcSpineNodeReadListScenario scenario = new FcSpineNodeReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  
  /**
   * Spine information acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param nodeId
   *          Node ID
   * @param userType
   *          User type (optional parameter)
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes/spines/{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("node_id") String nodeId,
      @QueryParam("user-type") String userType) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      SpineNodeRequest request = new SpineNodeRequest(null, null, null, clusterId, nodeId, null, userType);

      setCommonData(request);

      FcSpineNodeReadScenario scenario = new FcSpineNodeReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  
  /**
   * Spine deletion.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @return response data
   */
  @DELETE
  @Path("/{cluster_id}/nodes/spines/{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("cluster_id") String clusterId, @PathParam("node_id") String nodeId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      SpineNodeRequest request = new SpineNodeRequest(null, notificationAddress, notificationPort, clusterId, nodeId,
          null, null);

      setCommonData(request);

      FcSpineNodeDeleteScenario scenario = new FcSpineNodeDeleteScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }
}
