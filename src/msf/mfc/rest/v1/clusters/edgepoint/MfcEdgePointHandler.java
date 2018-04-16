
package msf.mfc.rest.v1.clusters.edgepoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.mfc.node.interfaces.edgepoints.MfcEdgePointCreateScenario;
import msf.mfc.node.interfaces.edgepoints.MfcEdgePointDeleteScenario;
import msf.mfc.node.interfaces.edgepoints.MfcEdgePointReadListScenario;
import msf.mfc.node.interfaces.edgepoints.MfcEdgePointReadScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.interfaces.edgepoints.data.EdgePointRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Configuration management: EdgePoint management (downlinkIF).
 *
 * @author NTT
 */
@Path("/v1/clusters")
public class MfcEdgePointHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcEdgePointHandler.class);

  /**
   * downlinkIF registration.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param requestBody
   *          Request message body
   * @return response data
   */
  @POST
  @Path("/{cluster_id}/points/edge-points")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response create(@PathParam("cluster_id") String clusterId, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      EdgePointRequest request = new EdgePointRequest(requestBody, null, null, clusterId, null, null, null);

      setCommonData(request);

      MfcEdgePointCreateScenario scenario = new MfcEdgePointCreateScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * downlinkIF information list acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param format
   *          Information type to acquire (optional parameter)
   * @param userType
   *          User type (URI parameter)
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/points/edge-points")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @QueryParam("format") String format,
      @QueryParam("user-type") String userType) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      EdgePointRequest request = new EdgePointRequest(null, null, null, clusterId, null, format, userType);

      setCommonData(request);

      MfcEdgePointReadListScenario scenario = new MfcEdgePointReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * downlinkIF information acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param edgePointId
   *          Edge-point ID (URI parameter)
   * @param userType
   *          User type
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/points/edge-points/{edge_point_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("edge_point_id") String edgePointId,
      @QueryParam("user-type") String userType) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      EdgePointRequest request = new EdgePointRequest(null, null, null, clusterId, edgePointId, null, userType);

      setCommonData(request);

      MfcEdgePointReadScenario scenario = new MfcEdgePointReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * downlinkIF deletion.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param edgePointId
   *          Edge-point ID (URI parameter)
   * @return response data
   */
  @DELETE
  @Path("/{cluster_id}/points/edge-points/{edge_point_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("cluster_id") String clusterId, @PathParam("edge_point_id") String edgePointId) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      EdgePointRequest request = new EdgePointRequest(null, null, null, clusterId, edgePointId, null, null);

      setCommonData(request);

      MfcEdgePointDeleteScenario scenario = new MfcEdgePointDeleteScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
