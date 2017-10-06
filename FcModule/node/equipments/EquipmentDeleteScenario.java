package msf.fc.node.equipments;

import java.text.MessageFormat;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.EcRequestUri;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.Equipment;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EquipmentDao;
import msf.fc.node.NodeManager;
import msf.fc.node.equipments.data.EquipmentRequest;
import msf.fc.rest.common.JsonUtil;
import msf.fc.rest.common.RestClient;
import msf.fc.rest.ec.node.equipment.data.EquipmentDeleteEcResponseBody;

public class EquipmentDeleteScenario extends AbstractEquipmentScenarioBase<EquipmentRequest> {

  private EquipmentRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(EquipmentCreateScenario.class);

  public EquipmentDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(EquipmentRequest request) throws MsfException {
    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getEquipmentTypeId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);

      this.request = request;

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      logger.performance("start wait to equipment increasing process.");
      synchronized (NodeManager.getInstance().getEquipmentCreateLockObject()) {
        logger.performance("end wait to equipment increasing process.");
        logger.performance("start wait to equipment decreasing process.");
        synchronized (NodeManager.getInstance().getEquipmentDeleteLockObject()) {
          logger.performance("end wait to equipment decreasing process.");

          RestResponseBase responseBase = null;
          SessionWrapper sessionWrapper = new SessionWrapper();
          try {
            sessionWrapper.openSession();
            sessionWrapper.beginTransaction();
            EquipmentDao equipmentDao = new EquipmentDao();
            Equipment equipment = getEquipment(sessionWrapper, equipmentDao, request.getClusterId(),
                Integer.parseInt(request.getEquipmentTypeId()));

            checkForEquipmentRelation(equipment);

            equipmentDao.delete(sessionWrapper, equipment.getId());

            sendEquipmentDelete();

            responseBase = responseEquipmentDeleteData();

            sessionWrapper.commit();
          } catch (MsfException msfException) {
            logger.error(msfException.getMessage(), msfException);
            sessionWrapper.rollback();
            throw msfException;
          } finally {
            sessionWrapper.closeSession();
          }

          return responseBase;
        }
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkForEquipmentRelation(Equipment equipment) throws MsfException {
    try {
      logger.methodStart(new String[] { "equipment" }, new Object[] { equipment });
      ;
      if (!equipment.getNodes().isEmpty()) {
        throw new MsfException(ErrorCode.DELETE_INFORMATION_ERROR, "nodes exist related to the equipment.");
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void sendEquipmentDelete() throws MsfException {
    try {
      logger.methodStart();
      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.EQUIPMENT_DELETE.getHttpMethod(),
          EcRequestUri.EQUIPMENT_DELETE.getUri(request.getEquipmentTypeId()), null);

      if (restResponseBase.getHttpStatusCode() != HttpStatus.NO_CONTENT_204) {
        EquipmentDeleteEcResponseBody equipmentDeleteEcResponseBody = new EquipmentDeleteEcResponseBody();
        equipmentDeleteEcResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            EquipmentDeleteEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);
        String errorMsg = MessageFormat.format("HttpStatusCode = {0}, ErrorCode = {1}",
            restResponseBase.getHttpStatusCode(), equipmentDeleteEcResponseBody.getErrorCode());
        logger.error(errorMsg);
        throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseEquipmentDeleteData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

}
