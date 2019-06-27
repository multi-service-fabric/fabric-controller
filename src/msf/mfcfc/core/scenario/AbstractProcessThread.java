
package msf.mfcfc.core.scenario;

import msf.mfcfc.common.log.MsfLogger;

/**
 * Processing base class called from the periodic execution timer task.
 *
 * @author NTT
 *
 */
public abstract class AbstractProcessThread extends Thread {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractProcessThread.class);

  protected volatile boolean isRunning = false;

  protected volatile boolean wakeUpFlag = false;

  /**
   * Returns the execution state of the operation.
   *
   * @return whether the operation is running or not.
   */
  public boolean isRunning() {
    return isRunning;
  }

  /**
   * Set the wakeup flag.
   *
   * @param wakeUpFlag
   *          Wakeup flag
   */
  public void setWakeUpFlag(boolean wakeUpFlag) {
    this.wakeUpFlag = wakeUpFlag;
  }

  protected synchronized void lock(boolean isForceStop) throws InterruptedException {
    try {
      logger.methodStart();

      while ((!wakeUpFlag) && (!isForceStop)) {
        wait();
      }

      logger.trace("wake up thread.");

      isRunning = true;
      wakeUpFlag = false;
    } finally {
      logger.methodEnd();
    }
  }
}
