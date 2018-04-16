
package msf.fc.rest.v1.common.operation;

import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.core.log.scenario.FcLogReadScenario;
import msf.fc.core.operation.scenario.FcOperationReadListScenario;
import msf.fc.core.operation.scenario.FcOperationReadScenario;
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
public class FcProcessingRequestHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcProcessingRequestHandler.class);

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

      FcOperationReadListScenario scenario = new FcOperationReadListScenario(OperationType.PRIORITY,
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

      FcOperationReadScenario scenario = new FcOperationReadScenario(OperationType.PRIORITY,
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

      FcLogReadScenario scenario = new FcLogReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
