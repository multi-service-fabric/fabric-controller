
package msf.mfcfc.core.status;

import java.util.concurrent.TimeUnit;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.system.NoticeDestInfoStatus;
import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.BlockadeStatus;
import msf.mfcfc.common.constant.ControllerEvent;
import msf.mfcfc.common.constant.ControllerType;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.ServiceStatus;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.status.scenario.data.SystemStatusNotifyRequestBody;
import msf.mfcfc.core.status.scenario.data.entity.SystemStatusControllerNotifyEntity;
import msf.mfcfc.db.DbManager;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.db.dao.common.SystemStatusDao;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Class to manage the system status. <br>
 * Execute the initialization, acquisition and change of the system status.
 *
 * @author NTT
 *
 */
public final class SystemStatusManager {

  private static final SystemStatusManager instance = new SystemStatusManager();

  private SystemStatus status;

  public static final Integer FIXED_SYSTEM_ID = 1;

  private static final MsfLogger logger = MsfLogger.getInstance(SystemStatusManager.class);

  private SystemStatusManager() {

  }

  /**
   * Initialize SystemStatusManager and return instance.
   *
   * @return SystemStatusManager instance
   */
  public static SystemStatusManager getInstance() {
    return instance;
  }

  /**
   * Return the system status held on memory.
   *
   * @return system status
   */
  public SystemStatus getSystemStatus() {
    synchronized (this) {
      try {
        logger.methodStart();
        SystemStatus retStatus = new SystemStatus();
        retStatus.setSystemId(status.getSystemId());
        retStatus.setServiceStatus(status.getServiceStatus());
        retStatus.setBlockadeStatus(status.getBlockadeStatus());
        return retStatus;
      } finally {
        logger.methodEnd();
      }
    }
  }

  /**
   * Get the system status from DB and update the system status held on memory.
   * This method must be called only once at the time of initialization.
   *
   * @return system status
   * @throws MsfException
   *           If DB control error occurs, or the record acquired from DB is not
   *           applicable (null)
   */
  public SystemStatus initStatus() throws MsfException {
    try {
      logger.methodStart();

      SessionWrapper sessionWrapper = new SessionWrapper();
      SystemStatusDao ssDao = DbManager.getInstance().createSystemStatusDao();
      SystemStatus ret = null;

      logger.performance("Start wait to read system_status table.");
      synchronized (this) {
        logger.performance("End wait to read system_status table.");
        try {
          sessionWrapper.openSession();

          ret = ssDao.read(sessionWrapper, FIXED_SYSTEM_ID);
          if (ret == null) {
            String message = "There is no record in system_status table where system_id=1.";
            logger.debug(message);
            throw new MsfException(ErrorCode.UNDEFINED_ERROR, message);
          }
          status = ret;

        } catch (MsfException ex) {
          logger.debug("DB control failed.", ex);
          throw ex;
        } finally {
          sessionWrapper.closeSession();
        }
      }

      logger.debug("Init system status.(systemId = {0}, blockadeStatus={1}, serviceStatus={2}; )", ret.getSystemId(),
          ret.getBlockadeStatus(), ret.getServiceStatus());
      return ret;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Update to the specified system status. <br>
   * Setting null is not available to the parameter updateStatus. <br>
   * Setting null is available to the member variables "service startup status"
   * "blocked status" of parameter updateStatus (SystemStatus). Do not update
   * the current status if specified null. However, either "service startup
   * status" or "blocked status" must be specified. (Setting null for both is
   * not available)
   *
   * @param updateStatus
   *          System status after update
   * @throws MsfException
   *           If updateStatus is specified null, or both "service startup
   *           status" and "blocked status" are specified null, or DB control
   *           error occurred, or an error of conditions for status transition
   *           occurred.
   */
  public void changeSystemStatus(SystemStatus updateStatus) throws MsfException {
    try {
      logger.methodStart(new String[] { "updateStatus" }, new Object[] { updateStatus });
      if (logger.isDebugEnabled()) {
        logger.debug("Current SystemStatus={0} .", status);
      }

      if (updateStatus == null) {
        String message = "Input parameter updateStatus is null.";
        logger.warn(message);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, message);
      }

      if (updateStatus.getServiceStatus() == null && updateStatus.getBlockadeStatus() == null) {
        String message = "Input parameter serviceStatus and blockadeStatus is null.";
        logger.warn(message);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, message);
      }

      if (status.getServiceStatus().equals(updateStatus.getServiceStatus())
          && status.getBlockadeStatus().equals(updateStatus.getBlockadeStatus())) {
        logger.warn("Argument status is the same as current status.");
        return;
      }

      SystemStatus tmpUpdateStatus = new SystemStatus();
      tmpUpdateStatus.setSystemId(FIXED_SYSTEM_ID);
      tmpUpdateStatus.setServiceStatus(updateStatus.getServiceStatus());
      tmpUpdateStatus.setBlockadeStatus(updateStatus.getBlockadeStatus());

      logger.performance("Start wait to update system_status table.");
      synchronized (this) {
        logger.performance("End wait to update system_status table.");

        if (tmpUpdateStatus.getServiceStatus() == null) {
          tmpUpdateStatus.setServiceStatus(status.getServiceStatus());
        }
        if (tmpUpdateStatus.getBlockadeStatus() == null) {
          tmpUpdateStatus.setBlockadeStatus(status.getBlockadeStatus());
        }

        if (!canUpdateStatus(status, tmpUpdateStatus)) {
          String message = "Input parameter can not update system status.";
          logger.warn(message);
          throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, message);
        }

        SessionWrapper sessionWrapper = new SessionWrapper();
        SystemStatusDao ssDao = DbManager.getInstance().createSystemStatusDao();

        try {
          sessionWrapper.openSession();
          sessionWrapper.beginTransaction();

          logger.debug("Update SystemStatus={0}.", tmpUpdateStatus);

          ssDao.update(sessionWrapper, tmpUpdateStatus);

          sessionWrapper.commit();

          String event = checkStatusNotify(tmpUpdateStatus);
          if (event != null && ConfigManager.getInstance() instanceof FcConfigManager) {
            RestRequestBase request = createRequest(event);

            for (NoticeDestInfoStatus noticeDestInfo : FcConfigManager.getInstance().getSystemConfStatus()
                .getNoticeDestInfo()) {
              sendStatusNotify(request, noticeDestInfo);
            }
          }

          status = tmpUpdateStatus;
        } catch (MsfException ex) {
          sessionWrapper.rollback();
          logger.debug("DB control failed.", ex);
          throw ex;
        } finally {
          sessionWrapper.closeSession();
        }
      }

    } finally {
      logger.methodEnd();
    }
  }

  private boolean canUpdateStatus(SystemStatus before, SystemStatus after) {
    try {
      logger.methodStart(new String[] { "before", "after" }, new Object[] { before, after });
      logger.debug("SystemStatus before={0}, after={1}", before, after);

      boolean ret = false;

      if (!before.getBlockadeStatus().equals(after.getBlockadeStatus())) {
        logger.debug("BlockadeStatus is change.");

        if (before.getServiceStatus().equals(after.getServiceStatus())) {
          logger.debug("ServiceStatus is not change.");

          if (Integer.valueOf(ServiceStatus.STARTED.getCode()).equals(before.getServiceStatus())) {
            logger.debug("ServiceStatus is STARTED.");
            ret = true;
            logger.debug("canUpdateStatus={0}", ret);
            return ret;
          } else {
            logger.debug("ServiceStatus is not STARTED.");
            logger.debug("canUpdateStatus={0}", ret);
            return ret;
          }
        } else {
          logger.debug("ServiceStatus is change.");

          logger.debug("canUpdateStatus={0}", ret);
          return ret;
        }
      }

      logger.debug("BlockadeStatus is not change.");
      logger.debug("before ServiceStatus={0}, after ServiceStatus={1}", before.getServiceStatusEnum(),
          after.getServiceStatusEnum());

      switch (before.getServiceStatusEnum()) {
        case STOPPED:

          if (ServiceStatus.INITIALIZING == after.getServiceStatusEnum()) {
            ret = true;
          }
          break;
        case STARTED:

          if (ServiceStatus.FINALIZING == after.getServiceStatusEnum()
              || ServiceStatus.SWITCHING == after.getServiceStatusEnum()) {
            ret = true;
          }
          break;
        case INITIALIZING:

          if (ServiceStatus.STARTED == after.getServiceStatusEnum()
              || ServiceStatus.STOPPED == after.getServiceStatusEnum()) {
            ret = true;
          }
          break;
        case FINALIZING:

          if (ServiceStatus.STOPPED == after.getServiceStatusEnum()) {
            ret = true;
          }
          break;
        case SWITCHING:

          if (ServiceStatus.STARTED == after.getServiceStatusEnum()
              || ServiceStatus.FINALIZING == after.getServiceStatusEnum()) {
            ret = true;
          }
          break;

        default:
          String message = logger.debug("Unexpected argument.(arg={0})", before.getServiceStatusEnum());
          throw new IllegalArgumentException(message);
      }

      logger.debug("canUpdateStatus={0}", ret);

      return ret;
    } finally {
      logger.methodEnd();
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

    SystemStatusNotifyRequestBody requestBody = new SystemStatusNotifyRequestBody();

    SystemStatusControllerNotifyEntity controller = new SystemStatusControllerNotifyEntity();
    String clusterId = String
        .valueOf(FcConfigManager.getInstance().getSystemConfSwClusterData().getSwCluster().getSwClusterId());
    controller.setClusterId(clusterId);
    controller.setControllerType(ControllerType.FC.getMessage());
    controller.setEvent(event);
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

  private RestResponseBase sendStatus(RestRequestBase request, String ipAddress, int port, int noticeTimeout) {
    RestResponseBase restResponseBase = new RestResponseBase();
    try {
      String targetUri = MfcFcRequestUri.STATUS_NOTIFY.getUri();
      restResponseBase = RestClient.sendRequest(MfcFcRequestUri.STATUS_NOTIFY.getHttpMethod(), targetUri, request,
          ipAddress, port);
    } catch (MsfException msf) {
      try {
        restResponseBase = null;
        TimeUnit.MILLISECONDS.sleep(noticeTimeout);
      } catch (InterruptedException ie) {

      }
    }
    return restResponseBase;
  }
}
