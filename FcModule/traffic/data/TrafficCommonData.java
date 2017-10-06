package msf.fc.traffic.data;

import java.io.File;

import msf.fc.common.config.ConfigManager;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.rest.ec.traffic.data.TrafficInfoCollectTrafficEcResponseBody;

public final class TrafficCommonData {
  private static final TrafficCommonData tcDataInstance = new TrafficCommonData();

  private static final MsfLogger logger = MsfLogger.getInstance(TrafficCommonData.class);

  private int trafficInterval;
  private int trafficDataRetentionPeriod;
  private String trafficTmToolPath;
  private String trafficTmInputFilePath;
  private String trafficTmOutputFilePath;

  private TrafficInfoCollectTrafficEcResponseBody trafficResponse;

  public static final double TMSKIPVALUE = -1.0;

  public static final Object STOPFLGLOCK = new Object();
  private boolean isForceStop = false;

  private TrafficCommonData() {
  }

  public static TrafficCommonData getInstance() {
    return tcDataInstance;
  }

  public int getTrafficInterval() {
    return trafficInterval;
  }

  public void setTrafficInterval(int trafficInterval) {
    this.trafficInterval = trafficInterval;
  }

  public int getTrafficDataRetentionPeriod() {
    return trafficDataRetentionPeriod;
  }

  public void setTrafficDataRetentionPeriod(int trafficDataRetentionPeriod) {
    this.trafficDataRetentionPeriod = trafficDataRetentionPeriod;
  }

  public String getTrafficTmToolPath() {
    return trafficTmToolPath;
  }

  public void setTrafficTmToolPath(String trafficTmToolPath) throws MsfException {
    this.trafficTmToolPath = checkConfPath("tmToolPath", trafficTmToolPath, false);
  }

  private String checkConfPath(String key, String path, boolean checkWrite) throws MsfException {
    int interval = ConfigManager.getInstance().getTrafficInterval();
    if (interval != 0) {
      if ((path == null) || path.equals("")) {
        logger.error(key + " is a require field.");
        throw new MsfException(ErrorCode.PARAMETER_FORMAT_ERROR, key + " is a require field.");
      }

      File file = new File(path);
      if (!file.isDirectory()) {
        logger.error("Please specify a directory on " + key);
        throw new MsfException(ErrorCode.PARAMETER_FORMAT_ERROR, "Please specify a directory on " + key);
      }
      if (checkWrite && !file.canWrite()) {
        logger.error(key + " direcotry can not write.");
        throw new MsfException(ErrorCode.PARAMETER_FORMAT_ERROR, key + " direcotry can not write.");
      }
    }
    if (!path.endsWith("/")) {
      return path + "/";
    }
    return path;
  }

  public String getTrafficTmInputFilePath() {
    return trafficTmInputFilePath;
  }

  public void setTrafficTmInputFilePath(String trafficTmInputFilePath) throws MsfException {
    this.trafficTmInputFilePath = checkConfPath("tmInputFilePath", trafficTmInputFilePath, true);
  }

  public String getTrafficTmOutputFilePath() {
    return trafficTmOutputFilePath;
  }

  public void setTrafficTmOutputFilePath(String trafficTmOutputFilePath) throws MsfException {
    this.trafficTmOutputFilePath = checkConfPath("tmOutputFilePath", trafficTmOutputFilePath, true);
  }

  public TrafficInfoCollectTrafficEcResponseBody getTrafficResponse() {
    return trafficResponse;
  }

  public void setTrafficResponse(TrafficInfoCollectTrafficEcResponseBody trafficResponse) {
    this.trafficResponse = trafficResponse;
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
