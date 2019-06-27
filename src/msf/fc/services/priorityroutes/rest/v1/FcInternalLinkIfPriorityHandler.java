
package msf.fc.services.priorityroutes.rest.v1;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.services.priorityroutes.scenario.internalifs.FcInternalLinkIfPriorityReadListScenario;
import msf.fc.services.priorityroutes.scenario.internalifs.FcInternalLinkIfPriorityReadScenario;
import msf.fc.services.priorityroutes.scenario.internalifs.FcInternalLinkIfPriorityUpdateScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.services.priorityroutes.scenario.internalifs.data.InternalLinkIfPriorityRequest;

/**
 * Priority routes control : Internal-link interface priority information.
 *
 * @author NTT
 */
@Path("/v1/priority_routes")
public class FcInternalLinkIfPriorityHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalLinkIfPriorityHandler.class);

  /**
   * Internal-link interface priority modification.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Fabric type (URI parameter)
   * @param nodeId
   *          Node ID
   * @param internalLinkIfId
   *          Internal link IF ID
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message (Body part)
   * @return response data
   */
  @PUT
  @Path("/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/internal-link-ifs/{internal_link_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("internal_link_if_id") String internalLinkIfId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      InternalLinkIfPriorityRequest request = new InternalLinkIfPriorityRequest(requestBody, notificationAddress,
          notificationPort, clusterId, fabricType, nodeId, internalLinkIfId);

      setCommonData(request);

      FcInternalLinkIfPriorityUpdateScenario scenario = new FcInternalLinkIfPriorityUpdateScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Internal-link interface priority information list acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @return response data
   */
  @GET
  @Path("/clusters/{cluster_id}/nodes")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      InternalLinkIfPriorityRequest request = new InternalLinkIfPriorityRequest(null, null, null, clusterId, null, null,
          null);

      setCommonData(request);

      FcInternalLinkIfPriorityReadListScenario scenario = new FcInternalLinkIfPriorityReadListScenario(
          OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Internal-link interface priority information acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Fabric type (URI parameter)
   * @param nodeId
   *          Node ID
   * @param internalLinkIfId
   *          Internal link IF ID
   * @return response data
   */
  @GET
  @Path("/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/internal-link-ifs/{internal_link_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("internal_link_if_id") String internalLinkIfId) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      InternalLinkIfPriorityRequest request = new InternalLinkIfPriorityRequest(null, null, null, clusterId, fabricType,
          nodeId, internalLinkIfId);

      setCommonData(request);

      FcInternalLinkIfPriorityReadScenario scenario = new FcInternalLinkIfPriorityReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

}
