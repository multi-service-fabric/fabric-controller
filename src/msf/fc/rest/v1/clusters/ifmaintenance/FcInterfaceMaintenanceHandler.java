
package msf.fc.rest.v1.clusters.ifmaintenance;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.node.interfaces.ifmaintenance.FcInterfaceMaintenanceUpdateScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.interfaces.ifmaintenance.data.InterfaceChangeStateRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Configuration management: Interface maintenance.
 *
 * @author NTT
 */
@Path("/v1/clusters")
public class FcInterfaceMaintenanceHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInterfaceMaintenanceHandler.class);

  /**
   * IF blockade status modification.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fablicType
   *          Fabric type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param ifType
   *          IF type (URI parameter)
   * @param ifId
   *          IF ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message (Body part)
   * @return response data
   */
  @PUT
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/maintenance/{if_type}/{if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fablicType,
      @PathParam("node_id") String nodeId, @PathParam("if_type") String ifType, @PathParam("if_id") String ifId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      InterfaceChangeStateRequest request = new InterfaceChangeStateRequest(requestBody, notificationAddress,
          notificationPort, clusterId, fablicType, nodeId, ifType, ifId);

      setCommonData(request);

      FcInterfaceMaintenanceUpdateScenario scenario = new FcInterfaceMaintenanceUpdateScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);
    } finally {
      logger.methodEnd();
    }
  }
}
