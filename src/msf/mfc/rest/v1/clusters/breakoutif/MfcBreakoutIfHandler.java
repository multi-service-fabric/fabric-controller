
package msf.mfc.rest.v1.clusters.breakoutif;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.mfc.node.interfaces.breakoutifs.MfcBreakoutInterfaceCreateDeleteScenario;
import msf.mfc.node.interfaces.breakoutifs.MfcBreakoutInterfaceReadListScenario;
import msf.mfc.node.interfaces.breakoutifs.MfcBreakoutInterfaceReadScenario;
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
public class MfcBreakoutIfHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcBreakoutIfHandler.class);

  /**
   * breakoutIF registration/deletion.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Node type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message body
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

      MfcBreakoutInterfaceCreateDeleteScenario scenario = new MfcBreakoutInterfaceCreateDeleteScenario(
          OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  /**
   * breakoutIF information list acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Node type (URI parameter)
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

      MfcBreakoutInterfaceReadListScenario scenario = new MfcBreakoutInterfaceReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * breakoutIF information acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Node type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param breakoutIfId
   *          breakoutIF ID (URI parameter)
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

      MfcBreakoutInterfaceReadScenario scenario = new MfcBreakoutInterfaceReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

}
