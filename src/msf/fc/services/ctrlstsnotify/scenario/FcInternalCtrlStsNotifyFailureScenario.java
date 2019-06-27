
package msf.fc.services.ctrlstsnotify.scenario;

import java.util.List;

import msf.fc.common.config.FcConfigManager;
import msf.fc.services.ctrlstsnotify.FcCtrlStsNotifyManager;
import msf.fc.services.ctrlstsnotify.common.config.type.system.NoticeDestInfoCtlFailure;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.services.ctrlstsnotify.CtrlStsNotifySender;
import msf.mfcfc.services.ctrlstsnotify.common.constant.MfcFcRequestUri;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.ControllerErrorInnerNotificationRequestBody;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.ControllerErrorNotificationRequest;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.ControllerErrorNotificationRequestBody;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.entity.ControllerErrorNotificationEntity;

/**
 * Class to provide the controller failure notification function.
 *
 * @author NTT
 *
 */
public class FcInternalCtrlStsNotifyFailureScenario
    extends FcAbstractCtrlStsNotifyScenarioBase<ControllerErrorNotificationRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalCtrlStsNotifyFailureScenario.class);

  private ControllerErrorNotificationRequest request;
  private ControllerErrorInnerNotificationRequestBody ecRequestBody;

  /**
   * Constructor.
   *
   * @param operationType
   *          Operation type
   * @param systemIfType
   *          System interface type
   */
  public FcInternalCtrlStsNotifyFailureScenario(OperationType operationType, SystemInterfaceType systemIfType) {

    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected void checkParameter(ControllerErrorNotificationRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ControllerErrorInnerNotificationRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          ControllerErrorInnerNotificationRequestBody.class);
      requestBody.validate();
      logger.debug("requestBody=" + request.getRequestBody());

      this.request = request;
      this.ecRequestBody = requestBody;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      int clusterId = FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId();
      ControllerErrorNotificationEntity controller = new ControllerErrorNotificationEntity();
      controller.setClusterId(String.valueOf(clusterId));
      controller.setControllerType(ecRequestBody.getController().getControllerType());
      controller.setSystemType(ecRequestBody.getController().getSystemType());
      controller.setFailureInfo(ecRequestBody.getController().getFailureInfo());

      ControllerErrorNotificationRequestBody requestBody = new ControllerErrorNotificationRequestBody();
      requestBody.setController(controller);
      String bodyStr = JsonUtil.toJson(requestBody);

      request.setRequestBody(bodyStr);

      int noticeRetryNum = FcCtrlStsNotifyManager.getInstance().getSystemConfData().getControllerFailureNotification()
          .getNoticeRetryNum();
      int noticeTimeout = FcCtrlStsNotifyManager.getInstance().getSystemConfData().getControllerFailureNotification()
          .getNoticeTimeout();
      List<NoticeDestInfoCtlFailure> noticeDestInfoList = FcCtrlStsNotifyManager.getInstance().getSystemConfData()
          .getControllerFailureNotification().getNoticeDestInfo();

      if (noticeDestInfoList.isEmpty()) {

        logger.info("Notification destination information is null. execute skip.");
        return responseNotifyData();
      }

      for (NoticeDestInfoCtlFailure noticeDestInfo : noticeDestInfoList) {
        CtrlStsNotifySender.sendNotify(noticeDestInfo.getNoticeAddress(), noticeDestInfo.getNoticePort(),
            noticeRetryNum, noticeTimeout, request, MfcFcRequestUri.NOTEIFY_CONTOROLLER_FAILURE);
      }

      return responseNotifyData();
    } finally {
      logger.methodEnd();
    }
  }

}
