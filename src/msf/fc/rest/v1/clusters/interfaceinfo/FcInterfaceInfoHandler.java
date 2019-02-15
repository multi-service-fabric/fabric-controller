
package msf.fc.rest.v1.clusters.interfaceinfo;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.node.interfaces.FcInterfaceReadListScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.interfaces.data.InterfaceRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Configuration management Interface information.
 *
 * @author NTT
 */
@Path("/v1/clusters")
public class FcInterfaceInfoHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInterfaceInfoHandler.class);

  /**
   * IF information list acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Fabric type (URI parameter)
   * @param nodeId
   *          Node ID
   * @param format
   *          Information type to acquire (optional parameter)
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @QueryParam("format") String format) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      InterfaceRequest request = new InterfaceRequest(null, null, null, clusterId, fabricType, nodeId, format);

      setCommonData(request);

      FcInterfaceReadListScenario scenario = new FcInterfaceReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }
}
