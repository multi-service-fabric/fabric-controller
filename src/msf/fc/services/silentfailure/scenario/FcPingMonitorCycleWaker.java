
package msf.fc.services.silentfailure.scenario;

import msf.mfcfc.common.log.MsfLogger;

/**
 * Periodic execution task for the monitoring communication between devices by
 * ping.
 *
 * @author NTT
 *
 */
public class FcPingMonitorCycleWaker extends FcAbstractSilentFailureCycleWaker {

  private static final MsfLogger logger = MsfLogger.getInstance(FcPingMonitorCycleWaker.class);

  /**
   * Constructor.
   *
   * @param interval
   *          Periodic execution cycle (second)
   */
  public FcPingMonitorCycleWaker(int interval) {
    super(interval);
  }

  /**
   * Start a thread to execute periodically.
   */
  public void start() {
    try {
      logger.methodStart();
      thread = new FcPingMonitorThread();
      thread.start();
    } finally {
      logger.methodEnd();
    }
  }
}
