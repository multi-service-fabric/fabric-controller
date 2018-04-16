
package msf.mfc.failure;

import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.failure.FailureManager;

/**
 * Class to provide the initialization and termination function of failure
 * management function block.
 *
 * @author NTT
 *
 */
public final class MfcFailureManager extends FailureManager {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcFailureManager.class);

  private MfcFailureManager() {
  }

  /**
   * Return the instance of MfcFailureManager
   *
   * @return MfcFailureManager instance
   */
  public static MfcFailureManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new MfcFailureManager();
      }
      return (MfcFailureManager) instance;
    } finally {
      logger.methodEnd();
    }
  }
}
