package msf.fc.traffic;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import msf.fc.common.FunctionBlockBase;
import msf.fc.common.config.ConfigManager;
import msf.fc.common.data.TrafficCollectInterval;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.traffic.TrafficCollectIntervalDao;
import msf.fc.traffic.data.TrafficCommonData;
import msf.fc.traffic.tm.TrafficCycleWaker;

public final class TrafficManager implements FunctionBlockBase {

  private static final TrafficManager tmInstance = new TrafficManager();

  private static final MsfLogger logger = MsfLogger.getInstance(TrafficManager.class);

  private TrafficCycleWaker trafficCycleWaker;
  private TrafficCommonData trafficCommonData;

  private Timer timer = null;

  private static final long SLEEPTIME = 1000;
  private static final int SLEEPCOUNT = 60;

  private TrafficManager() {
    trafficCommonData = TrafficCommonData.getInstance();
  }

  public static TrafficManager getInstance() {
    return tmInstance;
  }

  @Override
  public boolean start() {
    logger.methodStart();
    SessionWrapper sessionWrapper = new SessionWrapper();

    try {
      ConfigManager configManager = ConfigManager.getInstance();

      trafficCommonData.setTrafficTmToolPath(configManager.getTrafficTmToolPath());
      trafficCommonData.setTrafficInterval(configManager.getTrafficInterval());
      trafficCommonData.setTrafficTmOutputFilePath(configManager.getTrafficTmOutputFilePath());
      trafficCommonData.setTrafficDataRetentionPeriod(configManager.getTrafficDataRetentionPeriod());
      trafficCommonData.setTrafficTmInputFilePath(configManager.getTrafficTmInputFilePath());

      sessionWrapper.openSession();
      sessionWrapper.beginTransaction();

      TrafficCollectIntervalDao trafficCollectIntervalDao = new TrafficCollectIntervalDao();
      TrafficCollectInterval lastTime = trafficCollectIntervalDao.readNullData(sessionWrapper);

      final int interval = trafficCommonData.getTrafficInterval();
      if (lastTime == null || lastTime.getInterval() != interval) {
        TrafficCollectInterval current = new TrafficCollectInterval();
        current.setStartTime(new Date());
        current.setInterval(interval);
        trafficCollectIntervalDao.create(sessionWrapper, current);

        if (lastTime != null) {
          lastTime.setEndTime(new Timestamp(System.currentTimeMillis()));
          trafficCollectIntervalDao.update(sessionWrapper, lastTime);
        }

        sessionWrapper.commit();
      }

      if (interval != 0) {
        trafficCycleWaker = new TrafficCycleWaker();
        trafficCycleWaker.tmStart();

        this.setRenewTopology(true);

        timer = new Timer();
        timer.scheduleAtFixedRate(trafficCycleWaker, new Date(), TimeUnit.MINUTES.toMillis(interval));
      }

      return true;

    } catch (MsfException exp) {
      try {
        sessionWrapper.rollback();
      } catch (MsfException me) {
      }
      logger.error("Failed to start Traffic Manager.");
      return false;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  @Override
  public boolean checkStatus() {
    if (trafficCycleWaker != null) {
      return trafficCycleWaker.checkStatus();
    } else {
      return true;
    }
  }

  @Override
  public boolean stop() {

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
        } catch (InterruptedException ie) {
          logger.warn("sleep interrupted. Traffic stop Sequence.");
        }
      }
      return false;
    }
    return true;
  }

  public void setRenewTopology(boolean flag) {
    if (trafficCycleWaker != null) {
      trafficCycleWaker.setRenewTopology(flag);
    }
  }

  public boolean getRenewTopology() {
    if (trafficCycleWaker != null) {
      return trafficCycleWaker.getRenewTopology();
    } else {
      return false;
    }
  }

}
