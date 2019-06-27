
package msf.fc.traffic.traffics;

import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import msf.fc.common.config.FcConfigManager;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.rest.common.RestClient;

/**
 * Periodic execution task for traffic information notification.
 *
 * @author NTT
 *
 */
public class FcTrafficCycleWaker extends TimerTask {

  private static final MsfLogger logger = MsfLogger.getInstance(FcTrafficCycleWaker.class);

  private FcTrafficNoticeThread fcTrafficNoticeThread;

  private Date lastTimeTriedToWakeUpThread;

  private Date timeTriedToWakeUpThreadBeforeLast;

  private Date wakeUpTime;

  private boolean isFirst = true;

  /**
   * Startup of execution thread for traffic information notification.
   */
  public void noticeStart() {
    this.fcTrafficNoticeThread = new FcTrafficNoticeThread();
    fcTrafficNoticeThread.start();

    FcTrafficNoticeThread.setEndTime(new Date());
  }

  @Override
  public void run() {
    try {
      logger.methodStart();
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
            return;
          }
        }
      }

      timeTriedToWakeUpThreadBeforeLast = lastTimeTriedToWakeUpThread;
      lastTimeTriedToWakeUpThread = new Date();

      if (fcTrafficNoticeThread.isRunning()) {

        logger.info("Cancellation of traffic information notice. Because FcTrafficNoticeThread Already running.");
        return;
      }

      wakeUpTime = new Date();
      fcTrafficNoticeThread.setWakeUpFlag(true);
      synchronized (fcTrafficNoticeThread) {
        fcTrafficNoticeThread.notify();
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Execute the living confirmation of periodic execution thread.
   *
   * @return thread living confirmation result
   */
  public boolean threadAlive() {
    try {
      logger.methodStart();
      return fcTrafficNoticeThread.isAlive();
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
      fcTrafficNoticeThread.interrupt();
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

      if (fcTrafficNoticeThread == null) {
        logger.trace("Traffic: checkStatus() FcTrafficNoticeThread Instance is NULL : return false.");
        return false;
      }

      if (!this.threadAlive()) {
        logger.trace("Traffic: checkStatus() FcTrafficNoticeThread Thread not Alive : return false.");
        return false;
      }

      if (lastTimeTriedToWakeUpThread == null || timeTriedToWakeUpThreadBeforeLast == null) {

        logger.trace("Traffic: checkStatus() RunningTime no data : return true.");
        return true;
      }

      int interval = FcConfigManager.getInstance().getSystemConfTraffic().getExecCycle();

      if ((lastTimeTriedToWakeUpThread.getTime()
          - timeTriedToWakeUpThreadBeforeLast.getTime()) > (TimeUnit.SECONDS.toMillis(interval) * 1.5)) {
        logger.trace("Traffic: checkStatus() Run timing is fail : return false.");
        return false;
      }

      if ((lastTimeTriedToWakeUpThread.getTime() - wakeUpTime.getTime()) > (10 * TimeUnit.SECONDS.toMillis(interval))) {
        logger.trace("Traffic: checkStatus() FcTrafficNoticeThread Running at long time : return false.");
        return false;
      }
      return true;
    } finally {
      logger.methodEnd();
    }
  }

}
