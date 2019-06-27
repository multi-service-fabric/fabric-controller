
package msf.fc.services.ctrlstsnotify.scenario;

import msf.mfcfc.common.log.MsfLogger;

/**
 * Periodic execution task for the monitoring controller status.
 *
 * @author NTT
 *
 */
public class FcCtrlStsNotifyFailureCycleWaker extends FcAbstractCtrlStsNotifyCycleWaker {

  private static final MsfLogger logger = MsfLogger.getInstance(FcCtrlStsNotifyFailureCycleWaker.class);

  /**
   * Constructor.
   *
   * @param interval
   *          Periodic execution cycle (second)
   */
  public FcCtrlStsNotifyFailureCycleWaker(int interval) {
    super(interval);
  }

  /**
   * Start a thread to execute periodically.
   */
  public void start() {
    try {
      logger.methodStart();
      thread = new FcCtrlStsNotifyFailureMonitorThread();
      thread.start();
    } finally {
      logger.methodEnd();
    }
  }
}
