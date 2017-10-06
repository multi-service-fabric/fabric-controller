package msf.fc.rest.v1.common.status;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.core.status.scenario.SystemStatusReadScenario;
import msf.fc.core.status.scenario.SystemStatusUpdateScenario;
import msf.fc.core.status.scenario.data.SystemStatusReadRequest;
import msf.fc.core.status.scenario.data.SystemStatusUpdateRequest;
import msf.fc.rest.common.AbstractRestHandler;

@Path("/v1")
public class SystemStatusHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(SystemStatusHandler.class);

  @GET
  @Path("/MSFcontroller/status")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read() {
    try {
      logger.methodStart();
      loggingRequestReceived();
      SystemStatusReadRequest request = new SystemStatusReadRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      SystemStatusReadScenario scenario = new SystemStatusReadScenario(OperationType.PRIORITY,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  @PUT
  @Path("/internal/MSFcontroller/status")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);
      SystemStatusUpdateRequest request = new SystemStatusUpdateRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setRequestBody(requestBody);

      SystemStatusUpdateScenario scenario = new SystemStatusUpdateScenario(OperationType.PRIORITY,
          SystemInterfaceType.INTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
