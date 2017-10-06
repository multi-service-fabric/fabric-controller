package msf.fc.core;

import msf.fc.common.FunctionBlockBase;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.async.AsyncExecutor;
import msf.fc.core.operation.OperationManager;
import msf.fc.core.status.SystemStatusManager;

public final class CoreManager implements FunctionBlockBase {

  private static final MsfLogger logger = MsfLogger.getInstance(CoreManager.class);

  private static final CoreManager instance = new CoreManager();

  private CoreManager() {
  }

  public static CoreManager getInstance() {
    return instance;
  }

  @Override
  public boolean start() {
    logger.methodStart();

    SystemStatusManager ssManager = SystemStatusManager.getInstance();
    try {
      ssManager.initStatus();
    } catch (MsfException msfEx) {
      logger.error("Init SystemStatusManager failed.", msfEx);
      return false;
    }

    AsyncExecutor.getInstance();
    OperationManager.getInstance();

    logger.methodEnd();
    return true;
  }

  @Override
  public boolean checkStatus() {
    logger.methodStart();


    logger.methodEnd();
    return true;
  }

  @Override
  public boolean stop() {
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


    logger.methodEnd();
    return true;
  }

}
