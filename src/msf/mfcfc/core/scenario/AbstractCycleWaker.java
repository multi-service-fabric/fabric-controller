
package msf.mfcfc.core.scenario;

import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.rest.common.RestClient;

/**
 * Base class of timer task to execute periodically.
 *
 * @author NTT
 *
 */
public abstract class AbstractCycleWaker extends TimerTask {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractCycleWaker.class);

  protected Date lastTimeTriedToWakeUpThread;

  protected Date timeTriedToWakeUpThreadBeforeLast;

  protected Date wakeUpTime;

  protected AbstractProcessThread thread;

  protected boolean isFirst = true;

  protected int interval;

  protected AbstractCycleWaker(int interval) {
    this.interval = interval;
  }

  /**
   * Execute the living confirmation of periodic execution thread.
   *
   * @return Thread living confirmation result
   */
  public boolean isThreadAlive() {
    try {
      logger.methodStart();
      if (thread != null) {
        return thread.isAlive();
      } else {
        logger.warn("{0}: thread Instance is null : return false.", this.getClass().getSimpleName());
        return false;
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Execute the interrupt for forced shutdown.
   *
   */
  public void executeInterrupt() {
    try {
      logger.methodStart();
      if (thread != null) {
        thread.interrupt();
      } else {
        logger.warn("{0}: thread Instance is null : skip interrupt.", this.getClass().getSimpleName());
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Check the periodic execution status.
   *
   * @return true: Normal execution status / false: Abnormal status
   */
  public boolean checkStatus() {
    try {
      logger.methodStart();

      if (thread == null) {
        logger.warn("{0}: thread Instance is null : return false.", this.getClass().getSimpleName());
        return false;
      }

      if (!isThreadAlive()) {
        logger.trace("{0}: Thread not Alive : return false.", this.getClass().getSimpleName());
        return false;
      }

      if (lastTimeTriedToWakeUpThread == null || timeTriedToWakeUpThreadBeforeLast == null) {

        logger.trace("{0}: RunningTime no data : return true.", this.getClass().getSimpleName());
        return true;
      }

      if ((lastTimeTriedToWakeUpThread.getTime()
          - timeTriedToWakeUpThreadBeforeLast.getTime()) > (TimeUnit.SECONDS.toMillis(interval) * 1.5)) {
        logger.trace("{0}: Run timing is fail : return false.", this.getClass().getSimpleName());
        return false;
      }

      if ((lastTimeTriedToWakeUpThread.getTime() - wakeUpTime.getTime()) > (10 * TimeUnit.SECONDS.toMillis(interval))) {
        logger.trace("{0}: Running at long time : return false.", this.getClass().getSimpleName());
        return false;
      }
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  protected boolean waitRestClientWakeUp() {
    if (isFirst) {

      logger.debug("First time wake up.");
      isFirst = false;
      while (true) {

        if (RestClient.checkHttpClientStatus()) {
          break;
        }

        logger.debug("Waiting until RestClient to start.");
        try {
          Thread.sleep(1000);
        } catch (InterruptedException ie) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public void run() {
    try {
      logger.methodStart();

      boolean wokeUpRestClient = waitRestClientWakeUp();
      if (!wokeUpRestClient) {
        logger.debug("Http Client did not wake up.");
        return;
      }

      timeTriedToWakeUpThreadBeforeLast = lastTimeTriedToWakeUpThread;
      lastTimeTriedToWakeUpThread = new Date();

      if (thread == null) {
        logger.warn("Waking up thread has been canceled,because thread instance is null.");
        return;
      }

      if (thread.isRunning()) {

        logger.info("Waking up thread({0}) has been canceled,because thread is still running.",
            thread.getClass().getSimpleName());
        return;
      }

      wakeUpTime = new Date();
      thread.setWakeUpFlag(true);
      synchronized (thread) {
        thread.notify();
      }

    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Start a thread to execute periodically.
   */
  public abstract void start();
}
