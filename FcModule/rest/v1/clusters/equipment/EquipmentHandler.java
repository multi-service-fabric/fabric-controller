package msf.fc.rest.v1.clusters.equipment;

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

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.node.equipments.EquipmentCreateScenario;
import msf.fc.node.equipments.EquipmentDeleteScenario;
import msf.fc.node.equipments.EquipmentReadListScenario;
import msf.fc.node.equipments.EquipmentReadScenario;
import msf.fc.node.equipments.data.EquipmentRequest;
import msf.fc.rest.common.AbstractRestHandler;


@Path("/v1/clusters")

public class EquipmentHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(EquipmentHandler.class);

  @POST
  @Path("/{cluster_id}/equipment-types")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)

  public Response create(@PathParam("cluster_id") String clusterId, String requestBody) {

    try {
      logger.methodStart(new String[] { "clusterId" }, new Object[] { clusterId });
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      EquipmentRequest request = new EquipmentRequest();
      request.setClusterId(clusterId);
      request.setRequestBody(requestBody);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      EquipmentCreateScenario scenario = new EquipmentCreateScenario(OperationType.NORMAL,
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
  @Path("/{cluster_id}/equipment-types")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response readList(@PathParam("cluster_id") String clusterId, @QueryParam("format") String format) {

    try {
      logger.methodStart(new String[] { "clusterId", "format" }, new Object[] { clusterId, format });

      loggingRequestReceived();

      EquipmentRequest request = new EquipmentRequest();

      request.setClusterId(clusterId);
      request.setFormat(format);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      EquipmentReadListScenario scenario = new EquipmentReadListScenario(OperationType.NORMAL,
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
  @Path("/{cluster_id}/equipment-types/{equipment_type_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("cluster_id") String clusterId,
      @PathParam("equipment_type_id") String equipmentTypeId) {
    try {
      logger.methodStart(new String[] { "clusterId", "equipmeny_type_id" },
          new Object[] { clusterId, equipmentTypeId });

      loggingRequestReceived();

      EquipmentRequest request = new EquipmentRequest();
      request.setClusterId(clusterId);
      request.setEquipmentTypeId(equipmentTypeId);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      EquipmentReadScenario scenario = new EquipmentReadScenario(OperationType.NORMAL, SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }


  @DELETE
  @Path("/{cluster_id}/equipment-types/{equipment_type_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("cluster_id") String clusterId,
      @PathParam("equipment_type_id") String equipmentTypeId) {

    try {
      logger.methodStart(new String[] { "clusterId", "equipmentTypeId" }, new Object[] { clusterId, equipmentTypeId });

      loggingRequestReceived();

      EquipmentRequest request = new EquipmentRequest();
      request.setClusterId(clusterId);
      request.setEquipmentTypeId(equipmentTypeId);

      request.setRequestUri(getReuqestUri());
      request.setRequestMethod(getHttpMethod());

      EquipmentDeleteScenario scenario = new EquipmentDeleteScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);

      RestResponseBase restResponseBase = scenario.execute(request);

      loggingResponseJsonBody(restResponseBase.getResponseBody());
      loggingReturnedResponse(restResponseBase.getHttpStatusCode());

      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

}
