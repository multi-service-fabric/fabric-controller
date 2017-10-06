package msf.fc.rest.v1.slices.cp;

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

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.rest.common.AbstractRestHandler;
import msf.fc.slice.cps.l2cp.L2CpCreateScenario;
import msf.fc.slice.cps.l2cp.L2CpDeleteScenario;
import msf.fc.slice.cps.l2cp.L2CpReadListScenario;
import msf.fc.slice.cps.l2cp.L2CpReadScenario;
import msf.fc.slice.cps.l2cp.L2CpUpdateScenario;
import msf.fc.slice.cps.l2cp.data.L2CpRequest;

@Path("/v1/slices")
public class L2CpHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(L2CpHandler.class);

  @POST
  @Path("/l2vpn/{slice_id}/cps")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(@PathParam("slice_id") String sliceId, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);
      L2CpRequest request = new L2CpRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setSliceId(sliceId);
      request.setRequestBody(requestBody);

      L2CpCreateScenario scenario = new L2CpCreateScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  @PUT
  @Path("/l2vpn/{slice_id}/cps/{cp_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("slice_id") String sliceId, @PathParam("cp_id") String cpId, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);
      L2CpRequest request = new L2CpRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setSliceId(sliceId);
      request.setCpId(cpId);
      request.setRequestBody(requestBody);

      L2CpUpdateScenario scenario = new L2CpUpdateScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  @DELETE
  @Path("/l2vpn/{slice_id}/cps/{cp_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("slice_id") String sliceId, @PathParam("cp_id") String cpId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2CpRequest request = new L2CpRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setSliceId(sliceId);
      request.setCpId(cpId);

      L2CpDeleteScenario scenario = new L2CpDeleteScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  @GET
  @Path("/l2vpn/{slice_id}/cps/{cp_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("slice_id") String sliceId, @PathParam("cp_id") String cpId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2CpRequest request = new L2CpRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setSliceId(sliceId);
      request.setCpId(cpId);

      L2CpReadScenario scenario = new L2CpReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  @GET
  @Path("/l2vpn/{slice_id}/cps")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("slice_id") String sliceId, @QueryParam("format") String format) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2CpRequest request = new L2CpRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setSliceId(sliceId);
      request.setFormat(format);

      L2CpReadListScenario scenario = new L2CpReadListScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
