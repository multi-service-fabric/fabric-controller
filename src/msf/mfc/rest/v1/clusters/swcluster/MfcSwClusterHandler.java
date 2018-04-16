
package msf.mfc.rest.v1.clusters.swcluster;

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

import msf.mfc.node.clusters.MfcClusterCreateScenario;
import msf.mfc.node.clusters.MfcClusterDeleteScenario;
import msf.mfc.node.clusters.MfcClusterReadListScenario;
import msf.mfc.node.clusters.MfcClusterReadScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.clusters.data.SwClusterRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Configuration management SW cluster information management.
 *
 * @author NTT
 */
@Path("/v1/clusters")
public class MfcSwClusterHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcSwClusterHandler.class);

  /**
   * SW cluster addition.
   *
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message (Body part)
   * @return response data
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(@QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      SwClusterRequest request = new SwClusterRequest(requestBody, notificationAddress, notificationPort, null, null,
          null);

      setCommonData(request);

      MfcClusterCreateScenario scenario = new MfcClusterCreateScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * SW cluster information list acquisition.
   *
   * @param format
   *          Information type to acquire (optional parameter)
   * @param userType
   *          User type (optional parameter)
   * @return response data
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@QueryParam("format") String format, @QueryParam("user-type") String userType) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      SwClusterRequest request = new SwClusterRequest(null, null, null, null, format, userType);

      setCommonData(request);

      MfcClusterReadListScenario scenario = new MfcClusterReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * SW cluster information acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param userType
   *          User type (optional parameter)
   * @return response data
   */
  @GET
  @Path("/{cluster_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @QueryParam("user-type") String userType) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      SwClusterRequest request = new SwClusterRequest(null, null, null, clusterId, null, userType);

      setCommonData(request);

      MfcClusterReadScenario scenario = new MfcClusterReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  /**
   * SW cluster reduction.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @return response data
   */
  @DELETE
  @Path("/{cluster_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("cluster_id") String clusterId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      SwClusterRequest request = new SwClusterRequest(null, notificationAddress, notificationPort, clusterId, null,
          null);

      setCommonData(request);

      MfcClusterDeleteScenario scenario = new MfcClusterDeleteScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }
}
