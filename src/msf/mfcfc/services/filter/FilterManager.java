
package msf.mfcfc.services.filter;

import msf.mfcfc.common.FunctionBlockBase;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Class to provide the initialization, living confirmation and termination
 * function of filter management function block.
 *
 * @author NTT
 *
 */
public class FilterManager implements FunctionBlockBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FilterManager.class);

  protected static FilterManager instance = null;

  protected FilterManager() {

  }

  /**
   * Get the instance of FilterManager.<br>
   * <br>
   * Make sure to initialize the instance with child class before calling.
   *
   * @return FilterManager instance
   */
  public static FilterManager getInstance() {
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
