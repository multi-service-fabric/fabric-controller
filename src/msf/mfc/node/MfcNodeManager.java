
package msf.mfc.node;

import java.util.List;

import msf.mfc.common.data.MfcClusterLinkIfId;
import msf.mfc.db.dao.clusters.MfcClusterLinkIfIdDao;
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
public final class MfcNodeManager extends NodeManager {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcNodeManager.class);

  private static final Object MFC_EQUIPMENT_CREATE_LOCK_OBJECT = new Object();

  private static final Object MFC_EQUIPMENT_DELETE_LOCK_OBJECT = new Object();

  private static final Object MFC_CLUSTER_LINK_CREATE_LOCK_OBJECT = new Object();

  private MfcNodeManager() {

  }

  /**
   * Get the instance of MfcNodeManager.
   *
   * @return MfcNodeManager instance
   */
  public static MfcNodeManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new MfcNodeManager();
      }
      return (MfcNodeManager) instance;
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

      MfcClusterLinkIfIdDao mfcClusterLinkIfIdDao = new MfcClusterLinkIfIdDao();
      List<MfcClusterLinkIfId> mfcClusterLinkIfIds = mfcClusterLinkIfIdDao.readList(sessionWrapper);
      if (mfcClusterLinkIfIds.isEmpty()) {
        MfcClusterLinkIfId mfcClusterLinkIfId = new MfcClusterLinkIfId();
        mfcClusterLinkIfId.setNextId(1);
        mfcClusterLinkIfIdDao.create(sessionWrapper, mfcClusterLinkIfId);
        sessionWrapper.commit();
      }

      return true;
    } catch (Exception ex) {
      logger.error("Failed to start MfcNode Manager.", ex);
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
   * @return EQUIPMENT_CREATE_LOCK_OBJECT
   */
  public Object getMfcEquipmentCreateLockObject() {
    try {
      logger.methodStart();
      return MFC_EQUIPMENT_CREATE_LOCK_OBJECT;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the exclusive control object for device model information deletion.
   *
   * @return EQUIPMENT_DELETE_LOCK_OBJECT
   */
  public Object getMfcEquipmentDeleteLockObject() {
    try {
      logger.methodStart();
      return MFC_EQUIPMENT_DELETE_LOCK_OBJECT;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the exclusive control object for generating the inter-cluster link IF.
   *
   * @return MFC_CLUSTER_LINK_CREATE_LOCK_OBJECT
   */
  public Object getMfcClusterLinkCreateLockObject() {
    try {
      logger.methodStart();
      return MFC_CLUSTER_LINK_CREATE_LOCK_OBJECT;
    } finally {
      logger.methodEnd();
    }
  }

}
