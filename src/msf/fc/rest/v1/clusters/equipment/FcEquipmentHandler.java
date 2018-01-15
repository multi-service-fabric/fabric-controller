
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

import msf.fc.node.equipments.FcEquipmentCreateScenario;
import msf.fc.node.equipments.FcEquipmentDeleteScenario;
import msf.fc.node.equipments.FcEquipmentReadListScenario;
import msf.fc.node.equipments.FcEquipmentReadScenario;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.equipments.data.EquipmentRequest;
import msf.mfcfc.rest.common.AbstractRestHandler;

/**
 * Configuration management: Node type information management.
 *
 * @author NTT
 *
 */
@Path("/v1/equipment-types")

public class FcEquipmentHandler extends AbstractRestHandler {

  private static final MsfLogger logger = MsfLogger.getInstance(FcEquipmentHandler.class);

  /**
   * Device model information registration.
   *
   *
   * @param requestBody
   *          Request message body
   *
   * @return response data
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response create(String requestBody) {
    try {
      logger.methodStart();
      loggingRequestReceived();
      loggingRequestJsonBody(requestBody);

      EquipmentRequest request = new EquipmentRequest(requestBody, null, null, null, null);

      setCommonData(request);

      FcEquipmentCreateScenario scenario = new FcEquipmentCreateScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Device model list information acquisition for each SW cluster.
   *
   * @param format
   *          Information type to acquire (optional parameter)
   * @return response data
   */
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response readList(@QueryParam("format") String format) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      EquipmentRequest request = new EquipmentRequest(null, null, null, null, format);

      setCommonData(request);

      FcEquipmentReadListScenario scenario = new FcEquipmentReadListScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Device model information acquisition.
   *
   * @param equipmentTypeId
   *          Device model ID (URI parameter)
   * @return response data
   */
  @GET
  @Path("/{equipment_type_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response read(@PathParam("equipment_type_id") String equipmentTypeId) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      EquipmentRequest request = new EquipmentRequest(null, null, null, equipmentTypeId, null);

      setCommonData(request);

      FcEquipmentReadScenario scenario = new FcEquipmentReadScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

  /**
   * Device model information deletion.
   *
   * @param equipmentTypeId
   *          Device model ID (URI parameter)
   * @return response data
   */
  @DELETE
  @Path("/{equipment_type_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("equipment_type_id") String equipmentTypeId) {
    try {
      logger.methodStart();
      loggingRequestReceived();

      EquipmentRequest request = new EquipmentRequest(null, null, null, equipmentTypeId, null);

      setCommonData(request);

      FcEquipmentDeleteScenario scenario = new FcEquipmentDeleteScenario(OperationType.NORMAL,
          SystemInterfaceType.EXTERNAL);
      RestResponseBase restResponseBase = scenario.execute(request);
      return createResponse(restResponseBase);

    } finally {
      logger.methodEnd();
    }

  }

}
