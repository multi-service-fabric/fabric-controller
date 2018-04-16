
package msf.mfc.failure.status;

import java.util.List;
import java.util.Map;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.Failure;
import msf.mfc.common.config.type.system.NoticeDestInfoFailure;
import msf.mfcfc.common.constant.ClusterType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.failure.status.data.FailureStatusNotifyRequestBody;
import msf.mfcfc.failure.status.data.FailureStatusReadListResponseBody;
import msf.mfcfc.failure.status.data.FailureStatusRequest;
import msf.mfcfc.failure.status.data.entity.FailureStatusClusterFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusIfFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusNodeFailureEntity;
import msf.mfcfc.failure.status.data.entity.FailureStatusSliceUnitEntity;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for failure information notification.
 *
 * @author NTT
 *
 */
public class MfcFailureStatusNotifyScenario extends MfcAbstractFailureStatusScenarioBase<FailureStatusRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcFailureStatusNotifyScenario.class);

  private FailureStatusNotifyRequestBody requestBody;

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
  public MfcFailureStatusNotifyScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(FailureStatusRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      FailureStatusNotifyRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          FailureStatusNotifyRequestBody.class);

      requestBody.validate();

      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }

  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();

        Failure systemConfFailure = MfcConfigManager.getInstance().getSystemConfFailure();

        if (systemConfFailure.getNoticeDestInfo().isEmpty()) {

          logger.info("Notification destination information is empty. execute skip.");
          return responseFailureNotifyData();
        }

        boolean isSlice = false;
        for (NoticeDestInfoFailure info : systemConfFailure.getNoticeDestInfo()) {
          isSlice |= info.isIsSliceUnit();
        }

        FailureStatusSliceUnitEntity failureStatusSliceUnitEntity = null;

        if (isSlice) {

          if (checkNeedToGetFailureStatusList()) {

            List<RestResponseData> restResponseDataList = sendFailureReadListRequestForFc(sessionWrapper);

            if (checkResponseAllSuccess(restResponseDataList)) {

              Map<Integer, FailureStatusReadListResponseBody> failureStatusFromFcMap = createFailureStatusFromFcMap(
                  restResponseDataList);
              failureStatusSliceUnitEntity = createAllSliceNotifyEntity(sessionWrapper, failureStatusFromFcMap,
                  requestBody);
            } else {

              return createErrorResponse(restResponseDataList, null);
            }
          }
        }

        int retry = systemConfFailure.getNoticeRetryNum();
        int timeout = systemConfFailure.getNoticeTimeout();
        noticeFailureInfo(systemConfFailure.getNoticeDestInfo(), requestBody, failureStatusSliceUnitEntity, retry,
            timeout);

        return responseFailureNotifyData();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseFailureNotifyData() {
    return new RestResponseBase(HttpStatus.OK_200, (String) null);
  }

  private boolean checkNeedToGetFailureStatusList() {
    try {
      logger.methodStart();

      if (requestBody.getSliceUnit() != null) {
        return true;
      }

      if (requestBody.getClusterUnit() != null) {
        for (FailureStatusClusterFailureEntity clusterFailure : requestBody.getClusterUnit().getClusterList()) {
          if (clusterFailure.getClusterTypeEnum().equals(ClusterType.CLUSTER_LINK_IF)) {
            return true;
          }
        }
      }

      return false;
    } finally {
      logger.methodEnd();
    }
  }

  private void noticeFailureInfo(List<NoticeDestInfoFailure> noticeDestInfoList,
      FailureStatusNotifyRequestBody notifyRequestBody, FailureStatusSliceUnitEntity sliceUnitEntity, int retry,
      int timeout) {
    for (NoticeDestInfoFailure destInfo : noticeDestInfoList) {
      RestRequestBase request = createRequest(destInfo, notifyRequestBody, sliceUnitEntity);
      if (request == null) {

        logger.debug("no data for notify. dest = {0}", destInfo);
        continue;
      }
      notifyFailureInfo(request, destInfo.getNoticeAddress(), destInfo.getNoticePort(), timeout, retry);
    }
  }

  private RestRequestBase createRequest(NoticeDestInfoFailure destInfo,
      FailureStatusNotifyRequestBody notifyRequestBody, FailureStatusSliceUnitEntity sliceUnitEntity) {
    try {
      logger.methodStart(new String[] { "destInfo", "notifyRequestBody", "sliceUnitEntity" },
          new Object[] { destInfo, notifyRequestBody, sliceUnitEntity });

      boolean isNotice = false;
      FailureStatusNotifyRequestBody request = new FailureStatusNotifyRequestBody();

      if (destInfo.isIsPhysicalUnit()) {
        if (notifyRequestBody.getPhysicalUnit() != null) {
          List<FailureStatusIfFailureEntity> ifList = notifyRequestBody.getPhysicalUnit().getIfList();
          List<FailureStatusNodeFailureEntity> nodeList = notifyRequestBody.getPhysicalUnit().getNodeList();
          if ((ifList != null && !ifList.isEmpty()) || (nodeList != null && !nodeList.isEmpty())) {
            isNotice = true;
          }
        }
        request.setPhysicalUnit(notifyRequestBody.getPhysicalUnit());
      }

      if (destInfo.isIsClusterUnit()) {
        if (notifyRequestBody.getClusterUnit() != null) {
          if (notifyRequestBody.getClusterUnit().getClusterList() != null
              && !notifyRequestBody.getClusterUnit().getClusterList().isEmpty()) {
            isNotice = true;
          }
        }
        request.setClusterUnit(notifyRequestBody.getClusterUnit());
      }

      if (destInfo.isIsSliceUnit()) {
        if (sliceUnitEntity != null) {
          if ((sliceUnitEntity.getSliceList() != null && !sliceUnitEntity.getSliceList().isEmpty())
              || (sliceUnitEntity.getClusterLink() != null)) {
            isNotice = true;
          }
        }
        request.setSliceUnit(sliceUnitEntity);
      }

      RestRequestBase requestBase = null;
      if (isNotice) {
        requestBase = new RestRequestBase();
        requestBase.setRequestBody(JsonUtil.toJson(request));
      }
      return requestBase;
    } finally {
      logger.methodEnd();
      ;
    }
  }
}
