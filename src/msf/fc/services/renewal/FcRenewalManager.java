
package msf.fc.services.renewal;

import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.services.renewal.RenewalManager;

/**
 * Class to provide the initialization and termination function of the
 * controller file renewal function block.
 *
 * @author NTT
 *
 */
public class FcRenewalManager extends RenewalManager {

  private static final MsfLogger logger = MsfLogger.getInstance(FcRenewalManager.class);

  private FcRenewalManager() {

  }

  /**
   * Get the instance of FcSilentFailureManager<br>
   * This method does not guarantee the uniqueness of the returned instance if
   * it is called by multi-threads simultaneously on the first call.<br>
   * Guarantee that this function is called by only one thread simultaneously
   * when it is called for the first time.
   *
   * @return FcRenewalManager instance
   */
  public static FcRenewalManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new FcRenewalManager();
      }
      return (FcRenewalManager) instance;
    } finally {
      logger.methodEnd();
    }
  }

}
