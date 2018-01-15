
package msf.fc.rest.v1.failure.status;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.failure.status.FcFailureStatusReadListScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.failure.status.data.FailureStatusRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * REST request handler class of failure management function.
 *
 * @author NTT
 *
 */
@Path("/v1/failures")
public class FcFailureStatusHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcFailureStatusHandler.class);

  /**
   * Failure information list acquisition.
   *
   * @return response data
   */
  @GET
  @Path("/failure_status")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response failureReadList() {
    try {
      logger.methodStart();
      loggingRequestReceived();
      FailureStatusRequest request = new FailureStatusRequest(null, null, null);

      setCommonData(request);

      FcFailureStatusReadListScenario scenario = new FcFailureStatusReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
