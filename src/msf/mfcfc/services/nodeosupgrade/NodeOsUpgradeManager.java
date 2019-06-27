
package msf.mfcfc.services.nodeosupgrade;

import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.reservation.ReservationBlockBase;

/**
 * Class to provide the initialization, living confirmation and termination
 * function of the node OS upgrade management function block.
 *
 * @author NTT
 *
 */
public class NodeOsUpgradeManager implements ReservationBlockBase {

  private static final MsfLogger logger = MsfLogger.getInstance(NodeOsUpgradeManager.class);

  protected static NodeOsUpgradeManager instance = null;

  /**
   * Get the instance of NodeOsUpgradeManager
   *
   * @return NodeOsUpgradeManager instance
   */
  public static NodeOsUpgradeManager getInstance() {
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

  @Override
  public boolean stopScheduler() {
    return true;
  }

  @Override
  public boolean startScheduler() {
    return true;
  }

}
