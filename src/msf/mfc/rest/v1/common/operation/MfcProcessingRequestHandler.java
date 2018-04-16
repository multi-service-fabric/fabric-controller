
package msf.mfc.rest.v1.common.operation;

import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.mfc.core.log.scenario.MfcLogReadScenario;
import msf.mfc.core.operation.scenario.MfcOperationNotifyScenario;
import msf.mfc.core.operation.scenario.MfcOperationReadListScenario;
import msf.mfc.core.operation.scenario.MfcOperationReadScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.log.scenario.data.LogReadRequest;
import msf.mfcfc.core.operation.scenario.data.OperationRequest;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Common Processing request.
 *
 * @author NTT
 */
@Path("/v1")
public class MfcProcessingRequestHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcProcessingRequestHandler.class);

  /**
   * Operation list acquisition.
   *
   * @param format
   *          Information type to acquire (optional parameter)
   * @return response data
   */
  @GET
  @Path("/operations")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@QueryParam("format") String format) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      OperationRequest request = new OperationRequest(null, null, null, null, format);

      setCommonData(request);

      MfcOperationReadListScenario scenario = new MfcOperationReadListScenario(OperationType.PRIORITY,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Operation details acquisition.
   *
   * @param operationId
   *          Operation ID (URI parameter)
   * @return response data
   */
  @GET
  @Path("/operations/{operation_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("operation_id") String operationId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      OperationRequest request = new OperationRequest(null, null, null, operationId, null);

      setCommonData(request);

      MfcOperationReadScenario scenario = new MfcOperationReadScenario(OperationType.PRIORITY,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Operation result notification.
   *
   * @param operationId
   *          Operation ID (URI parameter)
   * @param requestBody
   *          Request message (Body part)
   * @return response data
   */
  @PUT
  @Path("/operations/{operation_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response notify(@PathParam("operation_id") String operationId, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      OperationRequest request = new OperationRequest(requestBody, null, null, operationId, null);

      setCommonData(request);

      MfcOperationNotifyScenario scenario = new MfcOperationNotifyScenario(OperationType.PRIORITY,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  /**
   * Controller log acquisition.
   *
   * @param logType
   *          Acquisition target log type
   * @param logLevel
   *          Acquisition target log level
   * @param controller
   *          Acquisition target controller
   * @param cluster
   *          Acquisition target SW cluster
   * @param startDate
   *          Start date of log acquisition target period
   * @param endDate
   *          End date of log acquisition target period
   * @param limitNumber
   *          Upper limit on the number of acquired logs (number of lines)
   * @param searchString
   *          Search target string
   * @param mergeType
   *          Log merge type
   * @return response data
   */
  @GET
  @Path("/operations/log")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response logRead(@QueryParam("log_type") String logType, @QueryParam("log_level") @Encoded String logLevel,
      @QueryParam("controller") @Encoded String controller, @QueryParam("cluster") @Encoded String cluster,
      @QueryParam("start_date") String startDate, @QueryParam("end_date") String endDate,
      @QueryParam("limit_number") Integer limitNumber, @QueryParam("search_string") String searchString,
      @QueryParam("merge_type") String mergeType) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      LogReadRequest request = new LogReadRequest(null, null, null, logType, logLevel, controller, cluster, startDate,
          endDate, limitNumber, searchString, mergeType);

      setCommonData(request);

      MfcLogReadScenario scenario = new MfcLogReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

}
