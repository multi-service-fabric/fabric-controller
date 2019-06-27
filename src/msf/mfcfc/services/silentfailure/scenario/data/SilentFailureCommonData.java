
package msf.mfcfc.services.silentfailure.scenario.data;

public final class SilentFailureCommonData {
  private static final SilentFailureCommonData sfDataInstance = new SilentFailureCommonData();

  public static final Object PING_MONITOR_STOPFLG_LOCK = new Object();

  public static final Object OSPF_NEIGHBOR_MONITOR_STOPFLG_LOCK = new Object();

  private boolean isForceStopPingMonitor = false;

  private boolean isForceStopOspfNeighborMonitor = false;

  private SilentFailureCommonData() {
  }

  public static SilentFailureCommonData getInstance() {
    return sfDataInstance;
  }

  public boolean isForceStopPingMonitor() {
    synchronized (PING_MONITOR_STOPFLG_LOCK) {
      return isForceStopPingMonitor;
    }
  }

  public void setForceStopPingMonitor(boolean forceStop) {
    synchronized (PING_MONITOR_STOPFLG_LOCK) {
      this.isForceStopPingMonitor = forceStop;
    }
  }

  public void checkForceStopPingMonitor() throws InterruptedException {
    synchronized (PING_MONITOR_STOPFLG_LOCK) {
      if (isForceStopPingMonitor) {
        throw new InterruptedException("ForceStop PingMonitor.");
      }
    }
  }

  public boolean isForceStopOspfNeighborMonitor() {
    synchronized (OSPF_NEIGHBOR_MONITOR_STOPFLG_LOCK) {
      return isForceStopOspfNeighborMonitor;
    }
  }

  public void setForceStopOspfNeighborMonitor(boolean forceStop) {
    synchronized (OSPF_NEIGHBOR_MONITOR_STOPFLG_LOCK) {
      this.isForceStopOspfNeighborMonitor = forceStop;
    }
  }

  public void checkForceStopOspfNeighborMonitor() throws InterruptedException {
    synchronized (OSPF_NEIGHBOR_MONITOR_STOPFLG_LOCK) {
      if (isForceStopOspfNeighborMonitor) {
        throw new InterruptedException("ForceStop ospfNeighborMonitor.");
      }
    }
  }

}
