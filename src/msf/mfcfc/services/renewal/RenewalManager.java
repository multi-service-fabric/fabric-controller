
package msf.mfcfc.services.renewal;

import msf.mfcfc.common.FunctionBlockBase;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Class to provide the initialization, living confirmation and termination
 * function of the controller file renewal function block.
 *
 * @author NTT
 *
 */
public class RenewalManager implements FunctionBlockBase {

  private static final MsfLogger logger = MsfLogger.getInstance(RenewalManager.class);

  protected static RenewalManager instance = null;

  /**
   * Get the instance of RenewalManager.<br>
   * <br>
   * Make sure to initialize the instance with child class before calling.
   *
   * @return RenewalManager instance
   */
  public static RenewalManager getInstance() {
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
