
package msf.fc.traffic;

import java.util.Date;
import java.util.Timer;

import msf.fc.common.config.FcConfigManager;
import msf.fc.common.config.type.system.Traffic;
import msf.fc.traffic.traffics.FcTrafficCycleWaker;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.traffic.TrafficManager;
import msf.mfcfc.traffic.traffics.data.TrafficCommonData;

/**
 * Class to provide the initialization and termination function of the traffic
 * information management function block.
 *
 * @author NTT
 *
 */
public final class FcTrafficManager extends TrafficManager {

  private static TrafficCommonData trafficCommonData = TrafficCommonData.getInstance();
  private FcTrafficCycleWaker trafficCycleWaker = null;

  private static final MsfLogger logger = MsfLogger.getInstance(FcTrafficManager.class);

  private static final long SLEEPTIME = 1000;
  private static final int SLEEPCOUNT = 60;

  private FcTrafficManager() {
  }

  /**
   * Returns the instance of TrafficManager. This method does not guarantee the
   * uniqueness of the returned instance if it is called by multi-threads
   * simultaneously on the first call.<br>
   * Guarantee that this function is called by only one thread simultaneously
   * when it is called for the first time.
   *
   * @return TrafficManager instance
   */
  public static FcTrafficManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new FcTrafficManager();
      }
      return (FcTrafficManager) instance;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean start() {

    try {
      logger.methodStart();

      Traffic systemConfig = FcConfigManager.getInstance().getSystemConfTraffic();
      final int interval = systemConfig.getExecCycle();

      if (interval != 0) {
        trafficCycleWaker = new FcTrafficCycleWaker();
        trafficCycleWaker.noticeStart();

        timer = new Timer();
        timer.scheduleAtFixedRate(trafficCycleWaker, new Date(), (interval * 1000));
      }

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean checkStatus() {

    try {
      logger.methodStart();
      if (trafficCycleWaker != null) {
        return trafficCycleWaker.checkStatus();
      }
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean stop() {

    try {
      logger.methodStart();

      if (timer != null) {
        timer.cancel();
        timer = null;

        trafficCommonData.setForceStop(true);

        trafficCycleWaker.executeInterrupt();

        for (int count = 1; count <= SLEEPCOUNT; count++) {

          if (!trafficCycleWaker.threadAlive()) {
            return true;
          }
          try {
            Thread.sleep(SLEEPTIME);
          } catch (InterruptedException ie) {
            logger.warn("sleep interrupted. Traffic stop Sequence.");
          }
        }

        logger.warn("Can't stop Thread.");
        return false;
      }
      return true;
    } finally {
      logger.methodEnd();
    }
  }
}
