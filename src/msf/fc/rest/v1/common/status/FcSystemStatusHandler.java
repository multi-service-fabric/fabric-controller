
package msf.fc.rest.v1.common.status;

import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.core.status.scenario.FcSystemStatusReadScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.status.scenario.data.SystemStatusReadRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * REST request handler class of system status.
 *
 * @author NTT
 */
@Path("/v1")
public class FcSystemStatusHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcSystemStatusHandler.class);

  /**
   * System status check.
   *
   * @param controller
   *          Acquisition target controller
   * @param cluster
   *          Acquisition target cluster
   * @param getInfo
   *          Acquired information
   *
   * @return response data
   */
  @GET
  @Path("/MSFcontroller/status")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@QueryParam("controller") @Encoded String controller,
      @QueryParam("cluster") @Encoded String cluster, @QueryParam("get_info") @Encoded String getInfo) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      SystemStatusReadRequest request = new SystemStatusReadRequest(null, null, null, controller, cluster, getInfo);

      setCommonData(request);

      FcSystemStatusReadScenario scenario = new FcSystemStatusReadScenario(OperationType.PRIORITY,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
