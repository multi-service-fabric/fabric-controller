
package msf.mfcfc.core.status;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.ServiceStatus;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.DbManager;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.db.dao.common.SystemStatusDao;
import msf.mfcfc.rest.common.RestClient;

/**
 * Class to manage the system status. <br>
 * Execute the initialization, acquisition and modification of the system
 * status.
 *
 * @author NTT
 *
 */
public class SystemStatusManager {

  protected static SystemStatusManager instance = null;

  protected SystemStatus status;

  public static final Integer FIXED_SYSTEM_ID = 1;

  private static final MsfLogger logger = MsfLogger.getInstance(SystemStatusManager.class);

  protected SystemStatusManager() {

  }

  /**
   * Initialize SystemStatusManager and return the instance. <br>
   * <br>
   * Make sure to initialize the instance with child class before calling.
   *
   * @return SystemStatusManager instance
   */
  public static SystemStatusManager getInstance() {
    try {
      logger.methodStart();
      return instance;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Returns the system status held on memory.
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
        retStatus.setRenewalStatus(status.getRenewalStatus());
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

      logger.debug("Init system status.(systemId = {0}, blockadeStatus={1}, serviceStatus={2}, renewalStatus={3}; )",
          ret.getSystemId(), ret.getBlockadeStatus(), ret.getServiceStatus(), ret.getRenewalStatus());
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

      if (updateStatus.getServiceStatus() == null && updateStatus.getBlockadeStatus() == null
          && updateStatus.getRenewalStatus() == null) {
        String message = "Input parameter serviceStatus and blockadeStatus and renewalStatus are null.";
        logger.warn(message);
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, message);
      }

      if (status.getServiceStatus().equals(updateStatus.getServiceStatus())
          && status.getBlockadeStatus().equals(updateStatus.getBlockadeStatus())
          && status.getRenewalStatus().equals(updateStatus.getRenewalStatus())) {
        logger.warn("Argument status is the same as current status.");
        return;
      }

      SystemStatus tmpUpdateStatus = new SystemStatus();
      tmpUpdateStatus.setSystemId(FIXED_SYSTEM_ID);
      tmpUpdateStatus.setServiceStatus(updateStatus.getServiceStatus());
      tmpUpdateStatus.setBlockadeStatus(updateStatus.getBlockadeStatus());
      tmpUpdateStatus.setRenewalStatus(updateStatus.getRenewalStatus());

      logger.performance("Start wait to update system_status table.");
      synchronized (this) {
        logger.performance("End wait to update system_status table.");

        if (tmpUpdateStatus.getServiceStatus() == null) {
          tmpUpdateStatus.setServiceStatus(status.getServiceStatus());
        }
        if (tmpUpdateStatus.getBlockadeStatus() == null) {
          tmpUpdateStatus.setBlockadeStatus(status.getBlockadeStatus());
        }
        if (tmpUpdateStatus.getRenewalStatus() == null) {
          tmpUpdateStatus.setRenewalStatus(status.getRenewalStatus());
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

          statusNotifyProcess(tmpUpdateStatus);

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
      logger.methodStart(new String[] { "before", "after" },
          new Object[] { ToStringBuilder.reflectionToString(before), ToStringBuilder.reflectionToString(after) });

      boolean ret = false;

      int changeCountNum = 0;

      if (!before.getServiceStatus().equals(after.getServiceStatus())) {
        logger.debug("count ServiceStatus for change.");
        changeCountNum++;
      }
      if (!before.getBlockadeStatus().equals(after.getBlockadeStatus())) {
        logger.debug("count BlockadeStatus for change.");
        changeCountNum++;
      }
      if (!before.getRenewalStatus().equals(after.getRenewalStatus())) {
        logger.debug("count RenewalStatus for change.");
        changeCountNum++;
      }

      if (changeCountNum != 1) {
        logger.debug("It cannot be changed because there is not one state that has changed.");
        return ret;
      }

      if (!before.getBlockadeStatus().equals(after.getBlockadeStatus())) {

        if (Integer.valueOf(ServiceStatus.STARTED.getCode()).equals(before.getServiceStatus())) {
          logger.debug("ServiceStatus is STARTED.");
          ret = true;
        } else {
          logger.debug("ServiceStatus is not STARTED.");
        }
      }

      if (!before.getServiceStatus().equals(after.getServiceStatus())) {

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
      }

      if (!before.getRenewalStatus().equals(after.getRenewalStatus())) {

        if (Integer.valueOf(ServiceStatus.STARTED.getCode()).equals(before.getServiceStatus())) {
          logger.debug("ServiceStatus is STARTED.");
          ret = true;
        } else {
          logger.debug("ServiceStatus is not STARTED.");
        }
      }

      logger.debug("canUpdateStatus={0}", ret);

      return ret;
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
  public void statusNotifyProcess(SystemStatus tmpUpdateStatus) {

  }

  protected RestResponseBase sendStatus(RestRequestBase request, String ipAddress, int port, int noticeTimeout) {
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
