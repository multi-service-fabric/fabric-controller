
package msf.mfc.db;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import msf.mfc.common.data.MfcAsyncRequest;
import msf.mfc.common.data.MfcAsyncRequestsForLower;
import msf.mfc.common.data.MfcAsyncRequestsForLowerPK;
import msf.mfc.common.data.MfcAsyncRequestsForLowerRollback;
import msf.mfc.common.data.MfcClusterLinkIf;
import msf.mfc.common.data.MfcClusterLinkIfId;
import msf.mfc.common.data.MfcCpId;
import msf.mfc.common.data.MfcCpIdPK;
import msf.mfc.common.data.MfcEquipment;
import msf.mfc.common.data.MfcEsiId;
import msf.mfc.common.data.MfcEsiIdPK;
import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2CpPK;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3CpPK;
import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.common.data.MfcLagLink;
import msf.mfc.common.data.MfcPhysicalLink;
import msf.mfc.common.data.MfcSliceId;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.common.data.MfcSystemStatus;
import msf.mfc.common.data.MfcVrfId;
import msf.mfc.db.dao.common.MfcAsyncRequestsDao;
import msf.mfc.db.dao.common.MfcAsyncRequestsForLowerDao;
import msf.mfc.db.dao.common.MfcAsyncRequestsForLowerRollbackDao;
import msf.mfc.db.dao.common.MfcSystemStatusDao;
import msf.mfc.db.dao.slices.MfcCpIdDao;
import msf.mfc.db.dao.slices.MfcEsiDao;
import msf.mfc.db.dao.slices.MfcSliceIdDao;
import msf.mfc.db.dao.slices.MfcVrfIdDao;
import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.data.AsyncRequestsForLower;
import msf.mfcfc.common.data.AsyncRequestsForLowerPK;
import msf.mfcfc.common.data.AsyncRequestsForLowerRollback;
import msf.mfcfc.common.data.CpId;
import msf.mfcfc.common.data.CpIdPK;
import msf.mfcfc.common.data.EsiId;
import msf.mfcfc.common.data.EsiIdPK;
import msf.mfcfc.common.data.SliceId;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.data.VrfId;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.DbManager;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.db.dao.common.AsyncRequestsDao;
import msf.mfcfc.db.dao.common.AsyncRequestsForLowerDao;
import msf.mfcfc.db.dao.common.AsyncRequestsForLowerRollbackDao;
import msf.mfcfc.db.dao.common.SystemStatusDao;
import msf.mfcfc.db.dao.slices.CpIdDao;
import msf.mfcfc.db.dao.slices.EsiDao;
import msf.mfcfc.db.dao.slices.SliceIdDao;
import msf.mfcfc.db.dao.slices.VrfIdDao;

/**
 * Database management class.
 *
 * @author NTT
 *
 */
public final class MfcDbManager extends DbManager {

  protected static final MsfLogger logger = MsfLogger.getInstance(MfcDbManager.class);

  private static final Comparator<MfcL2Slice> COMPARATOR_L2SLICE = new Comparator<MfcL2Slice>() {
    @Override
    public int compare(MfcL2Slice o1, MfcL2Slice o2) {
      return o1.getSliceId().compareTo(o2.getSliceId());
    }
  };

  private static final Comparator<MfcL3Slice> COMPARATOR_L3SLICE = new Comparator<MfcL3Slice>() {
    @Override
    public int compare(MfcL3Slice o1, MfcL3Slice o2) {
      return o1.getSliceId().compareTo(o2.getSliceId());
    }
  };

  private MfcDbManager() {

  }

  /**
   * Get the instance of MfcDbManager.
   *
   * @return FcDbManager instance
   */
  public static MfcDbManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new MfcDbManager();
      }
      return (MfcDbManager) instance;
    } finally {
      logger.methodEnd();

    }
  }

  @Override
  public boolean start() {
    try {
      logger.methodStart();
      try {
        return SessionWrapper.buildSessionFactory(createHibernateConfiguration());
      } catch (HibernateException he) {
        logger.error("Error build SessionFactory.", he);
        return false;
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the MfcL2Slice record lock <br>
   * <br>
   * Lock a record of entity list until transaction completes.
   *
   * @param l2slices
   *          {@link MfcL2Slice} entity list
   * @param sessionWrapper
   *          Session
   * @throws MsfException
   *           DB exclusive control error
   */
  public void getL2SlicesLock(List<MfcL2Slice> l2slices, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2slices", "sessionWrapper" }, new Object[] { l2slices, sessionWrapper });
      if (CollectionUtils.isEmpty(l2slices)) {
        throw new IllegalArgumentException("l2slices is empty.");
      }
      l2slices.sort(COMPARATOR_L2SLICE);
      Session session = sessionWrapper.getSession();

      for (MfcL2Slice l2Slice : new LinkedHashSet<>(l2slices)) {
        getLock(MfcL2Slice.class, l2Slice.getSliceId(), session);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the MfcL3Slice record lock <br>
   * <br>
   * Lock until transaction completes for a record of entity list.
   *
   * @param l3slices
   *          {@link MfcL3Slice} entity list
   * @param sessionWrapper
   *          Session
   * @throws MsfException
   *           DB exclusive control error
   */
  public void getL3SlicesLock(List<MfcL3Slice> l3slices, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3slices", "sessionWrapper" }, new Object[] { l3slices, sessionWrapper });
      if (CollectionUtils.isEmpty(l3slices)) {
        throw new IllegalArgumentException("l3slices is empty.");
      }
      l3slices.sort(COMPARATOR_L3SLICE);
      Session session = sessionWrapper.getSession();

      for (MfcL3Slice l3Slice : new LinkedHashSet<>(l3slices)) {
        getLock(MfcL3Slice.class, l3Slice.getSliceId(), session);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get record lock <br>
   * <br>
   * Lock until transaction completes for a record of entity list.
   *
   * @param l2slices
   *          {@link MfcL2Slice} entity list
   * @param l3slices
   *          {@link MfcL3Slice} ebtity list
   * @param sessionWrapper
   *          Session
   * @throws MsfException
   *           DB exclusive control error
   */
  public void getResourceLock(List<MfcL2Slice> l2slices, List<MfcL3Slice> l3slices, SessionWrapper sessionWrapper)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "l2slices", "l3slices", "sessionWrapper" },
          new Object[] { l2slices, l3slices, sessionWrapper });

      boolean verify = false;

      if (CollectionUtils.isNotEmpty(l2slices)) {
        getL2SlicesLock(l2slices, sessionWrapper);
        verify = true;
      }

      if (CollectionUtils.isNotEmpty(l3slices)) {
        getL3SlicesLock(l3slices, sessionWrapper);
        verify = true;
      }

      if (!verify) {
        throw new IllegalArgumentException("Resources is empty.");
      }
    } finally {
      logger.methodEnd();
    }
  }

  private static Configuration createHibernateConfiguration() {
    try {
      logger.methodStart();

      String path = ConfigManager.getInstance().getHibernateConfFilePath();
      Configuration configuration = new Configuration().configure(new File(path));

      configuration.addAnnotatedClass(MfcAsyncRequest.class);
      configuration.addAnnotatedClass(MfcAsyncRequestsForLower.class);
      configuration.addAnnotatedClass(MfcAsyncRequestsForLowerPK.class);
      configuration.addAnnotatedClass(MfcAsyncRequestsForLowerRollback.class);
      configuration.addAnnotatedClass(MfcClusterLinkIf.class);
      configuration.addAnnotatedClass(MfcClusterLinkIfId.class);
      configuration.addAnnotatedClass(MfcCpId.class);
      configuration.addAnnotatedClass(MfcCpIdPK.class);
      configuration.addAnnotatedClass(MfcEquipment.class);
      configuration.addAnnotatedClass(MfcEsiId.class);
      configuration.addAnnotatedClass(MfcEsiIdPK.class);
      configuration.addAnnotatedClass(MfcL2Cp.class);
      configuration.addAnnotatedClass(MfcL2CpPK.class);
      configuration.addAnnotatedClass(MfcL2Slice.class);
      configuration.addAnnotatedClass(MfcL3Cp.class);
      configuration.addAnnotatedClass(MfcL3CpPK.class);
      configuration.addAnnotatedClass(MfcL3Slice.class);
      configuration.addAnnotatedClass(MfcLagLink.class);
      configuration.addAnnotatedClass(MfcPhysicalLink.class);
      configuration.addAnnotatedClass(MfcSliceId.class);
      configuration.addAnnotatedClass(MfcSwCluster.class);
      configuration.addAnnotatedClass(MfcSystemStatus.class);
      configuration.addAnnotatedClass(MfcVrfId.class);
      return configuration;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public AsyncRequestsDao createAsyncRequestsDao() {
    return new AsyncRequestsDao() {
      private MfcAsyncRequestsDao dao = new MfcAsyncRequestsDao();

      @Override
      public AsyncRequest read(SessionWrapper session, String operationId) throws MsfException {
        MfcAsyncRequest entity = dao.read(session, operationId);
        if (entity != null) {
          return entity.getCommonEntity();
        } else {
          return null;
        }
      }

      @Override
      public void updateList(SessionWrapper session, Integer beforeStatus, Integer afterStatus, String afterSubStatus)
          throws MsfException {
        dao.updateList(session, beforeStatus, afterStatus, afterSubStatus);
      }

      @Override
      public List<AsyncRequest> readList(SessionWrapper session) throws MsfException {
        List<AsyncRequest> list = new ArrayList<>();
        for (MfcAsyncRequest request : dao.readList(session)) {
          list.add(request.getCommonEntity());
        }
        return list;
      }

      @Override
      public void create(SessionWrapper session, AsyncRequest entity) throws MsfException {
        dao.create(session, new MfcAsyncRequest(entity));
      }

      @Override
      public void update(SessionWrapper session, AsyncRequest entity) throws MsfException {
        MfcAsyncRequest updateEntity = dao.read(session, entity.getOperationId());
        updateEntity.setCommonEntity(entity);
        dao.update(session, updateEntity);
      }

      @Override
      public void delete(SessionWrapper session, String operationId) throws MsfException {
        dao.delete(session, operationId);
      }

      @Override
      public void delete(SessionWrapper session, Timestamp targetTime) throws MsfException {
        dao.delete(session, targetTime);
      }
    };
  }

  @Override
  public AsyncRequestsForLowerDao createAsyncRequestsForLowerDao() {
    return new AsyncRequestsForLowerDao() {
      private MfcAsyncRequestsForLowerDao dao = new MfcAsyncRequestsForLowerDao();

      @Override
      public void create(SessionWrapper session, AsyncRequestsForLower entity) throws MsfException {
        dao.create(session, new MfcAsyncRequestsForLower(entity));
      }

      @Override
      public AsyncRequestsForLower read(SessionWrapper session, AsyncRequestsForLowerPK primaryKey)
          throws MsfException {
        MfcAsyncRequestsForLower entity = dao.read(session, new MfcAsyncRequestsForLowerPK(primaryKey));
        if (entity != null) {
          return entity.getCommonEntity(null);
        } else {
          return null;
        }
      }

    };
  }

  @Override
  public AsyncRequestsForLowerRollbackDao createAsyncRequestsForLowerRollbackDao() {
    return new AsyncRequestsForLowerRollbackDao() {
      private MfcAsyncRequestsForLowerRollbackDao dao = new MfcAsyncRequestsForLowerRollbackDao();

      @Override
      public void create(SessionWrapper session, AsyncRequestsForLowerRollback entity) throws MsfException {
        dao.create(session, new MfcAsyncRequestsForLowerRollback(entity));
      }

      @Override
      public AsyncRequestsForLowerRollback read(SessionWrapper session, AsyncRequestsForLowerPK pk)
          throws MsfException {
        MfcAsyncRequestsForLowerRollback entity = dao.read(session, new MfcAsyncRequestsForLowerPK(pk));
        if (entity != null) {
          return entity.getCommonEntity(null);
        } else {
          return null;
        }
      }
    };
  }

  @Override
  public SystemStatusDao createSystemStatusDao() {
    return new SystemStatusDao() {
      private MfcSystemStatusDao dao = new MfcSystemStatusDao();

      @Override
      public void create(SessionWrapper session, SystemStatus entity) throws MsfException {
        dao.create(session, new MfcSystemStatus(entity));
      }

      @Override
      public SystemStatus read(SessionWrapper session, Integer systemId) throws MsfException {
        MfcSystemStatus entity = dao.read(session, systemId);
        if (entity != null) {
          return entity.getCommonEntity();
        } else {
          return null;
        }
      }

      @Override
      public void update(SessionWrapper session, SystemStatus entity) throws MsfException {
        MfcSystemStatus updateEntity = dao.read(session, entity.getSystemId());
        updateEntity.setCommonEntity(entity);
        dao.update(session, updateEntity);
      }

      @Override
      public void delete(SessionWrapper session, Integer systemId) throws MsfException {
        dao.delete(session, systemId);
      }
    };
  }

  @Override
  public CpIdDao createCpIdDao() {
    return new CpIdDao() {
      private MfcCpIdDao dao = new MfcCpIdDao();

      @Override
      public void create(SessionWrapper session, CpId entity) throws MsfException {
        dao.create(session, new MfcCpId(entity));
      }

      @Override
      public CpId read(SessionWrapper session, CpIdPK pk) throws MsfException {
        MfcCpId entity = dao.read(session, new MfcCpIdPK(pk));
        if (entity != null) {
          return entity.getCommonEntity();
        } else {
          return null;
        }
      }

      @Override
      public void update(SessionWrapper session, CpId entity) throws MsfException {
        MfcCpId updateEntity = dao.read(session, new MfcCpIdPK(entity.getId()));
        updateEntity.setCommonEntity(entity);
        dao.update(session, updateEntity);
      }

      @Override
      public void delete(SessionWrapper session, CpIdPK pk) throws MsfException {
        dao.delete(session, new MfcCpIdPK(pk));
      }
    };
  }

  @Override
  public EsiDao createEsiDao() {
    return new EsiDao() {
      private MfcEsiDao dao = new MfcEsiDao();

      @Override
      public void create(SessionWrapper session, EsiId entity) throws MsfException {
        dao.create(session, new MfcEsiId(entity));
      }

      @Override
      public EsiId read(SessionWrapper session, EsiIdPK pk) throws MsfException {
        MfcEsiId entity = dao.read(session, new MfcEsiIdPK(pk));
        if (entity != null) {
          return entity.getCommonEntity();
        } else {
          return null;
        }
      }

      @Override
      public void update(SessionWrapper session, EsiId entity) throws MsfException {
        MfcEsiId updateEntity = dao.read(session, new MfcEsiIdPK(entity.getId()));
        updateEntity.setCommonEntity(entity);
        dao.update(session, updateEntity);
      }

      @Override
      public void delete(SessionWrapper session, EsiIdPK pk) throws MsfException {
        dao.delete(session, new MfcEsiIdPK(pk));
      }
    };
  }

  @Override
  public SliceIdDao createSliceIdDao() {
    return new SliceIdDao() {
      private MfcSliceIdDao dao = new MfcSliceIdDao();

      @Override
      public void create(SessionWrapper session, SliceId entity) throws MsfException {
        dao.create(session, new MfcSliceId(entity));
      }

      @Override
      public SliceId read(SessionWrapper session, Integer pk) throws MsfException {
        MfcSliceId entity = dao.read(session, pk);
        if (entity != null) {
          return entity.getCommonEntity();
        } else {
          return null;
        }
      }

      @Override
      public void update(SessionWrapper session, SliceId entity) throws MsfException {
        MfcSliceId updateEntity = dao.read(session, entity.getLayerType());
        updateEntity.setCommonEntity(entity);
        dao.update(session, updateEntity);
      }

      @Override
      public void delete(SessionWrapper session, Integer pk) throws MsfException {
        dao.delete(session, pk);
      }
    };
  }

  @Override
  public VrfIdDao createVrfIdDao() {
    return new VrfIdDao() {
      private MfcVrfIdDao dao = new MfcVrfIdDao();

      @Override
      public void create(SessionWrapper session, VrfId entity) throws MsfException {
        dao.create(session, new MfcVrfId(entity));
      }

      @Override
      public VrfId read(SessionWrapper session, Integer pk) throws MsfException {
        MfcVrfId entity = dao.read(session, pk);
        if (entity != null) {
          return entity.getCommonEntity();
        } else {
          return null;
        }
      }

      @Override
      public void update(SessionWrapper session, VrfId entity) throws MsfException {
        MfcVrfId updateEntity = dao.read(session, entity.getLayerType());
        updateEntity.setCommonEntity(entity);
        dao.update(session, updateEntity);
      }

      @Override
      public void delete(SessionWrapper session, Integer pk) throws MsfException {
        dao.delete(session, pk);
      }
    };
  }
}
