
package msf.mfcfc.db;

import static org.hibernate.resource.transaction.spi.TransactionStatus.*;

import java.util.Objects;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import msf.mfcfc.common.constant.EcEmControlStatus;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;

/**
 * Class for DB session-related process.
 *
 * @author NTT
 */
public class SessionWrapper {

  private static final MsfLogger logger = MsfLogger.getInstance(SessionWrapper.class);

  private static final Object LOCK = new Object();

  private static SessionFactory sessionFactory = null;
  private Session currentSession;

  /**
   * Open the session. <br>
   * <br>
   * Never fail to invoke before using each function of SessionWrapper.
   *
   * @return SessionWrapper instance
   * @throws MsfException
   *           DB access error
   */
  public SessionWrapper openSession() throws MsfException {
    try {
      logger.methodStart();
      synchronized (LOCK) {
        if (currentSession == null) {
          currentSession = sessionFactory.openSession();
        }
      }
      return this;
    } catch (HibernateException he) {
      logger.error("Error open Session.", he);
      throw new MsfException(ErrorCode.DATABASE_CONNECTION_ERROR, "Error open Session.");
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the session. <br>
   * <br>
   * Use this method only in {@link DbManager} class or each DAO class.
   *
   * @return session
   * @throws MsfException
   *           DB access error
   */
  public Session getSession() throws MsfException {
    try {
      logger.methodStart();
      Objects.requireNonNull(currentSession, "Session is null.");
      return currentSession;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Close the session. <br>
   * <br>
   * Never fail to invoke when ending the use of each SessionWrapper function.
   *
   * @throws MsfException
   *           DB access error
   */
  public void closeSession() {
    try {
      logger.methodStart();
      if (currentSession == null) {
        return;
      }

      currentSession.close();
    } catch (HibernateException he) {
      logger.warn("Error close Session.", he);
    } finally {
      currentSession = null;
      logger.methodEnd();
    }
  }

  /**
   * Start the transaction. <br>
   * <br>
   * Never fail to call this method prior to invoking commit/rollback.
   *
   * @return SessionWrapper instance
   * @throws MsfException
   *           DB access error
   */
  public SessionWrapper beginTransaction() throws MsfException {
    try {
      logger.methodStart();
      Objects.requireNonNull(currentSession, "Session is null.");
      currentSession.beginTransaction();
      return this;
    } catch (HibernateException he) {
      logger.error("Error begin Transaction.", he);
      throw new MsfException(ErrorCode.DATABASE_TRANSACTION_ERROR, "Error begin Transaction.");
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Commit the transaction.
   *
   * @throws MsfException
   *           DB access error
   */
  public void commit() throws MsfException {
    try {
      logger.methodStart();
      Objects.requireNonNull(currentSession, "Session is null.");
      currentSession.getTransaction().commit();
    } catch (HibernateException he) {
      logger.error("Error commit Transaction.", he);
      throw new MsfException(ErrorCode.DATABASE_TRANSACTION_ERROR, "Error commit Transaction.");
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Commit the transaction. <br>
   * <br>
   * Commit procedures for taking the control response status of EC/EM as the
   * parameter. <br>
   * If the commit procedure fails, replace each exception to throw to the
   * caller according to the status of EC/EM control response.
   *
   * @param status
   *          EC/EM control response status
   * @throws MsfException
   *           Control error
   */
  public void commit(EcEmControlStatus status) throws MsfException {
    try {
      logger.methodStart(new String[] { "status" }, new Object[] { status });
      commit();
    } catch (MsfException me) {
      String msg = logger.error("Error commit Transaction. EcEmControlStatus = {0}", me, status);
      switch (status) {
        case EM_SUCCESS_BUT_EC_FAILED:
          throw new MsfException(ErrorCode.FC_EC_CONTROL_ERROR_EM_CONTROL_COMPLETED, msg);

        case SUCCESS:
          throw new MsfException(ErrorCode.FC_CONTROL_ERROR_EC_EM_CONTROL_COMPLETED, msg);

        case FAILED:
        default:
          throw new IllegalArgumentException(msg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Commit the transaction. <br>
   * <br>
   * Method for executing the commit procedure on MFC. <br>
   * If the commit procedure fails, replace each exception to throw to the
   * caller according to the request.
   *
   * @param requestUri
   *          Request URI
   * @throws MsfException
   *           Control error
   */
  public void commit(MfcFcRequestUri requestUri) throws MsfException {
    try {
      logger.methodStart(new String[] { "requestUri" }, new Object[] { requestUri });
      commit();
    } catch (MsfException me) {
      String msg = logger.error("Error commit Transaction. requestUri = {0}", me, requestUri);
      switch (requestUri) {
        case CLUSTER_LINK_IF_CREATE:
        case CLUSTER_LINK_IF_DELETE:
        case CP_CREATE_DELETE:
        case CP_CREATE:
        case CP_DELETE:
        case SLICE_UPDATE:

          throw new MsfException(ErrorCode.MFC_CONTROL_ERROR_FC_EC_EM_CONTROL_COMPLETED, msg);
        case EQUIPMENT_CREATE:
        case EQUIPMENT_DELETE:

          throw new MsfException(ErrorCode.MFC_CONTROL_ERROR_FC_EC_CONTROL_COMPLETED, msg);
        case SLICE_CREATE:
        case SLICE_DELETE:

          throw new MsfException(ErrorCode.MFC_CONTROL_ERROR_FC_CONTROL_COMPLETED, msg);
        default:

          throw me;
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Commit the transaction. <br>
   * <br>
   * Commit procedures for taking the control response status of EC/EM to the
   * parameter in node reduction. <br>
   * If the commit procedure fails, replace each exception to throw to the
   * caller according to the status of EC/EM control response.
   *
   * @param status
   *          EC/EM control response status
   * @throws MsfException
   *           Control error
   */
  public void commitNodeDelete(EcEmControlStatus status) throws MsfException {
    try {
      logger.methodStart(new String[] { "status" }, new Object[] { status });
      commit();
    } catch (MsfException me) {
      String errorMsg = "Error commit Transaction.";
      switch (status) {
        case EM_SUCCESS_BUT_EC_FAILED:
          throw new MsfException(ErrorCode.FC_EC_CONTROL_ERROR_EM_CONTROL_COMPLETED, errorMsg);

        case SUCCESS:
          throw new MsfException(ErrorCode.FC_CONTROL_ERROR_EC_EM_CONTROL_COMPLETED, errorMsg);

        case FAILED:
          throw new MsfException(ErrorCode.EC_CONTROL_ERROR, errorMsg);

        default:
          throw new IllegalArgumentException(errorMsg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Roll back the transaction.
   *
   * @throws MsfException
   *           DB access error
   */
  public void rollback() throws MsfException {
    try {
      logger.methodStart();
      Objects.requireNonNull(currentSession, "Session is null.");

      if (currentSession.getTransaction().getStatus().isOneOf(new TransactionStatus[] { ACTIVE, MARKED_ROLLBACK })) {
        currentSession.getTransaction().rollback();
      }
    } catch (HibernateException he) {
      logger.error("Error rollback Transaction.", he);
      throw new MsfException(ErrorCode.DATABASE_TRANSACTION_ERROR, "Error rollback Transaction.");
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Execute the session initialization process. <br>
   * <br>
   *
   * @param configuration
   *          Make sure to use only in Hibernate config {@link DbManager} class.
   *
   * @return true: initialization success/false: initialization failure
   */
  public static boolean buildSessionFactory(Configuration configuration) {
    try {
      logger.methodStart();
      synchronized (LOCK) {
        if (sessionFactory == null) {
          sessionFactory = configuration.buildSessionFactory();
        }
      }
      return true;
    } catch (HibernateException he) {
      logger.error("Error build SessionFactory.", he);
      return false;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Execute the termination of session. <br>
   * <br>
   * Use only in {@link DbManager} class.
   *
   * @return true termination success /false termination fail
   */
  public static boolean closeSessionFactory() {
    try {
      logger.methodStart();
      synchronized (LOCK) {
        if (sessionFactory != null) {
          sessionFactory.close();
          sessionFactory = null;
        }
      }
      return true;
    } catch (HibernateException he) {
      logger.error("Error close SessionFactory.", he);
      return false;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Execute the acquisition of data that the lazy load is set.
   *
   * @param <E>
   *          Table entity class
   * @param entity
   *          Table entity
   * @return table entity
   * @throws MsfException
   *           SQL execution error at lazy loading
   */
  public static <E> E getLazyLoadData(E entity) throws MsfException {
    try {
      if (Objects.isNull(entity)) {
        return null;
      }

      entity.toString();

      return entity;
    } catch (HibernateException he) {
      logger.error("Database lazy load failed.", he);
      throw new MsfException(ErrorCode.DATABASE_OPERATION_ERROR, "Database lazy load failed.");
    }
  }
}
