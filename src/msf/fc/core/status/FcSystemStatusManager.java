
package msf.fc.core.status;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.system.NoticeDestInfoStatus;
import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.BlockadeStatus;
import msf.mfcfc.common.constant.ControllerEvent;
import msf.mfcfc.common.constant.ControllerType;
import msf.mfcfc.common.constant.ServiceStatus;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.status.SystemStatusManager;
import msf.mfcfc.core.status.scenario.data.SystemStatusNotifyRequestBody;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusControllerNotifyEntity;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Class to manage the system status.<br>
 * Execute the initialization, acquisition and modification of the system
 * status.
 *
 * @author NTT
 *
 */
public final class FcSystemStatusManager extends SystemStatusManager {

  private static final MsfLogger logger = MsfLogger.getInstance(FcSystemStatusManager.class);

  private FcSystemStatusManager() {
  }

  /**
   * Returns the instance of FcSystemStatusManager This method does not
   * guarantee the uniqueness of the returned instance if it is called by
   * multi-threads simultaneously on the first call.<br>
   * Guarantee that this function is called by only one thread simultaneously
   * when it is called for the first time.
   *
   * @return FcSystemStatusManager instance
   */
  public static FcSystemStatusManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new FcSystemStatusManager();
      }
      return (FcSystemStatusManager) instance;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Execute the status notification process.
   *
   * @param tmpUpdateStatus
   *          System status information after modification
   */
  @Override
  public void statusNotifyProcess(SystemStatus tmpUpdateStatus) {

    String event = checkStatusNotify(tmpUpdateStatus);
    if (event != null && ConfigManager.getInstance() instanceof FcConfigManager) {
      RestRequestBase request = createRequest(event);

      for (NoticeDestInfoStatus noticeDestInfo : FcConfigManager.getInstance().getSystemConfStatus()
          .getNoticeDestInfo()) {
        sendStatusNotify(request, noticeDestInfo);
      }
    }
  }

  private String checkStatusNotify(SystemStatus tmpUpdateStatus) {

    String event = null;

    if (ServiceStatus.STARTED.getMessage().equals(status.getServiceStatusEnum().getMessage())
        && ServiceStatus.SWITCHING.getMessage().equals(tmpUpdateStatus.getServiceStatusEnum().getMessage())) {
      event = ControllerEvent.START_SYSTEM_SWITCHING.getMessage();
    } else if (ServiceStatus.SWITCHING.getMessage().equals(status.getServiceStatusEnum().getMessage())
        && ServiceStatus.STARTED.getMessage().equals(tmpUpdateStatus.getServiceStatusEnum().getMessage())) {
      event = ControllerEvent.END_SYSTEM_SWITCHING.getMessage();
    } else if (BlockadeStatus.NONE.getMessage().equals(status.getBlockadeStatusEnum().getMessage())
        && BlockadeStatus.BLOCKADE.getMessage().equals(tmpUpdateStatus.getBlockadeStatusEnum().getMessage())) {
      event = ControllerEvent.START_BLOCKADE.getMessage();
    } else if (BlockadeStatus.BLOCKADE.getMessage().equals(status.getBlockadeStatusEnum().getMessage())
        && BlockadeStatus.NONE.getMessage().equals(tmpUpdateStatus.getBlockadeStatusEnum().getMessage())) {
      event = ControllerEvent.END_BLOCKADE.getMessage();
    }
    return event;
  }

  private RestRequestBase createRequest(String event) {

    SystemStatusControllerNotifyEntity controller = new SystemStatusControllerNotifyEntity();
    String clusterId = String
        .valueOf(FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId());
    controller.setClusterId(clusterId);
    controller.setControllerType(ControllerType.FC.getMessage());
    controller.setEvent(event);

    SystemStatusNotifyRequestBody requestBody = new SystemStatusNotifyRequestBody();
    requestBody.setController(controller);

    RestRequestBase requestBase = new RestRequestBase();
    requestBase.setRequestBody(JsonUtil.toJson(requestBody));

    return requestBase;
  }

  private void sendStatusNotify(RestRequestBase request, NoticeDestInfoStatus noticeDestInfo) {

    int noticeRetryNum = FcConfigManager.getInstance().getSystemConfStatus().getNoticeRetryNum();
    int noticeTimeout = FcConfigManager.getInstance().getSystemConfStatus().getNoticeTimeout();

    for (int retryNum = 0; retryNum < noticeRetryNum; retryNum++) {
      if (sendStatus(request, noticeDestInfo.getNoticeAddress(), noticeDestInfo.getNoticePort(),
          noticeTimeout) != null) {
        break;
      }
    }
  }

}
