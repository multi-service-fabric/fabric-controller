
package msf.fc.failure;

import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.failure.FailureManager;

/**
 * Class to provide the initialization and termination function of the failure
 * management function block.
 *
 * @author NTT
 *
 */
public final class FcFailureManager extends FailureManager {

  private static final MsfLogger logger = MsfLogger.getInstance(FcFailureManager.class);

  private FcFailureManager() {
  }

  /**
   * Returns the instance of FcFailureManager. This method does not guarantee
   * the uniqueness of the returned instance if it is called by multi-threads
   * simultaneously on the first call.<br>
   * Guarantee that this function is called by only one thread simultaneously
   * when it is called for the first time.
   *
   * @return FcFailureManager instance
   */
  public static FcFailureManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new FcFailureManager();
      }
      return (FcFailureManager) instance;
    } finally {
      logger.methodEnd();
    }
  }
}
