
package msf.fc.services.filter.rest.v1;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.services.filter.scenario.filters.FcLagIfFilterCreateDeleteScenario;
import msf.fc.services.filter.scenario.filters.FcLagIfFilterReadListScenario;
import msf.fc.services.filter.scenario.filters.FcLagIfFilterReadScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.rest.common.PATCH;
import msf.mfcfc.services.filter.scenario.filters.data.LagIfFilterRequest;

/**
 * Filter management: Filter information(LagIF).
 *
 * @author NTT
 */
@Path("/v1/filter")
public class FcLagIfFilterHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLagIfFilterHandler.class);

  /**
   * LagIF filter addition/deletion.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Fabric type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param lagIfId
   *          LagIF ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message (Body part)
   * @return response data
   */
  @PATCH
  @Path("/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs/{lag_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createAndDelete(@PathParam("cluster_id") String clusterId,
      @PathParam("fabric_type") String fabricType, @PathParam("node_id") String nodeId,
      @PathParam("lag_if_id") String lagIfId, @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      LagIfFilterRequest request = new LagIfFilterRequest(requestBody, notificationAddress, notificationPort, clusterId,
          fabricType, nodeId, lagIfId, null);

      setCommonData(request);

      FcLagIfFilterCreateDeleteScenario scenario = new FcLagIfFilterCreateDeleteScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * LagIF filter information list acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Fabric type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param format
   *          Information type to acquire (optional parameter)
   * @return response data
   */
  @GET
  @Path("/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @QueryParam("format") String format) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      LagIfFilterRequest request = new LagIfFilterRequest(null, null, null, clusterId, fabricType, nodeId, null,
          format);

      setCommonData(request);

      FcLagIfFilterReadListScenario scenario = new FcLagIfFilterReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * LagIF filter information acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Fabric type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param lagIfId
   *          LagIF ID (URI parameter)
   * @return response data
   */
  @GET
  @Path("/clusters/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/lag-ifs/{lag_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("lag_if_id") String lagIfId) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      LagIfFilterRequest request = new LagIfFilterRequest(null, null, null, clusterId, fabricType, nodeId, lagIfId,
          null);

      setCommonData(request);

      FcLagIfFilterReadScenario scenario = new FcLagIfFilterReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

}
