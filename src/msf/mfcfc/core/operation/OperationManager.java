
package msf.mfcfc.core.operation;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.BlockadeStatus;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationExecutionStatus;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.ServiceStatus;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.status.SystemStatusManager;

/**
 * Based on MFC / FC system status, provide the excecution propriety decision of
 * operation, and the function to manage the operation ID.
 *
 * @author NTT
 *
 */
public class OperationManager {

  private static final MsfLogger logger = MsfLogger.getInstance(OperationManager.class);

  private static final OperationManager instance = new OperationManager();

  private boolean isStopPhase = false;

  private Map<String, Object> assignedOperationIdMap = new HashMap<String, Object>();

  Map<String, Map<String, Timer>> operationTimerMap = new HashMap<>();

  Map<String, OperationData> operationDataMap = new HashMap<>();

  private String clusterIdForOperationId = "000";

  private Map<String, Integer> lowerRequestNumberMap = new HashMap<>();

  protected OperationManager() {
  }

  /**
   * Get the instance of OperationManager.
   *
   * @return OperationManager instance
   */
  public static OperationManager getInstance() {
    return instance;
  }

  /**
   * Pay out the operation ID (UNIX timestamp).
   *
   * @return operation ID
   * @throws MsfException
   *           If request is unacceptable (stopped)
   */
  public String assignOperationId() throws MsfException {
    try {
      logger.methodStart();

      String ret = null;

      synchronized (this) {
        if (isStopPhase) {
          logger.debug("isStopPhase is true.");
          throw new MsfException(ErrorCode.SYSTEM_STATUS_ERROR, "Failed to assign operation id.");
        }

        while (true) {
          long currentTime = System.currentTimeMillis();
          ret = clusterIdForOperationId + String.valueOf(currentTime);

          if (!assignedOperationIdMap.containsKey(ret)) {
            assignedOperationIdMap.put(ret, new Object());
            break;
          }

          logger.debug("Operation id is repeated.(ope_id=" + ret + ")");
        }

      }
      logger.debug("Operation id is assigned.(ope_id=" + ret + ")");
      return ret;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Release the operation ID.
   *
   * @param operationId
   *          Operation ID
   */
  public void releaseOperationId(String operationId) {
    synchronized (this) {
      try {
        logger.methodStart(new String[] { "operationId" }, new Object[] { operationId });

        Object object = assignedOperationIdMap.remove(operationId);

        logger.debug("releaseOperationId execute.(result = {0})", (object != null));

        if (operationDataMap.get(operationId) != null) {

          operationDataMap.remove(operationId);
          logger.debug("operation data removed.");
        }
        if (lowerRequestNumberMap.get(operationId) != null) {

          lowerRequestNumberMap.remove(operationId);
        }
      } finally {
        logger.methodEnd();
      }
    }
  }

  /**
   * Decide whether to execute the operation, and return the result.
   *
   * @param operationType
   *          Operation type
   * @return excecution propriety of operation
   *
   */
  public OperationExecutionStatus getOperationExecutionStatus(OperationType operationType) {
    return getOperationExecutionStatus(operationType, false);
  }

  /**
   * Decide whether to execute the operation, and return the result.
   *
   * @param operationType
   *          Operation type
   * @param isGetRequest
   *          Whether the request is of information acquisition or not
   * @return excecution propriety of operation
   */
  public OperationExecutionStatus getOperationExecutionStatus(OperationType operationType, boolean isGetRequest) {
    try {
      logger.methodStart(new String[] { "operationType" }, new Object[] { operationType });

      OperationExecutionStatus ret = OperationExecutionStatus.NOT_ALLOWED;
      SystemStatus sysStatus = SystemStatusManager.getInstance().getSystemStatus();

      logger.debug("SystemStatus={0} .", sysStatus);

      switch (operationType) {
        case NORMAL:

          if (sysStatus.getServiceStatusEnum() == ServiceStatus.STARTED) {
            if (sysStatus.getBlockadeStatusEnum() == BlockadeStatus.NONE || isGetRequest) {
              ret = OperationExecutionStatus.ALLOWED;
            }
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

      logger.debug("result = " + ret);
      return ret;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Confirm the existence of running operations. <br>
   * If the blocking execution flag is true, wait until the number of running
   * operations becomes 0.
   *
   * @param blocking
   *          Blocking execution flag
   * @return presence/absence of running operations
   */
  public boolean hasNoExecutingOperations(boolean blocking) {
    try {
      logger.methodStart(new String[] { "blocking" }, new Object[] { blocking });

      boolean ret = false;

      if (!blocking) {
        synchronized (this) {
          if (assignedOperationIdMap.size() == 0) {
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
            if (assignedOperationIdMap.size() == 0) {
              logger.info("Assigned operation id num=0.");

              isStopPhase = true;
              ret = true;
              break;
            }

            logger.info("Assigned operation id info.(num={0}, operationId={1})", assignedOperationIdMap.size(),
                assignedOperationIdMap.keySet());
          }

          try {
            Thread.sleep(interval);
          } catch (InterruptedException ex) {
            logger.debug("Thread interrrupted.", ex);
          }
        }
      }
      logger.debug("result = " + ret);
      return ret;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Set the timer for the specified lower operation.
   *
   * @param operationId
   *          Operation ID
   * @param clusterId
   *          Lower operation target SW cluster ID
   * @param lowerOperationId
   *          Lower operation ID
   * @param timeout
   *          Timeout value (ms)
   * @param timerTask
   *          Processing at timeout
   */
  public void setTimerForOperation(String operationId, String clusterId, String lowerOperationId, int timeout,
      TimerTask timerTask) {
    synchronized (this) {
      try {
        logger.methodStart(new String[] { "operationId", "lowerOperationId", "timeout", "timerTask" },
            new Object[] { operationId, lowerOperationId, timeout, timerTask });

        Map<String, Timer> lowerOperationTimerMap = operationTimerMap.get(operationId);

        Timer timer = new Timer();
        timer.schedule(timerTask, timeout);

        if (lowerOperationTimerMap == null) {
          logger.debug("create first timer for lower sw cluster id :{0}, lower operation id :{1}", clusterId,
              lowerOperationId);
          lowerOperationTimerMap = new HashMap<String, Timer>();
          lowerOperationTimerMap.put(createIdForOperationTimer(clusterId, lowerOperationId), timer);
          operationTimerMap.put(operationId, lowerOperationTimerMap);
        } else {
          logger.debug("existing timer number is {0}", lowerOperationTimerMap.size());
          lowerOperationTimerMap.put(createIdForOperationTimer(clusterId, lowerOperationId), timer);
        }

      } finally {
        logger.methodEnd();
      }
    }
  }

  /**
   * Cancel the timer for the specified lower operation.
   *
   * @param operationId
   *          Operation ID
   * @param clusterId
   *          Lower operation target SW cluster ID
   * @param lowerOperationId
   *          Lower operation ID
   */
  public void cancelTimerForOperation(String operationId, String clusterId, String lowerOperationId) {
    synchronized (this) {
      try {
        logger.methodStart(new String[] { "operationId", "clusterId", "lowerOperationId" },
            new Object[] { operationId, clusterId, lowerOperationId });

        Map<String, Timer> lowerOperationTimerMap = operationTimerMap.get(operationId);

        if (lowerOperationTimerMap == null || lowerOperationTimerMap.size() == 0) {
          logger.debug("timer not found associated with operation id");
          return;
        }

        Timer timer = lowerOperationTimerMap.get(createIdForOperationTimer(clusterId, lowerOperationId));

        if (timer == null) {
          logger.debug("timer not found associated with lower operation id");
        } else {

          timer.cancel();
          logger.debug("timer is canceled.");

          lowerOperationTimerMap.remove(createIdForOperationTimer(clusterId, lowerOperationId));

          if (lowerOperationTimerMap.size() == 0) {
            logger.debug("removetimer map from parent map.");
            operationTimerMap.remove(operationId);
          }
        }
      } finally {
        logger.methodEnd();
      }
    }
  }

  /**
   * Operation data setting.
   *
   * @param operationId
   *          Operation ID
   * @param operationData
   *          Operation data to set
   */
  public void setOperationData(String operationId, OperationData operationData) {
    synchronized (this) {
      try {
        logger.methodStart(new String[] { "operationId", "operationData" },
            new Object[] { operationId, operationData });
        operationDataMap.put(operationId, operationData);
      } finally {
        logger.methodEnd();
      }

    }
  }

  /**
   * Operation data acquisition.
   *
   * @param operationId
   *          Operation ID
   * @return operation data
   */
  public OperationData getOperationData(String operationId) {
    synchronized (this) {
      try {
        logger.methodStart(new String[] { "operationId" }, new Object[] { operationId });
        return operationDataMap.get(operationId);
      } finally {
        logger.methodEnd();
      }

    }
  }

  /**
   * Return the exclusive control object of the specified operation ID.
   *
   * @param operationId
   *          Operation ID
   * @return exclusive control object of the specified operation ID
   */
  public Object getOperationIdObject(String operationId) {
    synchronized (this) {
      try {
        logger.methodStart(new String[] { "operationId" }, new Object[] { operationId });
        return assignedOperationIdMap.get(operationId);
      } finally {
        logger.methodEnd();
      }
    }
  }

  /**
   * Set the self cluster ID for operation ID generation.
   *
   * @param clusterId
   *          Self cluster ID
   */
  public void setClusterIdForOperationId(int clusterId) {
    try {
      logger.methodStart();
      clusterIdForOperationId = String.format("%03d", clusterId);
      logger.debug("cluster id for operation id = " + clusterIdForOperationId);
    } finally {
      logger.methodEnd();
    }
  }

  private String createIdForOperationTimer(String clusterId, String lowerOperationId) {
    String timerId = clusterId + "_" + lowerOperationId;
    logger.debug("timer id = " + timerId);
    return timerId;
  }

  /**
   * Set the number of requests that are sent to the lower controller
   * simultaneously.
   *
   * @param operationId
   *          Target operation ID
   * @param requestNumber
   *          Number of simultaneous requests
   */
  public void setLowerRequestNumber(String operationId, int requestNumber) {
    logger.debug("set lower request number = {0},(operation id = {1})", requestNumber, operationId);
    lowerRequestNumberMap.put(operationId, requestNumber);
  }

  /**
   * Get the number of requests that are sent to the lower controller
   * simultaneously.
   *
   * @param operationId
   *          Operation ID
   * @return the pre-set number of requests that are sent simultaneously. If
   *         unset, return null
   */
  public Integer getLowerRequestNumber(String operationId) {
    return lowerRequestNumberMap.get(operationId);
  }
}
