
package msf.fc.services.nodeosupgrade.rest.v1;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.services.nodeosupgrade.scenario.upgrade.FcNodeOsUpgradeCancelScenario;
import msf.fc.services.nodeosupgrade.scenario.upgrade.FcNodeOsUpgradeScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.NodeOsUpgradeRequest;

/**
 * Node OS upgrade function:main process.
 *
 * @author NTT
 */
@Path("/v1/upgrade_operations")
public class FcOsUpgradeHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcOsUpgradeHandler.class);

  /**
   * Node OS upgrade.
   *
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message (Body part)
   * @return response data
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response upgrade(@QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      NodeOsUpgradeRequest request = new NodeOsUpgradeRequest(requestBody, notificationAddress, notificationPort, null);

      setCommonData(request);

      FcNodeOsUpgradeScenario scenario = new FcNodeOsUpgradeScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL,
          null);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Node OS upgrade cancellation.
   *
   * @param operationId
   *          Operation ID (URI parameter)
   * @param requestBody
   *          Request message (Body part)
   * @return response data
   */
  @PUT
  @Path("/{operation_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response upgradeCancel(@PathParam("operation_id") String operationId, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      NodeOsUpgradeRequest request = new NodeOsUpgradeRequest(requestBody, null, null, operationId);

      setCommonData(request);

      FcNodeOsUpgradeCancelScenario scenario = new FcNodeOsUpgradeCancelScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

}
