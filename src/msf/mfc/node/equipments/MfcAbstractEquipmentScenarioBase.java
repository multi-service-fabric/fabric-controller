
package msf.mfc.node.equipments;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.common.data.MfcEquipment;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcEquipmentDao;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.equipments.AbstractEquipmentScenarioBase;
import msf.mfcfc.node.equipments.data.EquipmentReadResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Abstract class to implement the common process of device model
 * information-related processing in configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class MfcAbstractEquipmentScenarioBase<T extends RestRequestBase>
    extends AbstractEquipmentScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractEquipmentScenarioBase.class);

  protected MfcEquipment getEquipment(SessionWrapper sessionWrapper, MfcEquipmentDao mfcEquipmentDao,
      Integer equipmentTypeId) throws MsfException {
    try {
      logger.methodStart();
      MfcEquipment mfcEquipment = mfcEquipmentDao.read(sessionWrapper, equipmentTypeId);
      if (mfcEquipment == null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = equipment");
      }
      return mfcEquipment;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<MfcSwCluster> getSwClusterList(SessionWrapper sessionWrapper, MfcSwClusterDao mfcSwClusterDao)
      throws MsfException {
    try {
      logger.methodStart();
      List<MfcSwCluster> mfcSwClusterList = mfcSwClusterDao.readList(sessionWrapper);
      if (mfcSwClusterList.isEmpty()) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = equipment");
      }
      return mfcSwClusterList;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendEquipmentRead(MfcSwCluster mfcSwCluster, String equipmentTypeId) throws MsfException {
    try {
      logger.methodStart();

      SwCluster swCluster = MfcConfigManager.getInstance()
          .getSystemConfSwClusterData(Integer.valueOf(mfcSwCluster.getSwClusterId())).getSwCluster();
      String fcControlAddress = swCluster.getFcControlAddress();
      int fcControlPort = swCluster.getFcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(MfcFcRequestUri.EQUIPMENT_READ.getHttpMethod(),
          MfcFcRequestUri.EQUIPMENT_READ.getUri(equipmentTypeId), null, fcControlAddress, fcControlPort);

      EquipmentReadResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
          EquipmentReadResponseBody.class, ErrorCode.FC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          responseBody.getErrorCode(), ErrorCode.FC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendEquipmentCreateDeleteRequest(SessionWrapper sessionWrapper,
      List<MfcSwCluster> mfcSwClusterList, boolean isCreateRequest, RestRequestBase restRequest, String equipmentTypeId)
      throws MsfException {
    try {
      logger.methodStart();

      List<RestRequestData> restRequestDataList = new ArrayList<RestRequestData>();

      for (MfcSwCluster mfcSwCluster : mfcSwClusterList) {

        SwCluster swCluster = MfcConfigManager.getInstance().getSystemConfSwClusterData(mfcSwCluster.getSwClusterId())
            .getSwCluster();
        String fcControlAddress = swCluster.getFcControlAddress();
        int fcControlPort = swCluster.getFcControlPort();
        if (isCreateRequest) {
          RestRequestData requestData = new RestRequestData(Integer.valueOf(mfcSwCluster.getSwClusterId()),
              fcControlAddress, fcControlPort, MfcFcRequestUri.EQUIPMENT_CREATE.getHttpMethod(),
              MfcFcRequestUri.EQUIPMENT_CREATE.getUri(), restRequest, HttpStatus.CREATED_201);
          restRequestDataList.add(requestData);
        } else {
          RestRequestData requestData = new RestRequestData(Integer.valueOf(mfcSwCluster.getSwClusterId()),
              fcControlAddress, fcControlPort, MfcFcRequestUri.EQUIPMENT_DELETE.getHttpMethod(),
              MfcFcRequestUri.EQUIPMENT_DELETE.getUri(equipmentTypeId), null, HttpStatus.NO_CONTENT_204);
          restRequestDataList.add(requestData);
        }
      }

      List<RestResponseData> restResponseDataList = sendRequest(restRequestDataList, RequestType.REQUEST);

      if (!checkRollbackRequired(restResponseDataList)) {

        if (checkResponseAllSuccess(restResponseDataList)) {

          if (isCreateRequest) {
            sessionWrapper.commit(MfcFcRequestUri.EQUIPMENT_CREATE);
          } else {
            sessionWrapper.commit(MfcFcRequestUri.EQUIPMENT_DELETE);
          }

          return restResponseDataList.get(0).getResponse();
        } else {

          return createErrorResponse(restResponseDataList, null);
        }
      } else {

        List<RestRequestData> rollbackRequestDataList = new ArrayList<RestRequestData>();

        for (RestResponseData restResponseData : restResponseDataList) {
          if (HttpStatus.isSuccess(restResponseData.getResponse().getHttpStatusCode())) {

            SwCluster swCluster = MfcConfigManager.getInstance()
                .getSystemConfSwClusterData(restResponseData.getRequest().getClusterId()).getSwCluster();
            String fcControlAddress = swCluster.getFcControlAddress();
            int fcControlPort = swCluster.getFcControlPort();

            if (isCreateRequest) {
              RestRequestData rollbackRequestData = new RestRequestData(
                  Integer.valueOf(restResponseData.getRequest().getClusterId()), fcControlAddress, fcControlPort,
                  MfcFcRequestUri.EQUIPMENT_DELETE.getHttpMethod(),
                  MfcFcRequestUri.EQUIPMENT_DELETE.getUri(equipmentTypeId), null, HttpStatus.NO_CONTENT_204);
              rollbackRequestDataList.add(rollbackRequestData);
            } else {
              RestRequestData rollbackRequestData = new RestRequestData(
                  Integer.valueOf(restResponseData.getRequest().getClusterId()), fcControlAddress, fcControlPort,
                  MfcFcRequestUri.EQUIPMENT_CREATE.getHttpMethod(), MfcFcRequestUri.EQUIPMENT_CREATE.getUri(),
                  restRequest, HttpStatus.CREATED_201);
              rollbackRequestDataList.add(rollbackRequestData);
            }
          }
        }

        List<RestResponseData> restRollbackResponseDataList = sendRequest(rollbackRequestDataList,
            RequestType.ROLLBACK);

        sessionWrapper.rollback();

        return createErrorResponse(restResponseDataList, restRollbackResponseDataList);
      }

    } finally {
      logger.methodEnd();
    }
  }
}
