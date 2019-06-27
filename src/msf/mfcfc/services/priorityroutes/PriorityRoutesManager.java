
package msf.mfcfc.services.priorityroutes;

import msf.mfcfc.common.FunctionBlockBase;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Class to provide the initialization, living confirmation and termination
 * function of the priority routes control management function block.
 *
 * @author NTT
 *
 */
public class PriorityRoutesManager implements FunctionBlockBase {

  private static final MsfLogger logger = MsfLogger.getInstance(PriorityRoutesManager.class);

  protected static PriorityRoutesManager instance = null;

  protected PriorityRoutesManager() {

  }

  /**
   * Get the instance of PriorityRoutesManager.<br>
   * <br>
   * Make sure to initialize the instance with child class before calling.
   *
   * @return PriorityRoutesManager instance
   */
  public static PriorityRoutesManager getInstance() {
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
