package msf.fc.rest.v1.clusters.swcluster;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.node.clusters.ClusterReadListScenario;
import msf.fc.node.clusters.ClusterReadScenario;
import msf.fc.node.clusters.data.SwClusterRequest;
import msf.fc.rest.common.AbstractRestHandler;

@Path("/v1/clusters")
public class SwClusterHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(SwClusterHandler.class);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response readList(@QueryParam("format") String format, @QueryParam("user-type") String userType) {
    try {
      logger.methodStart(new String[] { "format", "userType" }, new Object[] { format, userType });

      loggingRequestReceived();

      SwClusterRequest request = new SwClusterRequest();
      request.setFormat(format);
      request.setUserType(userType);
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      ClusterReadListScenario scenario = new ClusterReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  @GET
  @Path("/{cluster_id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId, @QueryParam("user-type") String userType) {
    try {
      logger.methodStart(new String[] { "clusterId", "userType" }, new Object[] { clusterId, userType });
      loggingRequestReceived();

      SwClusterRequest request = new SwClusterRequest();

      request.setClusterId(clusterId);
      request.setUserType(userType);
      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      ClusterReadScenario scenario = new ClusterReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }
}
