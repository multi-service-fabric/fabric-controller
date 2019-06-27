
package msf.fc.services.nodeosupgrade.rest.v1.internal;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.services.nodeosupgrade.scenario.upgrade.FcInternalNodeOsUpgradeScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.services.nodeosupgrade.scenario.upgrade.data.InternalNodeOsUpgradeRequest;

/**
 * Node OS upgrade function:main process(Internal interface).
 *
 * @author NTT
 */
@Path("/v1/internal/upgrade_operations")
public class FcInternalOsUpgradeHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalOsUpgradeHandler.class);

  /**
   * Node OS upgrade completion notification.
   *
   * @param requestBody
   *          Request message (Body part)
   * @return response data
   */
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response notifyUpgrade(String requestBody) {
    try {
      logger.methodStart();

      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      InternalNodeOsUpgradeRequest request = new InternalNodeOsUpgradeRequest(requestBody, null, null);

      setCommonData(request);

      FcInternalNodeOsUpgradeScenario scenario = new FcInternalNodeOsUpgradeScenario(OperationType.NORMAL,
          SystemInterfaceType.INTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

}
