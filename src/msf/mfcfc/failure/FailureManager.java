
package msf.mfcfc.failure;

import msf.mfcfc.common.FunctionBlockBase;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Class to provide the initialization, living confirmation and termination
 * function of failure management function block.
 *
 * @author NTT
 *
 */
public class FailureManager implements FunctionBlockBase {

  protected static final MsfLogger logger = MsfLogger.getInstance(FailureManager.class);

  protected static FailureManager instance = null;

  /**
   * Get the instance of FailureManager. <br>
   * <br>
   * Make sure to initialize the instance with child class before calling.
   *
   * @return FailureManager instance
   */
  public static FailureManager getInstance() {
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
