
package msf.fc.rest.v1.traffic.traffics;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.traffic.traffics.FcCpTrafficReadListScenario;
import msf.fc.traffic.traffics.FcCpTrafficReadScenario;
import msf.fc.traffic.traffics.FcIfTrafficReadListScenario;
import msf.fc.traffic.traffics.FcIfTrafficReadScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.traffic.traffics.data.CpTrafficRequest;
import msf.mfcfc.traffic.traffics.data.IfTrafficRequest;

/**
 * REST request handler class for IF traffic management function.
 *
 * @author NTT
 *
 */
@Path("/v1/traffic")
public class FcTrafficHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcTrafficHandler.class);

  /**
   * IF traffic information list acquisition.
   *
   * @param clusterId
   *          Cluster ID
   * @param fabricType
   *          Node type
   * @param nodeId
   *          Node ID
   * @return response data
   */
  @GET
  @Path("/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response ifReadList(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      IfTrafficRequest request = new IfTrafficRequest(null, null, null, clusterId, fabricType, nodeId, null, null);

      setCommonData(request);

      FcIfTrafficReadListScenario scenario = new FcIfTrafficReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * IF traffic information acquisition.
   *
   * @param clusterId
   *          Cluster ID
   * @param fabricType
   *          Node type
   * @param nodeId
   *          Node ID
   * @param ifType
   *          IF type
   * @param ifId
   *          IF ID
   * @return response data
   */
  @GET
  @Path("/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/{if_type}/{if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response ifRead(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("if_type") String ifType, @PathParam("if_id") String ifId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      IfTrafficRequest request = new IfTrafficRequest(null, null, null, clusterId, fabricType, nodeId, ifType, ifId);

      setCommonData(request);

      FcIfTrafficReadScenario scenario = new FcIfTrafficReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * CP traffic information list acquisition.
   *
   * @param sliceType
   *          Slice type
   * @param sliceId
   *          Slice ID
   * @return response data
   */
  @GET
  @Path("/slices/{slice_type}/{slice_id}/cps")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response cpReadList(@PathParam("slice_type") String sliceType, @PathParam("slice_id") String sliceId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      CpTrafficRequest request = new CpTrafficRequest(null, null, null, sliceType, sliceId, null);

      setCommonData(request);

      FcCpTrafficReadListScenario scenario = new FcCpTrafficReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * CP traffic information acquisition.
   *
   * @param sliceType
   *          Slice type
   * @param sliceId
   *          Slice ID
   * @param cpId
   *          CP ID
   * @return response data
   */
  @GET
  @Path("/slices/{slice_type}/{slice_id}/cps/{cp_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response cpRead(@PathParam("slice_type") String sliceType, @PathParam("slice_id") String sliceId,
      @PathParam("cp_id") String cpId) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      CpTrafficRequest request = new CpTrafficRequest(null, null, null, sliceType, sliceId, cpId);

      setCommonData(request);

      FcCpTrafficReadScenario scenario = new FcCpTrafficReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
