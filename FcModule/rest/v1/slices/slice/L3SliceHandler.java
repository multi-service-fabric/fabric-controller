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

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.rest.common.AbstractRestHandler;
import msf.fc.slice.slices.l3slice.L3SliceCreateScenario;
import msf.fc.slice.slices.l3slice.L3SliceDeleteScenario;
import msf.fc.slice.slices.l3slice.L3SliceReadListScenario;
import msf.fc.slice.slices.l3slice.L3SliceReadScenario;
import msf.fc.slice.slices.l3slice.L3SliceUpdateScenario;
import msf.fc.slice.slices.l3slice.data.L3SliceRequest;

@Path("/v1/slices")
public class L3SliceHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(L3SliceHandler.class);

  @POST
  @Path("/l3vpn")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);
      L3SliceRequest request = new L3SliceRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setRequestBody(requestBody);

      L3SliceCreateScenario scenario = new L3SliceCreateScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  @PUT
  @Path("/l3vpn/{slice_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("slice_id") String sliceId, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);
      L3SliceRequest request = new L3SliceRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setSliceId(sliceId);
      request.setRequestBody(requestBody);

      L3SliceUpdateScenario scenario = new L3SliceUpdateScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  @DELETE
  @Path("/l3vpn/{slice_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("slice_id") String sliceId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L3SliceRequest request = new L3SliceRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setSliceId(sliceId);

      L3SliceDeleteScenario scenario = new L3SliceDeleteScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  @GET
  @Path("/l3vpn/{slice_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("slice_id") String sliceId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L3SliceRequest request = new L3SliceRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setSliceId(sliceId);

      L3SliceReadScenario scenario = new L3SliceReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  @GET
  @Path("/l3vpn")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@QueryParam("format") String format) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L3SliceRequest request = new L3SliceRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setFormat(format);

      L3SliceReadListScenario scenario = new L3SliceReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }
}
