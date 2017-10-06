package msf.fc.rest.v1.common.operation;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.operation.scenario.OperationReadListScenario;
import msf.fc.core.operation.scenario.OperationReadScenario;
import msf.fc.core.operation.scenario.data.OperationRequest;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.rest.common.AbstractRestHandler;


@Path("/v1")

public class ProcessingRequestHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(ProcessingRequestHandler.class);

  private static final String LIST = "list";

  @GET
  @Path("/operations")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@QueryParam("format") String format) {

    try {
      if (logger.isDebugEnabled()) {
        logger.methodStart(new String[] { "format" }, new Object[] { format });
      }
      loggingRequestReceived();
      OperationRequest request = new OperationRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      if (format == null) {
        format = LIST;
      }
      request.setFormat(format);

      OperationReadListScenario scenario = new OperationReadListScenario(OperationType.PRIORITY,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  @GET
  @Path("/operations/{operation_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("operation_id") String operationId) {

    try {
      if (logger.isDebugEnabled()) {
        logger.methodStart(new String[] { "operation_id" }, new Object[] { operationId });
      }
      loggingRequestReceived();
      OperationRequest request = new OperationRequest();
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());
      request.setOperationId(operationId);

      OperationReadScenario scenario = new OperationReadScenario(OperationType.PRIORITY, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

}
