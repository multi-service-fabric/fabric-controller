
package msf.mfc.node.equipments;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.common.data.MfcEquipment;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcEquipmentDao;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
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
import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for device model information list acquisition.
 *
 * @author NTT
 *
 */
public class MfcEquipmentReadListScenario extends MfcAbstractEquipmentScenarioBase<EquipmentRequest> {

  private EquipmentRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(MfcEquipmentReadScenario.class);

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
  public MfcEquipmentReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

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
        MfcEquipmentDao mfcEquipmentDao = new MfcEquipmentDao();

        List<MfcEquipment> mfcEquipments = mfcEquipmentDao.readList(sessionWrapper);

        MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();
        List<MfcSwCluster> mfcSwClusterList = getSwClusterList(sessionWrapper, mfcSwClusterDao);

        responseBase = responseEquipmentReadListData(mfcEquipments, mfcSwClusterList);
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

  private RestResponseBase responseEquipmentReadListData(List<MfcEquipment> mfcEquipments,
      List<MfcSwCluster> mfcSwClusterList) throws MsfException {
    try {
      logger.methodStart();

      RestResponseBase responseBase = null;

      if (RestFormatOption.DETAIL_LIST.getMessage().equals(request.getFormat())) {
        if (CollectionUtils.isNotEmpty(mfcEquipments)) {

          responseBase = sendEquipmentReadList(mfcSwClusterList.get(0),
              RestFormatOption.DETAIL_LIST.equals(request.getFormatEnum()));
        } else {

          EquipmentReadDetailListResponseBody body = new EquipmentReadDetailListResponseBody();
          body.setEquipmentTypeList(new ArrayList<>());
          responseBase = createRestResponse(body, HttpStatus.OK_200);
        }
      } else {
        EquipmentReadListResponseBody body = new EquipmentReadListResponseBody();
        body.setEquipmentTypeIdList(getEquipmentTypeIds(mfcEquipments));
        responseBase = createRestResponse(body, HttpStatus.OK_200);
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendEquipmentReadList(MfcSwCluster mfcSwCluster, boolean isDetailList)
      throws MsfException {
    try {
      logger.methodStart();

      SwCluster swCluster = MfcConfigManager.getInstance()
          .getSystemConfSwClusterData(Integer.valueOf(mfcSwCluster.getSwClusterId())).getSwCluster();
      String fcControlAddress = swCluster.getFcControlAddress();
      int fcControlPort = swCluster.getFcControlPort();

      String targetUri = MfcFcRequestUri.EQUIPMENT_READ_LIST.getUri();
      if (isDetailList) {
        targetUri = targetUri + "?format=" + RestFormatOption.DETAIL_LIST.getMessage();
      }

      RestResponseBase restResponseBase = RestClient.sendRequest(MfcFcRequestUri.EQUIPMENT_READ_LIST.getHttpMethod(),
          targetUri, null, fcControlAddress, fcControlPort);

      String errorCode = null;
      if (isDetailList) {
        EquipmentReadDetailListResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            EquipmentReadDetailListResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
        errorCode = responseBody.getErrorCode();
      } else {
        EquipmentReadListResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            EquipmentReadListResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
        errorCode = responseBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, errorCode,
          ErrorCode.FC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private List<String> getEquipmentTypeIds(List<MfcEquipment> equipments) {
    try {
      logger.methodStart();
      List<String> equipmentTypeIds = new ArrayList<>();
      for (MfcEquipment equipment : equipments) {
        equipmentTypeIds.add(String.valueOf(equipment.getEquipmentTypeInfoId()));
      }
      return equipmentTypeIds;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body" }, new Object[] { ToStringBuilder.reflectionToString(body) });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }
}
