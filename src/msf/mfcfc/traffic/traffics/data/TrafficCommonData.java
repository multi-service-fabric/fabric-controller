
package msf.mfcfc.traffic.traffics.data;

public final class TrafficCommonData {
  private static final TrafficCommonData tcDataInstance = new TrafficCommonData();

  public static final Object STOPFLGLOCK = new Object();

  private boolean isForceStop = false;

  private TrafficCommonData() {
  }

  public static TrafficCommonData getInstance() {
    return tcDataInstance;
  }

  public boolean isForceStop() {
    synchronized (STOPFLGLOCK) {
      return isForceStop;
    }
  }

  public void setForceStop(boolean forceStop) {
    synchronized (STOPFLGLOCK) {
      this.isForceStop = forceStop;
    }
  }

  public void checkForceStop() throws InterruptedException {
    synchronized (STOPFLGLOCK) {
      if (isForceStop) {
        throw new InterruptedException("ForceStop.");
      }
    }
  }

}
