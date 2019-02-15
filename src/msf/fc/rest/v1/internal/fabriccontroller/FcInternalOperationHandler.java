
package msf.fc.rest.v1.internal.fabriccontroller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.failure.status.FcFailureStatusNotifyScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.failure.logicalif.data.LogicalIfStatusRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Common Handler class to execute a request for the internal IF processing.
 *
 * @author NTT
 */
@Path("/v1/internal/FabricController/operations")
public class FcInternalOperationHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalOperationHandler.class);

  /**
   * Handler class to analyze action parameters in request body, and execute an
   * appropriate function according to the analysis result.
   *
   * @param requestBody
   *          Request message body
   * @return response data
   * @throws MsfException
   *           When an error occurs during request execution
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(String requestBody) {

    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);
      LogicalIfStatusRequest request = new LogicalIfStatusRequest(requestBody, null, null);

      setCommonData(request);

      FcFailureStatusNotifyScenario scenario = new FcFailureStatusNotifyScenario(OperationType.NORMAL,
          SystemInterfaceType.INTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

}
