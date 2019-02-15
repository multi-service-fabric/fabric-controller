
package msf.mfcfc.traffic;

import java.util.Timer;

import msf.mfcfc.common.FunctionBlockBase;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Class to provide the initialization, living confirmation and termination
 * function of the traffic management function block.
 *
 * @author NTT
 *
 */
public class TrafficManager implements FunctionBlockBase {

  private static final MsfLogger logger = MsfLogger.getInstance(TrafficManager.class);

  protected static TrafficManager instance = null;

  protected Timer timer = null;

  protected TrafficManager() {

  }

  /**
   * Get the instance of TrafficManager. <br>
   * <br>
   * Make sure to initialize the instance with child class before calling.
   *
   * @return TrafficManager instance
   */
  public static TrafficManager getInstance() {
    try {
      logger.methodStart();
      return instance;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean start() {
    return true;
  }

  @Override
  public boolean checkStatus() {
    return true;
  }

  @Override
  public boolean stop() {
    return true;
  }

}
