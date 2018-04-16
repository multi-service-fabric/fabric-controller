
package msf.fc.node;

import java.util.List;

import msf.fc.common.data.FcLagIfId;
import msf.fc.db.dao.clusters.FcLagIfIdDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.NodeManager;

/**
 * Class to provide the initialization, living confirmation and termination
 * function of configuration management function block.
 *
 * @author NTT
 *
 */
public final class FcNodeManager extends NodeManager {

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeManager.class);

  private static final Object FC_EQUIPMENT_CREATE_LOCK_OBJECT = new Object();

  private static final Object FC_EQUIPMENT_DELETE_LOCK_OBJECT = new Object();

  private static final Object FC_NODE_CREATE_AND_DELETE_LOCK_OBJECT = new Object();

  private static final Object FC_NODE_UPDATE_LOCK_OBJECT = new Object();

  private FcNodeManager() {

  }

  public static final int FC_LAG_IF_START_ID = 801;

  /**
   * Get the instance of FcNodeManager.
   *
   * @return FcNodeManager instance
   */
  public static FcNodeManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new FcNodeManager();
      }
      return (FcNodeManager) instance;
    } finally {
      logger.methodEnd();

    }
  }

  @Override
  public boolean start() {
    SessionWrapper sessionWrapper = new SessionWrapper();
    try {
      logger.methodStart();
      sessionWrapper.openSession();
      sessionWrapper.beginTransaction();

      FcLagIfIdDao fcLagIfIdDao = new FcLagIfIdDao();
      List<FcLagIfId> fcLagIfIds = fcLagIfIdDao.readList(sessionWrapper);
      if (fcLagIfIds.isEmpty()) {
        FcLagIfId fcLagIfId = new FcLagIfId();
        fcLagIfId.setNextId(FC_LAG_IF_START_ID);
        fcLagIfIdDao.create(sessionWrapper, fcLagIfId);
        sessionWrapper.commit();
      }

      return true;
    } catch (Exception ex) {
      logger.error("Failed to start FcNode Manager.", ex);
      try {
        sessionWrapper.rollback();
      } catch (MsfException me) {

      }
      return false;
    } finally {
      sessionWrapper.closeSession();
      logger.methodEnd();
    }
  }

  /**
   * Get the exclusive control object for device model information registration.
   *
   * @return FC_EQUIPMENT_CREATE_LOCK_OBJECT
   */
  public Object getFcEquipmentCreateLockObject() {
    try {
      logger.methodStart();
      return FC_EQUIPMENT_CREATE_LOCK_OBJECT;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the exclusive control object for device model information deletion.
   *
   * @return FC_EQUIPMENT_DELETE_LOCK_OBJECT
   */
  public Object getFcEquipmentDeleteLockObject() {
    try {
      logger.methodStart();
      return FC_EQUIPMENT_DELETE_LOCK_OBJECT;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the exclusive control object for node addition/reduction.
   *
   * @return FC_NODE_CREATE_AND_DELETE_LOCK_OBJECT
   */
  public Object getFcNodeCreateAndDeleteLockObject() {
    try {
      logger.methodStart();
      return FC_NODE_CREATE_AND_DELETE_LOCK_OBJECT;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the exclusive control object for node modification.
   *
   * @return FC_NODE_UPDATE_LOCK_OBJECT
   */
  public Object getFcNodeUpdateLockObject() {
    try {
      logger.methodStart();
      return FC_NODE_UPDATE_LOCK_OBJECT;
    } finally {
      logger.methodEnd();
    }
  }

}
