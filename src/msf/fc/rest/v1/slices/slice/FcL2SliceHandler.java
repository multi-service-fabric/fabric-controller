
package msf.fc.rest.v1.slices.slice;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.slice.slices.l2slice.FcL2SliceCreateScenario;
import msf.fc.slice.slices.l2slice.FcL2SliceDeleteScenario;
import msf.fc.slice.slices.l2slice.FcL2SliceReadListScenario;
import msf.fc.slice.slices.l2slice.FcL2SliceReadScenario;
import msf.fc.slice.slices.l2slice.FcL2SliceUpdateScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.slice.slices.l2slice.data.L2SliceRequest;

/**
 * REST request handler class for the L2 slice control (slice management
 * function).
 *
 * @author NTT
 *
 */
@Path("/v1/slices")
public class FcL2SliceHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2SliceHandler.class);

  /**
   * L2 slice addition.
   *
   * @param requestBody
   *          Request message body
   * @return response data
   */
  @POST
  @Path("/l2vpn")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);
      L2SliceRequest request = new L2SliceRequest(requestBody, null, null, null, null);

      setCommonData(request);

      FcL2SliceCreateScenario scenario = new FcL2SliceCreateScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * L2 slice modification.
   *
   * @param sliceId
   *          Slice ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message body
   * @return response data
   */
  @PUT
  @Path("/l2vpn/{slice_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("slice_id") String sliceId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2SliceRequest request = new L2SliceRequest(requestBody, notificationAddress, notificationPort, sliceId, null);

      setCommonData(request);

      FcL2SliceUpdateScenario scenario = new FcL2SliceUpdateScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * L2 slice deletion.
   *
   * @param sliceId
   *          Slice ID (URI parameter)
   * @return response data
   */
  @DELETE
  @Path("/l2vpn/{slice_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("slice_id") String sliceId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2SliceRequest request = new L2SliceRequest(null, null, null, sliceId, null);

      setCommonData(request);

      FcL2SliceDeleteScenario scenario = new FcL2SliceDeleteScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  /**
   * L2 slice information acquisition.
   *
   * @param sliceId
   *          Slice ID (URI parameter)
   * @return response data
   */
  @GET
  @Path("/l2vpn/{slice_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("slice_id") String sliceId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2SliceRequest request = new L2SliceRequest(null, null, null, sliceId, null);

      setCommonData(request);

      FcL2SliceReadScenario scenario = new FcL2SliceReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  /**
   * L2 slice information list acquisition.
   *
   * @param format
   *          Information type to acquire (optional parameter)
   * @return response data
   */
  @GET
  @Path("/l2vpn")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@QueryParam("format") String format) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2SliceRequest request = new L2SliceRequest(null, null, null, null, format);

      setCommonData(request);

      FcL2SliceReadListScenario scenario = new FcL2SliceReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }
}
