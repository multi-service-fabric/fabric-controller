
package msf.fc.rest.v1.internal.clusters;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.node.nodes.FcInternalNodeNotifyScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.nodes.data.InternalNodeRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Configuration management.
 *
 * @author NTT
 */
@Path("/v1/internal/nodes")
public class FcInternalNodeHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalNodeHandler.class);

  /**
   * Node addition completion notification.
   *
   * @param nodeId
   *          Node ID (URI parameter)
   * @param requestBody
   *          Request message body
   * @return response data
   */
  @PUT
  @Path("/{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response notify(@PathParam("node_id") String nodeId, String requestBody) {
    try {
      logger.methodStart();

      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      InternalNodeRequest request = new InternalNodeRequest(requestBody, null, null, nodeId);

      setCommonData(request);

      FcInternalNodeNotifyScenario scenario = new FcInternalNodeNotifyScenario(OperationType.NORMAL,
          SystemInterfaceType.INTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

}
