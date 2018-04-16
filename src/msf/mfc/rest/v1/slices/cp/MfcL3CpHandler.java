
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

import msf.mfc.slice.cps.l3cp.MfcL3CpCreateDeleteScenario;
import msf.mfc.slice.cps.l3cp.MfcL3CpCreateScenario;
import msf.mfc.slice.cps.l3cp.MfcL3CpDeleteScenario;
import msf.mfc.slice.cps.l3cp.MfcL3CpReadListScenario;
import msf.mfc.slice.cps.l3cp.MfcL3CpReadScenario;
import msf.mfc.slice.cps.l3cp.MfcL3CpStaticRouteCreateDeleteScenario;
import msf.mfc.slice.cps.l3cp.MfcL3CpUpdateScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.rest.common.PATCH;
import msf.mfcfc.slice.cps.l3cp.data.L3CpRequest;

/**
 * REST request handler class for L3CP control (slice management function).
 *
 * @author NTT
 *
 */
@Path("/v1/slices")
public class MfcL3CpHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3CpHandler.class);

  /**
   * L3CP addition/deletion(/modification).
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
  @Path("/l3vpn/{slice_id}/cps")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createAndDelete(@PathParam("slice_id") String sliceId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);
      L3CpRequest request = new L3CpRequest(requestBody, notificationAddress, notificationPort, sliceId, null, null);

      setCommonData(request);

      MfcL3CpCreateDeleteScenario scenario = new MfcL3CpCreateDeleteScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * L3CP addition.
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
  @Path("/l3vpn/{slice_id}/cps")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(@PathParam("slice_id") String sliceId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);
      L3CpRequest request = new L3CpRequest(requestBody, notificationAddress, notificationPort, sliceId, null, null);

      setCommonData(request);

      MfcL3CpCreateScenario scenario = new MfcL3CpCreateScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * L3CP modification.
   *
   * @param sliceId
   *          Slice ID (URI parameter)
   * @param cpId
   *          CP ID (URI parameter)
   * @param requestBody
   *          Request message body
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @return response data
   */
  @PUT
  @Path("/l3vpn/{slice_id}/cps/{cp_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("slice_id") String sliceId, @PathParam("cp_id") String cpId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L3CpRequest request = new L3CpRequest(requestBody, notificationAddress, notificationPort, sliceId, cpId, null);

      setCommonData(request);

      MfcL3CpUpdateScenario scenario = new MfcL3CpUpdateScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * L3CP deletion.
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
  @Path("/l3vpn/{slice_id}/cps/{cp_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("slice_id") String sliceId, @PathParam("cp_id") String cpId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L3CpRequest request = new L3CpRequest(null, notificationAddress, notificationPort, sliceId, cpId, null);

      setCommonData(request);

      MfcL3CpDeleteScenario scenario = new MfcL3CpDeleteScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * L3CP information acquisition.
   *
   * @param sliceId
   *          Slice ID (URI parameter)
   * @param cpId
   *          CP ID
   * @return response data
   */
  @GET
  @Path("/l3vpn/{slice_id}/cps/{cp_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("slice_id") String sliceId, @PathParam("cp_id") String cpId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L3CpRequest request = new L3CpRequest(null, null, null, sliceId, cpId, null);

      setCommonData(request);

      MfcL3CpReadScenario scenario = new MfcL3CpReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * L3CP information list acquisition.
   *
   * @param sliceId
   *          Slice ID (URI parameter)
   * @param format
   *          Information type to acquire (optional parameter)
   * @return response data
   */
  @GET
  @Path("/l3vpn/{slice_id}/cps")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("slice_id") String sliceId, @QueryParam("format") String format) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L3CpRequest request = new L3CpRequest(null, null, null, sliceId, null, format);

      setCommonData(request);

      MfcL3CpReadListScenario scenario = new MfcL3CpReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Static route addition/deletion.
   *
   * @param sliceId
   *          Slice ID (URI parameter)
   * @param cpId
   *          CP ID
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message body
   * @return response data
   */
  @PATCH
  @Path("/l3vpn/{slice_id}/cps/{cp_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createAndDeleteStaticRoute(@PathParam("slice_id") String sliceId, @PathParam("cp_id") String cpId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);
      L3CpRequest request = new L3CpRequest(requestBody, notificationAddress, notificationPort, sliceId, cpId, null);

      setCommonData(request);

      MfcL3CpStaticRouteCreateDeleteScenario scenario = new MfcL3CpStaticRouteCreateDeleteScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
