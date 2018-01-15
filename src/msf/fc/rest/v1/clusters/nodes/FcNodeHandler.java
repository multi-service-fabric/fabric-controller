package msf.fc.rest.v1.clusters.nodes;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.node.nodes.FcNodeReadListScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.nodes.data.NodeRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Configuration management Node information.
 *
 * @author NTT
 */
@Path("/v1/clusters")
public class FcNodeHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeHandler.class);

  /**
   * Node information list acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param format
   *          Information type to acquire (optional parameter)
   * @param userType
   *          User type (optional parameter)
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @QueryParam("format") String format,
      @QueryParam("user-type") String userType) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      NodeRequest request = new NodeRequest(null, null, null, clusterId, format, userType);

      setCommonData(request);

      FcNodeReadListScenario scenario = new FcNodeReadListScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

}
