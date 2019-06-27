
package msf.fc.services.priorityroutes.rest.v1;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.services.priorityroutes.scenario.nodes.FcNodeGroupPriorityCreateDeleteScenario;
import msf.fc.services.priorityroutes.scenario.nodes.FcNodeGroupPriorityReadScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.rest.common.PATCH;
import msf.mfcfc.services.priorityroutes.scenario.nodes.data.NodeGroupPriorityRequest;

/**
 * Priority routes control : Priority node group information.
 *
 * @author NTT
 */
@Path("/v1/priority_routes")
public class FcNodeGroupPriorityHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeGroupPriorityHandler.class);

  /**
   * Nodes addition/deletion to the priority node group.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message (Body part)
   * @return response data
   */
  @PATCH
  @Path("/clusters/{cluster_id}/priority_node_group")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createAndDelete(@PathParam("cluster_id") String clusterId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      NodeGroupPriorityRequest request = new NodeGroupPriorityRequest(requestBody, notificationAddress,
          notificationPort, clusterId);

      setCommonData(request);

      FcNodeGroupPriorityCreateDeleteScenario scenario = new FcNodeGroupPriorityCreateDeleteScenario(
          OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Priority node group information acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @return response data
   */
  @GET
  @Path("/clusters/{cluster_id}/priority_node_group")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      NodeGroupPriorityRequest request = new NodeGroupPriorityRequest(null, null, null, clusterId);

      setCommonData(request);

      FcNodeGroupPriorityReadScenario scenario = new FcNodeGroupPriorityReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

}
