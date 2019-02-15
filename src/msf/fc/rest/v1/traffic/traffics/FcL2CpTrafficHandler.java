
package msf.fc.rest.v1.traffic.traffics;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.traffic.traffics.FcL2CpTrafficReadListScenario;
import msf.fc.traffic.traffics.FcL2CpTrafficReadScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.traffic.traffics.data.L2CpTrafficRequest;

/**
 * REST request handler class for the L2CP traffic management function.
 *
 * @author NTT
 *
 */
@Path("/v1/traffic/slices/l2vpn")
public class FcL2CpTrafficHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2CpTrafficHandler.class);

  /**
   * L2CP traffic information list acquisition.
   *
   * @param sliceId
   *          Slice ID
   * @return response data
   */
  @GET
  @Path("/{slice_id}/cps")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response cpReadList(@PathParam("slice_id") String sliceId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2CpTrafficRequest request = new L2CpTrafficRequest(null, null, null, SliceType.L2_SLICE.getMessage(), sliceId,
          null);

      setCommonData(request);

      FcL2CpTrafficReadListScenario scenario = new FcL2CpTrafficReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * L2CP traffic information acquisition.
   *
   * @param sliceId
   *          Slice ID
   * @param cpId
   *          CP ID
   * @return response data
   */
  @GET
  @Path("/{slice_id}/cps/{cp_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response cpRead(@PathParam("slice_id") String sliceId, @PathParam("cp_id") String cpId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      L2CpTrafficRequest request = new L2CpTrafficRequest(null, null, null, SliceType.L2_SLICE.getMessage(), sliceId,
          cpId);

      setCommonData(request);

      FcL2CpTrafficReadScenario scenario = new FcL2CpTrafficReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
