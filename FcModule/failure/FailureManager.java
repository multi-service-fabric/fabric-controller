package msf.fc.failure;

import msf.fc.common.FunctionBlockBase;
import msf.fc.common.log.MsfLogger;

public final class FailureManager implements FunctionBlockBase {

  private static final MsfLogger logger = MsfLogger.getInstance(FailureManager.class);
  private static final FailureManager instance = new FailureManager();

  private FailureManager() {
  }

  public static FailureManager getInstance() {
    return instance;
  }

  @Override
  public boolean start() {
    logger.methodStart();
    try {
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean checkStatus() {
    logger.methodStart();
    try {
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean stop() {
    logger.methodStart();
    try {
      return true;
    } finally {
      logger.methodEnd();
    }
  }
}
