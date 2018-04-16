
package msf.mfc.rest.v1.slices.cp;

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

import msf.mfc.slice.cps.l2cp.MfcL2CpCreateDeleteScenario;
import msf.mfc.slice.cps.l2cp.MfcL2CpCreateScenario;
import msf.mfc.slice.cps.l2cp.MfcL2CpDeleteScenario;
import msf.mfc.slice.cps.l2cp.MfcL2CpReadListScenario;
import msf.mfc.slice.cps.l2cp.MfcL2CpReadScenario;
import msf.mfc.slice.cps.l2cp.MfcL2CpUpdateScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.rest.common.PATCH;
import msf.mfcfc.slice.cps.l2cp.data.L2CpRequest;

/**
 * REST request handler class for L2CP control (slice management function).
 *
 * @author NTT
 *
 */
@Path("/v1/slices")
public class MfcL2CpHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2CpHandler.class);

  /**
   * L2CP addition/deletion(/modification).
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
  @PATCH
  @Path("/l2vpn/{slice_id}/cps")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createAndDelete(@PathParam("slice_id") String sliceId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);
      L2CpRequest request = new L2CpRequest(requestBody, notificationAddress, notificationPort, sliceId, null, null);

      setCommonData(request);

      MfcL2CpCreateDeleteScenario scenario = new MfcL2CpCreateDeleteScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * L2CP addition.
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
  @POST
  @Path("/l2vpn/{slice_id}/cps")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(@PathParam("slice_id") String sliceId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);
      L2CpRequest request = new L2CpRequest(requestBody, notificationAddress, notificationPort, sliceId, null, null);

      setCommonData(request);

      MfcL2CpCreateScenario scenario = new MfcL2CpCreateScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * L2CP modification.
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
   *          Request message body
   * @return response data
   */
  @PUT
  @Path("/l2vpn/{slice_id}/cps/{cp_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("slice_id") String sliceId, @PathParam("cp_id") String cpId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2CpRequest request = new L2CpRequest(requestBody, notificationAddress, notificationPort, sliceId, cpId, null);

      setCommonData(request);

      MfcL2CpUpdateScenario scenario = new MfcL2CpUpdateScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * L2CP deletion.
   *
   * @param sliceId
   *          Slice ID (URI parameter)
   * @param cpId
   *          CP ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @return response data
   */
  @DELETE
  @Path("/l2vpn/{slice_id}/cps/{cp_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("slice_id") String sliceId, @PathParam("cp_id") String cpId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2CpRequest request = new L2CpRequest(null, notificationAddress, notificationPort, sliceId, cpId, null);

      setCommonData(request);

      MfcL2CpDeleteScenario scenario = new MfcL2CpDeleteScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * L2CP information acquisition.
   *
   * @param sliceId
   *          Slice ID (URI parameter)
   * @param cpId
   *          CP ID
   * @return response data
   */
  @GET
  @Path("/l2vpn/{slice_id}/cps/{cp_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("slice_id") String sliceId, @PathParam("cp_id") String cpId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2CpRequest request = new L2CpRequest(null, null, null, sliceId, cpId, null);

      setCommonData(request);

      MfcL2CpReadScenario scenario = new MfcL2CpReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * L2CP information list acquisition.
   *
   * @param sliceId
   *          Slice ID (URI parameter)
   * @param format
   *          Information type to acquire (optional parameter)
   * @return response data
   */
  @GET
  @Path("/l2vpn/{slice_id}/cps")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("slice_id") String sliceId, @QueryParam("format") String format) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2CpRequest request = new L2CpRequest(null, null, null, sliceId, null, format);

      setCommonData(request);

      MfcL2CpReadListScenario scenario = new MfcL2CpReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
