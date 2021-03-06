
package msf.fc.rest.v1.clusters.breakoutif;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.node.interfaces.breakoutifs.FcBreakoutInterfaceCreateDeleteScenario;
import msf.fc.node.interfaces.breakoutifs.FcBreakoutInterfaceReadListScenario;
import msf.fc.node.interfaces.breakoutifs.FcBreakoutInterfaceReadScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;
import msf.mfcfc.rest.common.PATCH;

/**
 * Configuration management Interface management (physical IF).
 *
 * @author NTT
 */
@Path("/v1/clusters")
public class FcBreakoutIfHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcBreakoutIfHandler.class);

  /**
   * breakout IF registration/deletion.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Fabric type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message (Body part)
   * @return response data
   */
  @PATCH
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/breakout-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createAndDelete(@PathParam("cluster_id") String clusterId,
      @PathParam("fabric_type") String fabricType, @PathParam("node_id") String nodeId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      BreakoutIfRequest request = new BreakoutIfRequest(requestBody, notificationAddress, notificationPort, clusterId,
          fabricType, nodeId, null, null);

      setCommonData(request);

      FcBreakoutInterfaceCreateDeleteScenario scenario = new FcBreakoutInterfaceCreateDeleteScenario(
          OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * breakout IF information list acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Fabric type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param format
   *          Information type to acquire (optional parameter)
   *
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/breakout-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @QueryParam("format") String format) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      BreakoutIfRequest request = new BreakoutIfRequest(null, null, null, clusterId, fabricType, nodeId, null, format);

      setCommonData(request);

      FcBreakoutInterfaceReadListScenario scenario = new FcBreakoutInterfaceReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * breakout IF information acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Fabric type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param breakoutIfId
   *          breakout IF ID (URI parameter)
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/breakout-ifs/{breakout_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("breakout_if_id") String breakoutIfId) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      BreakoutIfRequest request = new BreakoutIfRequest(null, null, null, clusterId, fabricType, nodeId, breakoutIfId,
          null);

      setCommonData(request);

      FcBreakoutInterfaceReadScenario scenario = new FcBreakoutInterfaceReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

}
