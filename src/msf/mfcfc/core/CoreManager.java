
package msf.mfcfc.core;

import java.util.ArrayList;
import java.util.List;

import msf.mfcfc.common.FunctionBlockBase;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.async.AsyncExecutor;
import msf.mfcfc.core.async.SendRequestExecutor;
import msf.mfcfc.core.operation.OperationManager;
import msf.mfcfc.core.status.SystemStatusManager;

/**
 * Class to provide the initialization, living confirmation and termination
 * function of system basic function block.
 *
 * @author NTT
 *
 */
public final class CoreManager implements FunctionBlockBase {

  private static final MsfLogger logger = MsfLogger.getInstance(CoreManager.class);

  private static final CoreManager instance = new CoreManager();

  private static List<FunctionBlockBase> extensionsList = new ArrayList<FunctionBlockBase>();

  private CoreManager() {
  }

  /**
   * Get the instance of CoreManager.
   *
   * @return CoreManager instance
   */
  public static CoreManager getInstance() {
    return instance;
  }

  /**
   * Initialize the system basic function block.
   */
  @Override
  public boolean start() {
    try {
      logger.methodStart();

      SystemStatusManager ssManager = SystemStatusManager.getInstance();
      try {
        ssManager.initStatus();
      } catch (MsfException msfEx) {
        logger.error("Init SystemStatusManager failed.", msfEx);
        return false;
      }

      AsyncExecutor.getInstance();
      SendRequestExecutor.getInstance();
      OperationManager.getInstance();

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Execute the living confirmation of system basic function block.
   */
  @Override
  public boolean checkStatus() {
    try {
      logger.methodStart();

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Terminate the basic system function block.
   */
  @Override
  public boolean stop() {
    try {
      logger.methodStart();

      AsyncExecutor executor = AsyncExecutor.getInstance();
      try {

        if (!executor.shutdown()) {
          logger.warn("Failed to shudwon AsyncExecutor.(TimeOut)");
        } else {
          logger.debug("AsyncExecutor shutdown is success.");
        }
      } catch (InterruptedException ex) {
        logger.warn("Failed to shutdown AsyncExecutor.", ex);
      }

      return true;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Add an instance of an extension function.
   *
   * @param extensionFunction
   *          Instance of an extension function
   */
  public void addExtensionFunction(FunctionBlockBase extensionFunction) {

    extensionsList.add(extensionFunction);
  }

  /**
   * Get instances of extension functions.<BR>
   * Function configuration information is returned in the order set by
   * setExtensionFunction ().
   *
   * @return Function configuration information
   */
  public List<FunctionBlockBase> getExtensionFunctions() {

    return extensionsList;
  }

}
