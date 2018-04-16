
package msf.mfc.core.status.scenario;

import java.util.List;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.NoticeDestInfoStatus;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.status.scenario.data.SystemStatusNotifyRequest;
import msf.mfcfc.core.status.scenario.data.SystemStatusNotifyRequestBody;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Class to provide the system status update function.
 *
 * @author NTT
 *
 */
public class MfcInternalSystemStatusNotifyScenario extends MfcAbstractStatusScenarioBase<SystemStatusNotifyRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcInternalSystemStatusNotifyScenario.class);

  private SystemStatusNotifyRequest request;
  private SystemStatusNotifyRequestBody requestBody;

  /**
   * Constructor.
   *
   * @param operationType
   *          Operation type
   * @param systemIfType
   *          System interface type
   */
  public MfcInternalSystemStatusNotifyScenario(OperationType operationType, SystemInterfaceType systemIfType) {

    this.syncType = SynchronousType.SYNC;

    this.operationType = operationType;
    this.systemIfType = systemIfType;
  }

  @Override
  protected void checkParameter(SystemStatusNotifyRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      SystemStatusNotifyRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          SystemStatusNotifyRequestBody.class);
      requestBody.validate();
      logger.debug("requestBody=" + request.getRequestBody());

      this.request = request;
      this.requestBody = requestBody;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      int noticeRetryNum = MfcConfigManager.getInstance().getSystemConfStatus().getNoticeRetryNum();
      int noticeTimeout = MfcConfigManager.getInstance().getSystemConfStatus().getNoticeTimeout();
      List<NoticeDestInfoStatus> noticeDestInfoList = MfcConfigManager.getInstance().getSystemConfStatus()
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
