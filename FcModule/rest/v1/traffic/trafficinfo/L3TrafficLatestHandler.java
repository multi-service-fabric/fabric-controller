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
import msf.fc.traffic.history.L3TrafficLatestReadListScenario;
import msf.fc.traffic.history.data.L3TrafficRequest;

@Path("/v1/traffic")
public class L3TrafficLatestHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(L3TrafficLatestHandler.class);

  @GET
  @Path("/l3vpn/latest")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@QueryParam("slice_id") String sliceId) {

    try {
      logger.methodStart(new String[] { "sliceId" }, new Object[] { sliceId });

      loggingRequestReceived();

      L3TrafficRequest request = new L3TrafficRequest();
      request.setSliceId(sliceId);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      L3TrafficLatestReadListScenario scenario = new L3TrafficLatestReadListScenario(OperationType.NORMAL,
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
