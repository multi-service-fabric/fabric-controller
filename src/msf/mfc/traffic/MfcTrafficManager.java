
package msf.mfc.traffic;

import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.traffic.TrafficManager;

/**
 * Class to provide the initialization and termination function of traffic
 * information management function block.
 *
 * @author NTT
 *
 */
public final class MfcTrafficManager extends TrafficManager {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcTrafficManager.class);

  private MfcTrafficManager() {
  }

  /**
   * Return the instance of TrafficManager
   *
   * @return TrafficManager instance
   */
  public static MfcTrafficManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new MfcTrafficManager();
      }
      return (MfcTrafficManager) instance;
    } finally {
      logger.methodEnd();
    }
  }

}
