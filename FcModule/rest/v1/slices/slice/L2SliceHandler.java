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
import msf.fc.slice.slices.l2slice.L2SliceCreateScenario;
import msf.fc.slice.slices.l2slice.L2SliceDeleteScenario;
import msf.fc.slice.slices.l2slice.L2SliceReadListScenario;
import msf.fc.slice.slices.l2slice.L2SliceReadScenario;
import msf.fc.slice.slices.l2slice.L2SliceUpdateScenario;
import msf.fc.slice.slices.l2slice.data.L2SliceRequest;

@Path("/v1/slices")
public class L2SliceHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(L2SliceHandler.class);

  @POST
  @Path("/l2vpn")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);
      L2SliceRequest request = new L2SliceRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setRequestBody(requestBody);

      L2SliceCreateScenario scenario = new L2SliceCreateScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  @PUT
  @Path("/l2vpn/{slice_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("slice_id") String sliceId, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);
      L2SliceRequest request = new L2SliceRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setSliceId(sliceId);
      request.setRequestBody(requestBody);

      L2SliceUpdateScenario scenario = new L2SliceUpdateScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  @DELETE
  @Path("/l2vpn/{slice_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("slice_id") String sliceId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2SliceRequest request = new L2SliceRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setSliceId(sliceId);

      L2SliceDeleteScenario scenario = new L2SliceDeleteScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  @GET
  @Path("/l2vpn/{slice_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("slice_id") String sliceId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2SliceRequest request = new L2SliceRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setSliceId(sliceId);

      L2SliceReadScenario scenario = new L2SliceReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  @GET
  @Path("/l2vpn")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@QueryParam("format") String format) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2SliceRequest request = new L2SliceRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setFormat(format);

      L2SliceReadListScenario scenario = new L2SliceReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }
}
