package msf.fc.node;

import msf.fc.common.FunctionBlockBase;
import msf.fc.common.log.MsfLogger;

public final class NodeManager implements FunctionBlockBase {
  private static final NodeManager instance = new NodeManager();
  private static final MsfLogger logger = MsfLogger.getInstance(NodeManager.class);
  private static final Object EQUIPMENT_CREATE_LOCK_OBJECT = new Object();
  private static final Object EQUIPMENT_DELETE_LOCK_OBJECT = new Object();
  private static final Object NODE_CREATE_AND_DELETE_LOCK_OBJECT = new Object();

  private NodeManager() {

  }

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
    try {
      logger.methodStart();
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean checkStatus() {
    try {
      logger.methodStart();
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public boolean stop() {
    try {
      logger.methodStart();
      return true;
    } finally {
      logger.methodEnd();
    }
  }

  public Object getEquipmentCreateLockObject() {
    try {
      logger.methodStart();
      return EQUIPMENT_CREATE_LOCK_OBJECT;
    } finally {
      logger.methodEnd();
    }
  }

  public Object getEquipmentDeleteLockObject() {
    try {
      logger.methodStart();
      return EQUIPMENT_DELETE_LOCK_OBJECT;
    } finally {
      logger.methodEnd();
    }
  }

  public Object getNodeCreateAndDeleteLockObject() {
    try {
      logger.methodStart();
      return NODE_CREATE_AND_DELETE_LOCK_OBJECT;
    } finally {
      logger.methodEnd();
    }
  }

}
