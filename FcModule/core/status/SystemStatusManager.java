package msf.fc.core.status;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.ServiceStatus;
import msf.fc.common.data.SystemStatus;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.common.SystemStatusDao;

public final class SystemStatusManager {

  private static final SystemStatusManager instance = new SystemStatusManager();

  private SystemStatus status;

  public static final Integer FIXED_SYSTEM_ID = 1;

  private static final MsfLogger logger = MsfLogger.getInstance(SystemStatusManager.class);

  private SystemStatusManager() {

  }

  public static SystemStatusManager getInstance() {
    return instance;
  }

  public SystemStatus getSystemStatus() {
    synchronized (this) {
      SystemStatus retStatus = new SystemStatus();
      retStatus.setSystemId(status.getSystemId());
      retStatus.setServiceStatus(status.getServiceStatus());
      retStatus.setBlockadeStatus(status.getBlockadeStatus());
      return retStatus;
    }
  }

  public SystemStatus initStatus() throws MsfException {
    logger.methodStart();

    SessionWrapper sessionWrapper = new SessionWrapper();
    SystemStatusDao ssDao = new SystemStatusDao();
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
    logger.methodEnd();
    return ret;
  }

  public void changeSystemStatus(SystemStatus updateStatus) throws MsfException {
    if (logger.isDebugEnabled()) {
      logger.methodStart(new String[] { "updateStatus" }, new Object[] { updateStatus });
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
      SystemStatusDao ssDao = new SystemStatusDao();

      try {
        sessionWrapper.openSession();
        sessionWrapper.beginTransaction();

        logger.debug("Update SystemStatus={0}.", tmpUpdateStatus);

        ssDao.update(sessionWrapper, tmpUpdateStatus);

        sessionWrapper.commit();

        status = tmpUpdateStatus;
      } catch (MsfException ex) {
        sessionWrapper.rollback();
        logger.debug("DB control failed.", ex);
        throw ex;
      } finally {
        sessionWrapper.closeSession();
      }
    }

    logger.methodEnd();
  }

  private boolean canUpdateStatus(SystemStatus before, SystemStatus after) {
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
  }
}
