
package msf.mfcfc.node;

import msf.mfcfc.common.FunctionBlockBase;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Class to provide the initialization, living confirmation and termination
 * function of configuration management function block.
 *
 * @author NTT
 *
 */
public class NodeManager implements FunctionBlockBase {

  protected static final MsfLogger logger = MsfLogger.getInstance(NodeManager.class);

  protected static NodeManager instance = null;

  protected NodeManager() {

  }

  /**
   * Get the instance of NodeManager.
   *
   * @return NodeManager instance
   */
  public static NodeManager getInstance() {
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
