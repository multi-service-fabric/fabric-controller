
package msf.fc.services.nodeosupgrade.rest.v1;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.services.nodeosupgrade.scenario.detour.FcNodeDetourReadScenario;
import msf.fc.services.nodeosupgrade.scenario.detour.FcNodeDetourUpdateScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.services.nodeosupgrade.scenario.detour.data.NodeDetourRequest;

/**
 * Node OS upgrade function:Node detour.
 *
 * @author NTT
 */
@Path("/v1/detour/clusters")
public class FcDetourHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcDetourHandler.class);

  /**
   * Node detour.
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
  @PUT
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      NodeDetourRequest request = new NodeDetourRequest(requestBody, notificationAddress, notificationPort, clusterId,
          fabricType, nodeId);

      setCommonData(request);

      FcNodeDetourUpdateScenario scenario = new FcNodeDetourUpdateScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Node detour information acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      NodeDetourRequest request = new NodeDetourRequest(null, null, null, clusterId, null, null);

      setCommonData(request);

      FcNodeDetourReadScenario scenario = new FcNodeDetourReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

}
