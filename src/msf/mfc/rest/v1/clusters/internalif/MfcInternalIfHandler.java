
package msf.mfc.rest.v1.clusters.internalif;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.mfc.node.interfaces.internalifs.MfcInternalInterfaceReadListScenario;
import msf.mfc.node.interfaces.internalifs.MfcInternalInterfaceReadScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.interfaces.internalifs.data.InternalIfRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Configuration management: Interface management (Intra-cluster link IF).
 *
 * @author NTT
 */
@Path("/v1/clusters")
public class MfcInternalIfHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcInternalIfHandler.class);

  /**
   * Intra-cluster IF information list acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Node type (URI parameter)
   * @param nodeId
   *          Node ID
   * @param format
   *          Information type to acquire (optional parameter)
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/internal-link-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @QueryParam("format") String format) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      InternalIfRequest request = new InternalIfRequest(null, null, null, clusterId, fabricType, nodeId, null, format);

      setCommonData(request);

      MfcInternalInterfaceReadListScenario scenario = new MfcInternalInterfaceReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Intra-cluster IF information acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Node type (URI parameter)
   * @param nodeId
   *          Node ID
   * @param internalLinkIfId
   *          Intra-cluster link IF ID
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/internal-link-ifs/{internal_link_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("internal_link_if_id") String internalLinkIfId) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      InternalIfRequest request = new InternalIfRequest(null, null, null, clusterId, fabricType, nodeId,
          internalLinkIfId, null);

      setCommonData(request);

      MfcInternalInterfaceReadScenario scenario = new MfcInternalInterfaceReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }
}
