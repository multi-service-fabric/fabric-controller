
package msf.fc.services.silentfailure.scenario;

import msf.mfcfc.common.log.MsfLogger;

/**
 * Periodic execution task for the OSPF neighbor monitoring.
 *
 * @author NTT
 *
 */
public class FcOspfNeighborMonitorCycleWaker extends FcAbstractSilentFailureCycleWaker {

  private static final MsfLogger logger = MsfLogger.getInstance(FcOspfNeighborMonitorCycleWaker.class);

  /**
   * Constructor.
   *
   * @param interval
   *          Periodic execution cycle (second)
   */
  public FcOspfNeighborMonitorCycleWaker(int interval) {
    super(interval);
  }

  /**
   * Start a thread to execute periodically.
   */
  public void start() {
    try {
      logger.methodStart();
      thread = new FcOspfNeighborMonitorThread();
      thread.start();
    } finally {
      logger.methodEnd();
    }
  }
}
