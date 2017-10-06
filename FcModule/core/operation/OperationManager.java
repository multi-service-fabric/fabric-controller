package msf.fc.core.operation;

import java.util.ArrayList;
import java.util.List;

import msf.fc.common.config.ConfigManager;
import msf.fc.common.constant.BlockadeStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationExecutionStatus;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.ServiceStatus;
import msf.fc.common.data.SystemStatus;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.status.SystemStatusManager;

public final class OperationManager {

  private static final MsfLogger logger = MsfLogger.getInstance(OperationManager.class);

  private static final OperationManager instance = new OperationManager();

  private boolean isStopPhase = false;

  private List<String> assignedOperationIdList = new ArrayList<String>();

  private OperationManager() {
  }

  public static OperationManager getInstance() {
    return instance;
  }

  public String assignOperationId() throws MsfException {
    logger.methodStart();

    String ret = null;

    synchronized (this) {
      if (isStopPhase) {
        logger.debug("isStopPhase is true.");
        throw new MsfException(ErrorCode.SYSTEM_STATUS_ERROR, "Failed to assign operation id.");
      }

      while (true) {
        long currentTime = System.currentTimeMillis();
        ret = String.valueOf(currentTime);

        if (!assignedOperationIdList.contains(ret)) {
          assignedOperationIdList.add(ret);
          break;
        }

        logger.debug("Operation id is repeated.(ope_id=" + ret + ")");
      }

    }

    logger.methodEnd(new String[] { "assigned operationId" }, new Object[] { ret });
    return ret;
  }

  public void releaseOperationId(String operationId) {
    logger.methodStart(new String[] { "operationId" }, new Object[] { operationId });

    boolean ret = false;

    synchronized (this) {
      ret = assignedOperationIdList.remove(operationId);
    }

    logger.debug("releaseOperationId execute.(result = {0})", ret);
    logger.methodEnd();
  }

  public OperationExecutionStatus getOperationExecutionStatus(OperationType operationType) {
    logger.methodStart(new String[] { "operationType" }, new Object[] { operationType });

    OperationExecutionStatus ret = OperationExecutionStatus.NOT_ALLOWED;
    SystemStatus sysStatus = SystemStatusManager.getInstance().getSystemStatus();

    logger.debug("SystemStatus={0} .", sysStatus);

    switch (operationType) {
      case NORMAL:
        if (sysStatus.getServiceStatusEnum() == ServiceStatus.STARTED
            && sysStatus.getBlockadeStatusEnum() == BlockadeStatus.NONE) {
          ret = OperationExecutionStatus.ALLOWED;
        }
        break;
      case PRIORITY:
        if (sysStatus.getServiceStatusEnum() != ServiceStatus.STOPPED) {
          ret = OperationExecutionStatus.ALLOWED;
        }
        break;
      default:
        String message = logger.debug("Unexpected argument.(arg={0})", operationType);
        throw new IllegalArgumentException(message);
    }

    logger.methodEnd(new String[] { "result" }, new Object[] { ret });
    return ret;
  }

  public boolean hasNoExecutingOperations(boolean blocking) {
    logger.methodStart(new String[] { "blocking" }, new Object[] { blocking });

    boolean ret = false;

    if (!blocking) {
      synchronized (this) {
        if (assignedOperationIdList.size() == 0) {
          isStopPhase = true;
          logger.methodEnd(new String[] { "result" }, new Object[] { ret });
          ret = true;

        } else {
          logger.methodEnd(new String[] { "result" }, new Object[] { ret });
          ret = false;
        }
      }

    } else {
      int interval = ConfigManager.getInstance().getExecutingOperationCheckCycle();
      for (;;) {
        synchronized (this) {
          if (assignedOperationIdList.size() == 0) {
            logger.info("Assigned operation id num=0.");

            isStopPhase = true;
            ret = true;
            break;
          }

          logger.info("Assigned operation id info.(num={0}, operationId={1})", assignedOperationIdList.size(),
              assignedOperationIdList);
        }

        try {
          Thread.sleep(interval);
        } catch (InterruptedException ex) {
          logger.debug("Thread interrrupted.", ex);
        }
      }
    }

    logger.methodEnd(new String[] { "result" }, new Object[] { ret });
    return ret;
  }

}
