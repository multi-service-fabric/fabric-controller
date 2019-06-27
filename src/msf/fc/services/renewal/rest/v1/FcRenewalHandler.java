
package msf.fc.services.renewal.rest.v1;

import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.services.renewal.scenario.renewals.FcRenewalGetScenario;
import msf.fc.services.renewal.scenario.renewals.FcRenewalSetScenario;
import msf.fc.services.renewal.scenario.renewals.FcSwitchoverScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.services.renewal.scenario.renewals.data.RenewalRequest;

/**
 * controller file upgrade: state management(Controller renewal).
 *
 * @author NTT
 */
@Path("/v1/manage")
public class FcRenewalHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcRenewalHandler.class);

  /**
   * Controller renewal.
   *
   * @param controller
   *          Target controller
   * @param cluster
   *          Target SW cluster
   * @param requestBody
   *          Request message (Body part)
   * @return Response data
   */
  @PUT
  @Path("/renewal")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response setRenewal(@Encoded @QueryParam("controller") String controller,
      @Encoded @QueryParam("cluster") String cluster, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      RenewalRequest request = new RenewalRequest(requestBody, null, null, controller, cluster);

      setCommonData(request);

      FcRenewalSetScenario scenario = new FcRenewalSetScenario(OperationType.PRIORITY, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the controller renewal information.
   *
   * @return Response data
   */
  @GET
  @Path("/renewal")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getRenewal() {
    try {
      logger.methodStart();
      loggingRequestReceived();

      RenewalRequest request = new RenewalRequest(null, null, null, null, null);

      setCommonData(request);

      FcRenewalGetScenario scenario = new FcRenewalGetScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Controller switch over.
   *
   * @param controller
   *          Target controller
   * @param cluster
   *          Target SW cluster
   * @return Response data
   */
  @POST
  @Path("/switchover")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response switchover(@Encoded @QueryParam("controller") String controller,
      @Encoded @QueryParam("cluster") String cluster) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      RenewalRequest request = new RenewalRequest(null, null, null, controller, cluster);

      setCommonData(request);

      FcSwitchoverScenario scenario = new FcSwitchoverScenario(OperationType.PRIORITY, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
