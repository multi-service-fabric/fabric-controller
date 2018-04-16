
package msf.mfc.rest.v1.clusters.rr;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.mfc.node.nodes.rrs.MfcRrNodeReadListScenario;
import msf.mfc.node.nodes.rrs.MfcRrNodeReadScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.nodes.rrs.data.RrNodeRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Configuration management Node management (RR).
 *
 * @author NTT
 */
@Path("/v1/clusters")
public class MfcRrNodeHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcRrNodeHandler.class);

  /**
   * RR information list acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param format
   *          Information type to acquire (optional parameter)
   * @param userType
   *          User type
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes/rrs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @QueryParam("format") String format,
      @QueryParam("user-type") String userType) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      RrNodeRequest request = new RrNodeRequest(null, null, null, clusterId, null, format, userType);

      setCommonData(request);

      MfcRrNodeReadListScenario scenario = new MfcRrNodeReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * RR information acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param nodeId
   *          Node ID
   * @param userType
   *          User type
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes/rrs/{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("node_id") String nodeId,
      @QueryParam("user-type") String userType) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      RrNodeRequest request = new RrNodeRequest(null, null, null, clusterId, nodeId, null, userType);

      setCommonData(request);

      MfcRrNodeReadScenario scenario = new MfcRrNodeReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

}
