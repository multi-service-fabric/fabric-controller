
package msf.fc.rest.v1.clusters.physicalif;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.node.interfaces.physicalifs.FcPhysicalInterfaceReadListScenario;
import msf.fc.node.interfaces.physicalifs.FcPhysicalInterfaceReadScenario;
import msf.fc.node.interfaces.physicalifs.FcPhysicalInterfaceUpdateScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.interfaces.physicalifs.data.PhysicalIfRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Configuration management Interface management (physical IF).
 *
 * @author NTT
 */
@Path("/v1/clusters")
public class FcPhysicalIfHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcPhysicalIfHandler.class);

  /**
   * Physical IF information list acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Node type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param format
   *          Information type to acquire (optional parameter)
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/physical-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @QueryParam("format") String format) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      PhysicalIfRequest request = new PhysicalIfRequest(null, null, null, clusterId, fabricType, nodeId, null, format);

      setCommonData(request);

      FcPhysicalInterfaceReadListScenario scenario = new FcPhysicalInterfaceReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Physical IF information acquisition.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Node type (URI parameter)
   * @param nodeId
   *          Node ID (URI parameter)
   * @param ifId
   *          Physical IF ID (URI parameter)
   * @return response data
   */
  @GET
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/physical-ifs/{if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("if_id") String ifId) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      PhysicalIfRequest request = new PhysicalIfRequest(null, null, null, clusterId, fabricType, nodeId, ifId, null);

      setCommonData(request);

      FcPhysicalInterfaceReadScenario scenario = new FcPhysicalInterfaceReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  /**
   * Physical IF information change.
   *
   * @param clusterId
   *          Cluster ID (URI parameter)
   * @param fabricType
   *          Node type (URI parameter))
   * @param nodeId
   *          Node ID (URI parameter)
   * @param ifId
   *          Physical IF ID (URI parameter)
   * @param notificationAddress
   *          Operation completion notification address
   * @param notificationPort
   *          Operation completion notification port
   * @param requestBody
   *          Request message body
   * @return response data
   */
  @PUT
  @Path("/{cluster_id}/nodes/{fabric_type}/{node_id}/interfaces/physical-ifs/{if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("cluster_id") String clusterId, @PathParam("fabric_type") String fabricType,
      @PathParam("node_id") String nodeId, @PathParam("if_id") String ifId,
      @QueryParam("notification_address") String notificationAddress,
      @QueryParam("notification_port") String notificationPort, String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      PhysicalIfRequest request = new PhysicalIfRequest(requestBody, notificationAddress, notificationPort, clusterId,
          fabricType, nodeId, ifId, null);

      setCommonData(request);

      FcPhysicalInterfaceUpdateScenario scenario = new FcPhysicalInterfaceUpdateScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

}
