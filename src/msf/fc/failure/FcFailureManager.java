
package msf.fc.failure;

import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.failure.FailureManager;

/**
 * Class to provide the initialization and termination function of failure
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
   * Return the instance of FcFailureManager.
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
