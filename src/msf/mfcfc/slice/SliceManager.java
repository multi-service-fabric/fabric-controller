
package msf.mfcfc.slice;

import msf.mfcfc.common.FunctionBlockBase;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Class to provide the initialization, living confirmation and termination
 * function of slice management function block.
 *
 * @author NTT
 *
 */
public class SliceManager implements FunctionBlockBase {

  protected static final MsfLogger logger = MsfLogger.getInstance(SliceManager.class);

  protected static SliceManager instance = null;

  private static final Object l2SliceCreateLockObject = new Object();

  private static final Object l3SliceCreateLockObject = new Object();

  protected SliceManager() {

  }

  /**
   * Get the instance of SliceManager. <br>
   * <br>
   * Make sure to initialize the instance with child class before calling.
   *
   * @return SliceManager instance
   */
  public static SliceManager getInstance() {
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

  /**
   * Get the lock object for exclusive control of L2 slice addition.
   *
   * @return the lock object for exclusive control of L2 slice addition
   */
  public Object getL2SliceCreateLockObject() {
    return l2SliceCreateLockObject;
  }

  /**
   * Get the lock object for exclusive control of L3 slice addition.
   *
   * @return the lock object for exclusive control of L3 slice addition
   */
  public Object getL3SliceCreateLockObject() {
    return l3SliceCreateLockObject;
  }

}
