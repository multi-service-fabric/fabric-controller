
package msf.fc.node.equipments;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.data.FcEquipment;
import msf.fc.db.dao.clusters.FcEquipmentDao;
import msf.fc.rest.ec.node.equipment.data.EquipmentReadListEcResponseBody;
import msf.fc.rest.ec.node.equipment.data.entity.EquipmentEcEntity;
import msf.mfcfc.common.constant.EcRequestUri;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.equipments.data.EquipmentReadDetailListResponseBody;
import msf.mfcfc.node.equipments.data.EquipmentReadListResponseBody;
import msf.mfcfc.node.equipments.data.EquipmentRequest;
import msf.mfcfc.node.equipments.data.entity.EquipmentTypeEntity;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for device model information list acquisition.
 *
 * @author NTT
 *
 */
public class FcEquipmentReadListScenario extends FcAbstractEquipmentScenarioBase<EquipmentRequest> {

  private EquipmentRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcEquipmentReadListScenario.class);

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public FcEquipmentReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(EquipmentRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      if (request.getFormat() != null) {
        ParameterCheckUtil.checkNotNull(request.getFormatEnum());
      }

      this.request = request;

    } finally {
      logger.methodEnd();
    }

  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {

    try {
      logger.methodStart();

      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();
        FcEquipmentDao fcEquipmentDao = new FcEquipmentDao();

        List<FcEquipment> equipments = fcEquipmentDao.readList(sessionWrapper);

        responseBase = responseEquipmentReadListData(equipments, request.getFormat());

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }

  }

  private RestResponseBase responseEquipmentReadListData(List<FcEquipment> equipments, String format)
      throws MsfException {
    try {
      logger.methodStart();
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        EquipmentReadDetailListResponseBody body = new EquipmentReadDetailListResponseBody();

        if (CollectionUtils.isNotEmpty(equipments)) {
          EquipmentReadListEcResponseBody equipmentReadListEcResponseBody = sendEquipmentReadList();
          body.setEquipmentTypeList(getEquipmentTypes(equipments, equipmentReadListEcResponseBody));
        } else {

          body.setEquipmentTypeList(new ArrayList<>());
        }
        return createRestResponse(body, HttpStatus.OK_200);
      } else {
        EquipmentReadListResponseBody body = new EquipmentReadListResponseBody();
        body.setEquipmentTypeIdList(getEquipmentTypeIds(equipments));
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private EquipmentReadListEcResponseBody sendEquipmentReadList() throws MsfException {
    try {
      logger.methodStart();

      String ecControlIpAddress = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster()
          .getEcControlAddress();

      int ecControlPort = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getEcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(EcRequestUri.EQUIPMENT_READ_LIST.getHttpMethod(),
          EcRequestUri.EQUIPMENT_READ_LIST.getUri(), null, ecControlIpAddress, ecControlPort);

      EquipmentReadListEcResponseBody equipmentReadListEcResponseBody = JsonUtil.fromJson(
          restResponseBase.getResponseBody(), EquipmentReadListEcResponseBody.class, ErrorCode.EC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          equipmentReadListEcResponseBody.getErrorCode(), ErrorCode.EC_CONTROL_ERROR);

      return equipmentReadListEcResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private List<EquipmentTypeEntity> getEquipmentTypes(List<FcEquipment> equipments,
      EquipmentReadListEcResponseBody body) throws MsfException {
    try {
      logger.methodStart();
      List<EquipmentTypeEntity> equipmentTypes = new ArrayList<>();
      List<EquipmentEcEntity> equipmentEcList = body.getEquipmentList();
      boolean isExist;
      for (FcEquipment fcEquipment : equipments) {
        isExist = false;
        String equipmentTypeId = String.valueOf(fcEquipment.getEquipmentTypeId());
        for (EquipmentEcEntity equipmentEc : equipmentEcList) {
          if (equipmentTypeId.equals(equipmentEc.getEquipmentTypeId())) {
            equipmentTypes.add(getEquipmentType(fcEquipment, equipmentEc));
            isExist = true;
            break;
          }
        }
        if (!isExist) {

          throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is only the data in the FC system.");
        }
      }
      if (equipmentEcList.size() != equipmentTypes.size()) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND, "There is only the data in the EC system.");
      }
      return equipmentTypes;
    } finally {
      logger.methodEnd();
    }
  }

  private List<String> getEquipmentTypeIds(List<FcEquipment> equipments) {
    try {
      logger.methodStart();
      List<String> equipmentTypeIds = new ArrayList<>();
      for (FcEquipment equipment : equipments) {
        equipmentTypeIds.add(String.valueOf(equipment.getEquipmentTypeId()));
      }
      return equipmentTypeIds;
    } finally {
      logger.methodEnd();
    }
  }

}
