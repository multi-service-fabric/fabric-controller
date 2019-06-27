
package msf.mfcfc.services.ctrlstsnotify.scenario.data;

public final class CtrlStsNotifyCommonData {
  private static final CtrlStsNotifyCommonData sfDataInstance = new CtrlStsNotifyCommonData();

  public static final Object FAILURE_MONITOR_STOPFLG_LOCK = new Object();

  private boolean isForceStopFailureMonitor = false;

  private CtrlStsNotifyCommonData() {
  }

  public static CtrlStsNotifyCommonData getInstance() {
    return sfDataInstance;
  }

  public boolean isForceStopFauilureMonitor() {
    synchronized (FAILURE_MONITOR_STOPFLG_LOCK) {
      return isForceStopFailureMonitor;
    }
  }

  public void setForceStopFailureMonitor(boolean forceStop) {
    synchronized (FAILURE_MONITOR_STOPFLG_LOCK) {
      this.isForceStopFailureMonitor = forceStop;
    }
  }

  public void checkForceStopFailureMonitor() throws InterruptedException {
    synchronized (FAILURE_MONITOR_STOPFLG_LOCK) {
      if (isForceStopFailureMonitor) {
        throw new InterruptedException("ForceStop FailureMonitor.");
      }
    }
  }

}
