
package msf.mfc.rest.v1.failure.status;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.mfc.failure.status.MfcFailureStatusNotifyScenario;
import msf.mfc.failure.status.MfcFailureStatusReadListScenario;
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
@Path("/v1")
public class MfcFailureStatusHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcFailureStatusHandler.class);

  /**
   * Failure information list acquisition.
   *
   * @return response data
   */
  @GET
  @Path("/failures/failure_status")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response failureReadList() {
    try {
      logger.methodStart();
      loggingRequestReceived();
      FailureStatusRequest request = new FailureStatusRequest(null, null, null);

      setCommonData(request);

      MfcFailureStatusReadListScenario scenario = new MfcFailureStatusReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Failure information notification.
   *
   * @param requestBody
   *          Request message (Body part)
   *
   * @return response data
   */
  @PUT
  @Path("/MSFcontroller/failure_status")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response notify(String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);
      FailureStatusRequest request = new FailureStatusRequest(requestBody, null, null);

      setCommonData(request);

      MfcFailureStatusNotifyScenario scenario = new MfcFailureStatusNotifyScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
