
package msf.mfcfc.services.ctrlstsnotify;

import msf.mfcfc.common.FunctionBlockBase;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Class to provide the initialization and termination function of the
 * controller status notification function block.
 *
 * @author NTT
 *
 */
public class CtrlStsNotifyManager implements FunctionBlockBase {

  private static final MsfLogger logger = MsfLogger.getInstance(CtrlStsNotifyManager.class);

  protected static CtrlStsNotifyManager instance = null;

  protected CtrlStsNotifyManager() {

  }

  /**
   * Get the instance of CtrlStsNotifyManager. Make sure to initialize the
   * instance with child class before calling.
   *
   * @return CtrlStsNotifyManager instance
   */
  public static CtrlStsNotifyManager getInstance() {
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
