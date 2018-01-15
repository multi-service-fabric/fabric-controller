package msf.fc.rest.v1.clusters.clusterlinkif;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.node.interfaces.clusterlinkifs.FcClusterLinkInterfaceCreateScenario;
import msf.fc.node.interfaces.clusterlinkifs.FcClusterLinkInterfaceDeleteScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Configuration management Intercluster link management.
 *
 * @author NTT
 */
@Path("/v1/clusters")
public class FcClusterLinkIfHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcClusterLinkIfHandler.class);

  /**
   * Intercluster link IF generation.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message body
   * @return response data
   */
  @POST
  @Path("/{cluster_id}/interfaces/cluster-link-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response create(@PathParam("cluster_id") String clusterId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      ClusterLinkIfRequest request = new ClusterLinkIfRequest(requestBody, notificationAddress, notificationPort,
          clusterId, null, null);

      setCommonData(request);

      FcClusterLinkInterfaceCreateScenario scenario = new FcClusterLinkInterfaceCreateScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Intercluster link IF reduction.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param clusterLinkIfId
   *          Intercluster link IF ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @return response data
   */
  @DELETE
  @Path("/{cluster_id}/interfaces/cluster-link-ifs/{cluster_link_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("cluster_id") String clusterId,
      @PathParam("cluster_link_if_id") String clusterLinkIfId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      ClusterLinkIfRequest request = new ClusterLinkIfRequest(null, notificationAddress, notificationPort, clusterId,
          clusterLinkIfId, null);

      setCommonData(request);

      FcClusterLinkInterfaceDeleteScenario scenario = new FcClusterLinkInterfaceDeleteScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
