package msf.fc.rest.v1.internal.fabriccontroller;

import java.text.MessageFormat;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.common.constant.InternalOperationAction;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.exception.MsfException;
import msf.fc.common.internal.InternalOperationScenario;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.ErrorResponseData;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.failure.logicalif.LogicalIfStatusReadScenario;
import msf.fc.failure.logicalif.LogicalIfStatusUpdateScenario;
import msf.fc.failure.logicalif.data.LogicalIfStatusRequest;
import msf.fc.rest.common.AbstractRestHandler;

@Path("/v1/internal/FabricController/operations")
public class InternalOperationHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(InternalOperationHandler.class);

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(String requestBody) {
    logger.methodStart();
    try {
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      RestResponseBase actionResponse = executeInternalOperationScenario(requestBody);
      logger.debug("Internal Operation action={0}", actionResponse.getResponseBody());

      RestResponseBase restResponseBase = null;
      if (actionResponse instanceof ErrorResponseData) {
        restResponseBase = actionResponse;
      } else {
        switch (InternalOperationAction.getEnumFromMessage(actionResponse.getResponseBody())) {
          case GET_LOGICAL_IF_STATUS:
            restResponseBase = executeLogicalIfStatusReadScenario(requestBody);
            break;
          case UPDATE_LOGICAL_IF_STATUS:
            restResponseBase = executeLogicalIfStatusUpdateScenario(requestBody);
            break;
          default:
            throw new IllegalArgumentException(MessageFormat.format("action={0}",
                InternalOperationAction.getEnumFromMessage(actionResponse.getResponseBody())));
        }

        loggingResponseJsonBody(restResponseBase.getResponseBody());
        loggingReturnedResponse(restResponseBase.getHttpStatusCode());
      }
      return createResponse(restResponseBase);
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase executeInternalOperationScenario(String requestBody) {
    logger.methodStart();
    RestRequestBase request = new RestRequestBase();
    request.setRequestUri(getReuqestUri());
    request.setRequestMethod(getHttpMethod());
    request.setRequestBody(requestBody);

    InternalOperationScenario scenario = new InternalOperationScenario(OperationType.NORMAL,
        SystemInterfaceType.INTERNAL);
    logger.methodEnd();
    return scenario.execute(request);
  }

  private RestResponseBase executeLogicalIfStatusReadScenario(String requestBody) {
    logger.methodStart();
    LogicalIfStatusRequest request = new LogicalIfStatusRequest();
    request.setRequestUri(getReuqestUri());
    request.setRequestMethod(getHttpMethod());
    request.setRequestBody(requestBody);

    LogicalIfStatusReadScenario logicalIfStatusReadScenario = new LogicalIfStatusReadScenario(OperationType.NORMAL,
        SystemInterfaceType.INTERNAL);
    logger.methodEnd();
    return logicalIfStatusReadScenario.execute(request);
  }

  private RestResponseBase executeLogicalIfStatusUpdateScenario(String requestBody) {
    logger.methodStart();
    LogicalIfStatusRequest request = new LogicalIfStatusRequest();
    request.setRequestUri(getReuqestUri());
    request.setRequestMethod(getHttpMethod());
    request.setRequestBody(requestBody);

    LogicalIfStatusUpdateScenario logicalIfStatusUpdateScenario = new LogicalIfStatusUpdateScenario(
        OperationType.NORMAL, SystemInterfaceType.INTERNAL);
    logger.methodEnd();
    return logicalIfStatusUpdateScenario.execute(request);
  }

}
