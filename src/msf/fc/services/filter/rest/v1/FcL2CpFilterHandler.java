
package msf.fc.services.filter.rest.v1;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.services.filter.scenario.filters.FcL2CpFilterCreateDeleteScenario;
import msf.fc.services.filter.scenario.filters.FcL2CpFilterReadListScenario;
import msf.fc.services.filter.scenario.filters.FcL2CpFilterReadScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.rest.common.PATCH;
import msf.mfcfc.services.filter.scenario.filters.data.L2CpFilterRequest;

/**
 * Filter management: Filter information(L2CP).
 *
 * @author NTT
 */
@Path("/v1/filter")
public class FcL2CpFilterHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2CpFilterHandler.class);

  /**
   * L2CP filter addition/deletion.
   *
   * @param sliceId
   *          Slice ID (URI parameter)
   * @param cpId
   *          CP ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message (Body part)
   * @return response data
   */
  @PATCH
  @Path("/slices/l2vpn/{slice_id}/cps/{cp_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createAndDelete(@PathParam("slice_id") String sliceId, @PathParam("cp_id") String cpId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      L2CpFilterRequest request = new L2CpFilterRequest(requestBody, notificationAddress, notificationPort, sliceId,
          cpId, null);

      setCommonData(request);

      FcL2CpFilterCreateDeleteScenario scenario = new FcL2CpFilterCreateDeleteScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * L2CP filter information list acquisition.
   *
   * @param sliceId
   *          Slice ID (URI parameter)
   * @param format
   *          Information type to acquire (optional parameter)
   * @return response data
   */
  @GET
  @Path("/slices/l2vpn/{slice_id}/cps")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("slice_id") String sliceId, @QueryParam("format") String format) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      L2CpFilterRequest request = new L2CpFilterRequest(null, null, null, sliceId, null, format);

      setCommonData(request);

      FcL2CpFilterReadListScenario scenario = new FcL2CpFilterReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * L2CP filter information acquisition.
   *
   * @param sliceId
   *          Slice ID (URI parameter)
   * @param cpId
   *          CP ID (URI parameter)
   * @return response data
   */
  @GET
  @Path("/slices/l2vpn/{slice_id}/cps/{cp_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("slice_id") String sliceId, @PathParam("cp_id") String cpId) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      L2CpFilterRequest request = new L2CpFilterRequest(null, null, null, sliceId, cpId, null);

      setCommonData(request);

      FcL2CpFilterReadScenario scenario = new FcL2CpFilterReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

}
