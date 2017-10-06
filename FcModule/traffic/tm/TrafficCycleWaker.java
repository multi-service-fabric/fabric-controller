package msf.fc.traffic.tm;

import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import msf.fc.common.log.MsfLogger;
import msf.fc.rest.common.RestClient;
import msf.fc.traffic.data.TrafficCommonData;

public class TrafficCycleWaker extends TimerTask {

  private static final MsfLogger logger = MsfLogger.getInstance(TrafficCycleWaker.class);

  private TrafficTmEstimation trafficTmEstimation;
  private TrafficCommonData trafficCommonData;

  private Date isRunningTime;
  private Date beforeIsRunningTime;
  private Date wakeupTime;

  private boolean isFirst = true;

  public TrafficCycleWaker() {
    trafficCommonData = TrafficCommonData.getInstance();

  }

  public void tmStart() {
    this.trafficTmEstimation = new TrafficTmEstimation();
    trafficTmEstimation.start();

    TrafficTmEstimation.setEndTime(new Date());
  }

  @Override
  public void run() {
      logger.debug("First time wakeup.");
      isFirst = false;
      while (true) {
          break;
        } else {
          logger.debug("Waiting until RestClient to start.");
          try {
            Thread.sleep(1000);
          } catch (InterruptedException ie) {
            return;
          }
        }
      }
    }

    beforeIsRunningTime = isRunningTime;
    isRunningTime = new Date();

    if (trafficTmEstimation.isRunning()) {
      logger.info("TM Execute cancel. Because TM Estimation running now.");
      return;
    }

    wakeupTime = new Date();
    trafficTmEstimation.setWakeupFlag(true);
    synchronized (trafficTmEstimation) {
      trafficTmEstimation.notify();
    }
  }

  public boolean threadAlive() {
    return trafficTmEstimation.isAlive();
  }

  public void executeInterrupt() {
    trafficTmEstimation.interrupt();
  }

  public void setRenewTopology(boolean flag) {
    trafficTmEstimation.setRenewTopology(flag);
  }

  public boolean getRenewTopology() {
    return trafficTmEstimation.getRenewTopology();
  }

  public boolean checkStatus() {
    int interval = trafficCommonData.getTrafficInterval();
    if (interval == 0) {
      logger.trace("Traffic: checkStatus() interval = 0 : return true.");
      return true;
    }

    if (trafficTmEstimation == null) {
      logger.trace("Traffic: checkStatus() TrafficTmEstimation Instance is NULL : return false.");
      return false;
    }

    if (!this.threadAlive()) {
      logger.trace("Traffic: checkStatus() TrafficTmEstimation Thread not Alive : return false.");
      return false;
    }

    if (isRunningTime == null || beforeIsRunningTime == null) {
      logger.trace("Traffic: checkStatus() RunningTime no data : return true.");
      return true;
    }

    if ((isRunningTime.getTime() - beforeIsRunningTime.getTime()) > (TimeUnit.MINUTES.toMillis(interval) * 1.5)) {
      logger.trace("Traffic: checkStatus() Run timing is fail : return false.");
      return false;
    }

    if ((isRunningTime.getTime() - wakeupTime.getTime()) > (10 * TimeUnit.MINUTES.toMillis(interval))) {
      logger.trace("Traffic: checkStatus() TmEstimation Running at long time : return false.");
      return false;
    }
    return true;
  }

}
