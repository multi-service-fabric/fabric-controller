
package msf.fc.services.filter;

import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.services.filter.FilterManager;

/**
 * Class to provide the initialization and termination function of the filter
 * management function block.
 *
 * @author NTT
 *
 */
public final class FcFilterManager extends FilterManager {

  private static final MsfLogger logger = MsfLogger.getInstance(FcFilterManager.class);

  private FcFilterManager() {

  }

  /**
   * Get the instance of FcFilterManager.<br>
   * This method does not guarantee the uniqueness of the returned instance if
   * it is called by multi-threads simultaneously on the first call.<br>
   * Guarantee that this function is called by only one thread simultaneously
   * when it is called for the first time.
   *
   * @return FcFilterManager instance
   */
  public static FcFilterManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new FcFilterManager();
      }
      return (FcFilterManager) instance;
    } finally {
      logger.methodEnd();
    }
  }

}
