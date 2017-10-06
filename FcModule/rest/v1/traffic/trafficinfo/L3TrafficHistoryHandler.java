package msf.fc.rest.v1.traffic.trafficinfo;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.rest.common.AbstractRestHandler;
import msf.fc.traffic.history.L3TrafficHistoryReadListScenario;
import msf.fc.traffic.history.data.L3TrafficRequest;

@Path("/v1/traffic")
public class L3TrafficHistoryHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(L3TrafficHistoryHandler.class);


  @GET
  @Path("/l3vpn/history")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@QueryParam("slice_id") String sliceId, @QueryParam("start_time") String startTime,
      @QueryParam("end_time") String endTime, @QueryParam("interval") Integer interval) {
    try {
      logger.methodStart(new String[] { "sliceId", "startTime", "endTime", "interval" },
          new Object[] { sliceId, startTime, endTime, interval });

      loggingRequestReceived();

      L3TrafficRequest request = new L3TrafficRequest();
      request.setSliceId(sliceId);
      request.setStartTime(startTime);
      request.setEndTime(endTime);
      request.setInterval(interval);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      L3TrafficHistoryReadListScenario scenario = new L3TrafficHistoryReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }
}
