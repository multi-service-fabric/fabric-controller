
package msf.fc.rest.v1.clusters.leaf;

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

import msf.fc.node.nodes.leafs.FcLeafNodeCreateScenario;
import msf.fc.node.nodes.leafs.FcLeafNodeDeleteScenario;
import msf.fc.node.nodes.leafs.FcLeafNodeReadListScenario;
import msf.fc.node.nodes.leafs.FcLeafNodeReadScenario;
import msf.fc.node.nodes.leafs.FcLeafNodeUpdateScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Configuration management Node management (Leaf).
 *
 * @author NTT
 */
@Path("/v1/clusters")
public class FcLeafNodeHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLeafNodeHandler.class);

  /**
   * Leaf addition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification node
   * @param requestBody
   *          Request message body
   * @return response data
   */
  @POST
  @Path("/{cluster_id}/nodes/leafs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(@PathParam("cluster_id") String clusterId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      LeafNodeRequest request = new LeafNodeRequest(requestBody, notificationAddress, notificationPort, clusterId, null,
          null, null);

      setCommonData(request);

      FcLeafNodeCreateScenario scenario = new FcLeafNodeCreateScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Leaf information list acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param format
   *          Information type to acquire (optional parameter)
   * @param userType
   *          User type
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes/leafs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @QueryParam("format") String format,
      @QueryParam("user-type") String userType) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      LeafNodeRequest request = new LeafNodeRequest(null, null, null, clusterId, null, format, userType);

      setCommonData(request);

      FcLeafNodeReadListScenario scenario = new FcLeafNodeReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Leaf information acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param nodeId
   *          Node ID
   * @param userType
   *          User type
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes/leafs/{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("node_id") String nodeId,
      @QueryParam("user-type") String userType) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      LeafNodeRequest request = new LeafNodeRequest(null, null, null, clusterId, nodeId, null, userType);

      setCommonData(request);

      FcLeafNodeReadScenario scenario = new FcLeafNodeReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Leaf deletion.
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
  @Path("/{cluster_id}/nodes/leafs/{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("cluster_id") String clusterId, @PathParam("node_id") String nodeId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      LeafNodeRequest request = new LeafNodeRequest(null, notificationAddress, notificationPort, clusterId, nodeId,
          null, null);

      setCommonData(request);

      FcLeafNodeDeleteScenario scenario = new FcLeafNodeDeleteScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Leaf modification.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
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
  @PUT
  @Path("/{cluster_id}/nodes/leafs/{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("cluster_id") String clusterId, @PathParam("node_id") String nodeId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      LeafNodeRequest request = new LeafNodeRequest(requestBody, notificationAddress, notificationPort, clusterId,
          nodeId, null, null);

      setCommonData(request);

      FcLeafNodeUpdateScenario scenario = new FcLeafNodeUpdateScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
