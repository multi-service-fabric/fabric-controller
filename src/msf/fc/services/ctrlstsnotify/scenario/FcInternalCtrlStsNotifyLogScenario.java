
package msf.fc.services.ctrlstsnotify.scenario;

import java.util.List;

import msf.fc.common.config.FcConfigManager;
import msf.fc.services.ctrlstsnotify.FcCtrlStsNotifyManager;
import msf.fc.services.ctrlstsnotify.common.config.type.system.NoticeDestInfoLog;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.services.ctrlstsnotify.CtrlStsNotifySender;
import msf.mfcfc.services.ctrlstsnotify.common.constant.MfcFcRequestUri;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.ControllerLogInnerNotificationRequestBody;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.ControllerLogNotificationRequest;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.ControllerLogNotificationRequestBody;
import msf.mfcfc.services.ctrlstsnotify.scenario.data.entity.ControllerLogNotificationEntity;

/**
 * Class to provide the controller log notification function.
 *
 * @author NTT
 *
 */
public class FcInternalCtrlStsNotifyLogScenario
    extends FcAbstractCtrlStsNotifyScenarioBase<ControllerLogNotificationRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalCtrlStsNotifyLogScenario.class);

  private ControllerLogNotificationRequest request;
  private ControllerLogInnerNotificationRequestBody ecRequestBody;

  /**
   * Constructor.
   *
   * @param operationType
   *          Operation type
   * @param systemIfType
   *          System interface type
   */
  public FcInternalCtrlStsNotifyLogScenario(OperationType operationType, SystemInterfaceType systemIfType) {

    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected void checkParameter(ControllerLogNotificationRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ControllerLogInnerNotificationRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          ControllerLogInnerNotificationRequestBody.class);
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
      ControllerLogNotificationEntity controller = new ControllerLogNotificationEntity();
      controller.setClusterId(String.valueOf(clusterId));
      controller.setControllerType(ecRequestBody.getController().getControllerType());
      controller.setLogLevel(ecRequestBody.getController().getLogLevel());
      controller.setLogList(ecRequestBody.getController().getLogList());

      ControllerLogNotificationRequestBody requestBody = new ControllerLogNotificationRequestBody();
      requestBody.setController(controller);
      String bodyStr = JsonUtil.toJson(requestBody);

      request.setRequestBody(bodyStr);

      int noticeRetryNum = FcCtrlStsNotifyManager.getInstance().getSystemConfData().getLogNotification()
          .getNoticeRetryNum();
      int noticeTimeout = FcCtrlStsNotifyManager.getInstance().getSystemConfData().getLogNotification()
          .getNoticeTimeout();
      List<NoticeDestInfoLog> noticeDestInfoList = FcCtrlStsNotifyManager.getInstance().getSystemConfData()
          .getLogNotification().getNoticeDestInfo();

      if (noticeDestInfoList.isEmpty()) {

        logger.info("Notification destination information is null. execute skip.");
        return responseNotifyData();
      }

      for (NoticeDestInfoLog noticeDestInfo : noticeDestInfoList) {
        CtrlStsNotifySender.sendNotify(noticeDestInfo.getNoticeAddress(), noticeDestInfo.getNoticePort(),
            noticeRetryNum, noticeTimeout, request, MfcFcRequestUri.NOTIFY_CONTROLLER_LOG);
      }

      return responseNotifyData();
    } finally {
      logger.methodEnd();
    }
  }

}
