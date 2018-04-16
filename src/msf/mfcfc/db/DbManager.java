
package msf.mfcfc.db;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

import msf.mfcfc.common.FunctionBlockBase;
import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.dao.common.AsyncRequestsDao;
import msf.mfcfc.db.dao.common.AsyncRequestsForLowerDao;
import msf.mfcfc.db.dao.common.AsyncRequestsForLowerRollbackDao;
import msf.mfcfc.db.dao.common.SystemStatusDao;
import msf.mfcfc.db.dao.slices.CpIdDao;
import msf.mfcfc.db.dao.slices.EsiDao;
import msf.mfcfc.db.dao.slices.SliceIdDao;
import msf.mfcfc.db.dao.slices.VlanIfIdDao;
import msf.mfcfc.db.dao.slices.VrfIdDao;

/**
 * Database management class.
 *
 * @author NTT
 *
 */
public class DbManager implements FunctionBlockBase {

  protected static final MsfLogger logger = MsfLogger.getInstance(DbManager.class);

  protected static DbManager instance = null;

  protected DbManager() {

  }

  /**
   * Get the instance of {@link DbManager}. <br>
   * <br>
   * Make sure to initialize the instance with child class before calling.
   *
   * @return {@link DbManager} instance
   */
  public static DbManager getInstance() {
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
      return SessionWrapper.closeSessionFactory();
    } finally {
      logger.methodEnd();
    }
  }

  protected <E> void getLock(Class<E> cls, Serializable pk, Session session) throws MsfException {
    try {
      logger.methodStart(new String[] { "cls", "pk", "session" }, new Object[] { cls, pk, session });

      ConfigManager cm = ConfigManager.getInstance();

      for (int i = 0; i < cm.getLockRetryNum(); i++) {
        if (tryLock(cls, pk)) {
          try {
            if (Objects.isNull(session.load(cls, pk, LockMode.UPGRADE_NOWAIT))) {
              throw new MsfException(ErrorCode.EXCLUSIVE_CONTROL_ERROR, "Error get Lock.");
            }

            return;
          } catch (HibernateException he) {

            logger.error("lock was interrupted by another session.", he);
            throw new MsfException(ErrorCode.EXCLUSIVE_CONTROL_ERROR, "lock was interrupted by another session.");
          }
        }

        try {

          Thread.sleep(cm.getLockTimeout());
        } catch (InterruptedException ie) {
          logger.warn("Error sleep.", ie);
        }
      }

      throw new MsfException(ErrorCode.EXCLUSIVE_CONTROL_ERROR, "Error get Lock.");
    } finally {
      logger.methodEnd();
    }
  }

  private <E> boolean tryLock(Class<E> cls, Serializable pk) throws MsfException {
    logger.methodStart();

    SessionWrapper dummySession = new SessionWrapper();
    try {
      dummySession.openSession();

      if (Objects.isNull(dummySession.getSession().load(cls, pk, LockMode.UPGRADE_NOWAIT))) {
        return false;
      }

      return true;
    } catch (ObjectNotFoundException onfe) {
      logger.error("Error try Lock.", onfe);

      throw new MsfException(ErrorCode.EXCLUSIVE_CONTROL_ERROR, "Error try Lock.");
    } catch (HibernateException he) {
      logger.warn("Error try Lock.", he);

      return false;
    } finally {
      dummySession.closeSession();
      logger.methodEnd();
    }
  }

  public AsyncRequestsDao createAsyncRequestsDao() {
    return null;
  }

  public AsyncRequestsForLowerDao createAsyncRequestsForLowerDao() {
    return null;
  }

  public AsyncRequestsForLowerRollbackDao createAsyncRequestsForLowerRollbackDao() {
    return null;
  }

  public SystemStatusDao createSystemStatusDao() {
    return null;
  }

  public CpIdDao createCpIdDao() {
    return null;
  }

  public EsiDao createEsiDao() {
    return null;
  }

  public SliceIdDao createSliceIdDao() {
    return null;
  }

  public VlanIfIdDao createVlanIfIdDao() {
    return null;
  }

  public VrfIdDao createVrfIdDao() {
    return null;
  }
}
