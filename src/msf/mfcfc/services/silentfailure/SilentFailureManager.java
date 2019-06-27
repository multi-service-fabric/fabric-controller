
package msf.mfcfc.services.silentfailure;

import msf.mfcfc.common.FunctionBlockBase;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Class to provide the initialization, living confirmation and termination
 * function of the silent failure detection function block.
 *
 * @author NTT
 *
 */
public class SilentFailureManager implements FunctionBlockBase {

  private static final MsfLogger logger = MsfLogger.getInstance(SilentFailureManager.class);

  protected static SilentFailureManager instance = null;

  /**
   * Get the instance of SilentFailureManager.<br>
   * <br>
   * Make sure to initialize the instance with child class before calling.
   *
   * @return SilentFailureManager instance
   */
  public static SilentFailureManager getInstance() {
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
