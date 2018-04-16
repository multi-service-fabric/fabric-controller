
package msf.fc.core.status.scenario;

import java.util.List;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.system.NoticeDestInfoStatus;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.status.scenario.data.SystemStatusInnerNotifyRequestBody;
import msf.mfcfc.core.status.scenario.data.SystemStatusNotifyRequest;
import msf.mfcfc.core.status.scenario.data.SystemStatusNotifyRequestBody;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusControllerNotifyEntity;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Class to provide the system status update function.
 *
 * @author NTT
 *
 */
public class FcInternalSystemStatusNotifyScenario extends FcAbstractStatusScenarioBase<SystemStatusNotifyRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalSystemStatusNotifyScenario.class);

  private SystemStatusNotifyRequest request;
  private SystemStatusInnerNotifyRequestBody ecRequestBody;

  /**
   * Constructor.
   *
   * @param operationType
   *          Operation type
   * @param systemIfType
   *          System interface type
   */
  public FcInternalSystemStatusNotifyScenario(OperationType operationType, SystemInterfaceType systemIfType) {

    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected void checkParameter(SystemStatusNotifyRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      SystemStatusInnerNotifyRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          SystemStatusInnerNotifyRequestBody.class);
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
      SystemStatusControllerNotifyEntity controller = new SystemStatusControllerNotifyEntity();
      controller.setClusterId(String.valueOf(clusterId));
      controller.setControllerType(ecRequestBody.getController().getControllerType());
      controller.setEvent(ecRequestBody.getController().getEvent());

      SystemStatusNotifyRequestBody requestBody = new SystemStatusNotifyRequestBody();
      requestBody.setController(controller);
      String bodyStr = JsonUtil.toJson(requestBody);

      request.setRequestBody(bodyStr);

      int noticeRetryNum = FcConfigManager.getInstance().getSystemConfStatus().getNoticeRetryNum();
      int noticeTimeout = FcConfigManager.getInstance().getSystemConfStatus().getNoticeTimeout();
      List<NoticeDestInfoStatus> noticeDestInfoList = FcConfigManager.getInstance().getSystemConfStatus()
          .getNoticeDestInfo();

      if (noticeDestInfoList.isEmpty()) {

        logger.info("Notification destination information is null. execute skip.");
        return responsStatusNotifyData();
      }

      for (NoticeDestInfoStatus noticeDestInfo : noticeDestInfoList) {
        sendStatusNotify(noticeDestInfo.getNoticeAddress(), noticeDestInfo.getNoticePort(), noticeRetryNum,
            noticeTimeout, request);
      }

      return responsStatusNotifyData();
    } finally {
      logger.methodEnd();
    }
  }

}
