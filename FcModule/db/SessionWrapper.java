package msf.fc.db;

import static org.hibernate.resource.transaction.spi.TransactionStatus.*;

import java.io.File;
import java.util.Objects;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import msf.fc.common.config.ConfigManager;
import msf.fc.common.constant.EcEmControlStatus;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.data.AsyncRequest;
import msf.fc.common.data.BootErrorMessage;
import msf.fc.common.data.BootErrorMessagePK;
import msf.fc.common.data.CpId;
import msf.fc.common.data.CpIdPK;
import msf.fc.common.data.EdgePoint;
import msf.fc.common.data.EdgePointPK;
import msf.fc.common.data.Equipment;
import msf.fc.common.data.EquipmentIf;
import msf.fc.common.data.EquipmentIfPK;
import msf.fc.common.data.EquipmentPK;
import msf.fc.common.data.InternalLinkIf;
import msf.fc.common.data.L2Cp;
import msf.fc.common.data.L2CpPK;
import msf.fc.common.data.L2Slice;
import msf.fc.common.data.L3Cp;
import msf.fc.common.data.L3CpBgpOption;
import msf.fc.common.data.L3CpBgpOptionPK;
import msf.fc.common.data.L3CpOspfOption;
import msf.fc.common.data.L3CpOspfOptionPK;
import msf.fc.common.data.L3CpPK;
import msf.fc.common.data.L3CpStaticRouteOption;
import msf.fc.common.data.L3CpStaticRouteOptionPK;
import msf.fc.common.data.L3CpVrrpOption;
import msf.fc.common.data.L3CpVrrpOptionPK;
import msf.fc.common.data.L3Slice;
import msf.fc.common.data.LagConstruction;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.data.Rr;
import msf.fc.common.data.RrPK;
import msf.fc.common.data.SliceId;
import msf.fc.common.data.SliceManagerBaseInfo;
import msf.fc.common.data.SwCluster;
import msf.fc.common.data.SystemStatus;
import msf.fc.common.data.TrafficCollectInterval;
import msf.fc.common.data.TrafficHistoryL2slice;
import msf.fc.common.data.TrafficHistoryL2slicePK;
import msf.fc.common.data.TrafficHistoryL3slice;
import msf.fc.common.data.TrafficHistoryL3slicePK;
import msf.fc.common.data.VrfId;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;

public class SessionWrapper {

  private static final MsfLogger logger = MsfLogger.getInstance(SessionWrapper.class);

  private static final Object LOCK = new Object();

  private static SessionFactory sessionFactory = null;
  private Session currentSession;

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

  public Session getSession() throws MsfException {
    try {
      logger.methodStart();
      Objects.requireNonNull(currentSession, "Session is null.");
      return currentSession;
    } finally {
      logger.methodEnd();
    }
  }

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

        default:
          throw new IllegalArgumentException(msg);
      }
    } finally {
      logger.methodEnd();
    }
  }

  public void rollback() throws MsfException {
    try {
      logger.methodStart();
      Objects.requireNonNull(currentSession, "Session is null.");
      if (currentSession.getTransaction().getStatus().isOneOf(
          new TransactionStatus[] { ACTIVE, MARKED_ROLLBACK })) {
        currentSession.getTransaction().rollback();
      }
    } catch (HibernateException he) {
      logger.error("Error rollback Transaction.", he);
      throw new MsfException(ErrorCode.DATABASE_TRANSACTION_ERROR, "Error rollback Transaction.");
    } finally {
      logger.methodEnd();
    }
  }

  public static boolean buildSessionFactory() {
    try {
      logger.methodStart();
      synchronized (LOCK) {
        if (sessionFactory == null) {
          Configuration configuration = createHibernateConfiguration();
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

  private static Configuration createHibernateConfiguration() throws HibernateException {
    try {
      logger.methodStart();
      String path = ConfigManager.getInstance().getHibernateConfFilePath();
      Configuration configuration = new Configuration().configure(new File(path));

      configuration.addAnnotatedClass(AsyncRequest.class);
      configuration.addAnnotatedClass(BootErrorMessage.class);
      configuration.addAnnotatedClass(BootErrorMessagePK.class);
      configuration.addAnnotatedClass(CpId.class);
      configuration.addAnnotatedClass(CpIdPK.class);
      configuration.addAnnotatedClass(EdgePoint.class);
      configuration.addAnnotatedClass(EdgePointPK.class);
      configuration.addAnnotatedClass(Equipment.class);
      configuration.addAnnotatedClass(EquipmentIf.class);
      configuration.addAnnotatedClass(EquipmentIfPK.class);
      configuration.addAnnotatedClass(EquipmentPK.class);
      configuration.addAnnotatedClass(InternalLinkIf.class);
      configuration.addAnnotatedClass(L2Cp.class);
      configuration.addAnnotatedClass(L2CpPK.class);
      configuration.addAnnotatedClass(L2Slice.class);
      configuration.addAnnotatedClass(L3Cp.class);
      configuration.addAnnotatedClass(L3CpBgpOption.class);
      configuration.addAnnotatedClass(L3CpBgpOptionPK.class);
      configuration.addAnnotatedClass(L3CpOspfOption.class);
      configuration.addAnnotatedClass(L3CpOspfOptionPK.class);
      configuration.addAnnotatedClass(L3CpPK.class);
      configuration.addAnnotatedClass(L3CpStaticRouteOption.class);
      configuration.addAnnotatedClass(L3CpStaticRouteOptionPK.class);
      configuration.addAnnotatedClass(L3CpVrrpOption.class);
      configuration.addAnnotatedClass(L3CpVrrpOptionPK.class);
      configuration.addAnnotatedClass(L3Slice.class);
      configuration.addAnnotatedClass(LagConstruction.class);
      configuration.addAnnotatedClass(LagIf.class);
      configuration.addAnnotatedClass(Node.class);
      configuration.addAnnotatedClass(PhysicalIf.class);
      configuration.addAnnotatedClass(Rr.class);
      configuration.addAnnotatedClass(RrPK.class);
      configuration.addAnnotatedClass(SliceId.class);
      configuration.addAnnotatedClass(SliceManagerBaseInfo.class);
      configuration.addAnnotatedClass(SwCluster.class);
      configuration.addAnnotatedClass(SystemStatus.class);
      configuration.addAnnotatedClass(TrafficCollectInterval.class);
      configuration.addAnnotatedClass(TrafficHistoryL2slice.class);
      configuration.addAnnotatedClass(TrafficHistoryL2slicePK.class);
      configuration.addAnnotatedClass(TrafficHistoryL3slice.class);
      configuration.addAnnotatedClass(TrafficHistoryL3slicePK.class);
      configuration.addAnnotatedClass(VrfId.class);
      return configuration;
    } finally {
      logger.methodEnd();
    }
  }

}
