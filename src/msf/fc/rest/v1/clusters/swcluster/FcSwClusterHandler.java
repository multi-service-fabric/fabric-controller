
package msf.fc.rest.v1.clusters.swcluster;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.node.clusters.FcClusterReadListScenario;
import msf.fc.node.clusters.FcClusterReadScenario;
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
public class FcSwClusterHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcSwClusterHandler.class);

  /**
   * SW Cluster information list acquisition.
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

      FcClusterReadListScenario scenario = new FcClusterReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * SW Cluster information acquisition.
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

      FcClusterReadScenario scenario = new FcClusterReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }
}
