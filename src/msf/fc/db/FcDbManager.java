
package msf.fc.db;

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

import msf.fc.common.data.FcAsyncRequest;
import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcClusterLinkIf;
import msf.fc.common.data.FcCpId;
import msf.fc.common.data.FcCpIdPK;
import msf.fc.common.data.FcEdgePoint;
import msf.fc.common.data.FcEquipment;
import msf.fc.common.data.FcEsiId;
import msf.fc.common.data.FcEsiIdPK;
import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL2CpPK;
import msf.fc.common.data.FcL2Slice;
import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcL3CpPK;
import msf.fc.common.data.FcL3Slice;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcLagIfId;
import msf.fc.common.data.FcLeafNode;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.common.data.FcSliceId;
import msf.fc.common.data.FcSwCluster;
import msf.fc.common.data.FcSystemStatus;
import msf.fc.common.data.FcVlanIf;
import msf.fc.common.data.FcVlanIfId;
import msf.fc.common.data.FcVlanIfPK;
import msf.fc.common.data.FcVrfId;
import msf.fc.db.dao.common.FcAsyncRequestsDao;
import msf.fc.db.dao.common.FcSystemStatusDao;
import msf.fc.db.dao.slices.FcCpIdDao;
import msf.fc.db.dao.slices.FcEsiDao;
import msf.fc.db.dao.slices.FcSliceIdDao;
import msf.fc.db.dao.slices.FcVlanIfIdDao;
import msf.fc.db.dao.slices.FcVrfIdDao;
import msf.mfcfc.common.config.ConfigManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.data.AsyncRequest;
import msf.mfcfc.common.data.CpId;
import msf.mfcfc.common.data.CpIdPK;
import msf.mfcfc.common.data.EsiId;
import msf.mfcfc.common.data.EsiIdPK;
import msf.mfcfc.common.data.SliceId;
import msf.mfcfc.common.data.SystemStatus;
import msf.mfcfc.common.data.VlanIfId;
import msf.mfcfc.common.data.VrfId;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.DbManager;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.db.dao.common.AsyncRequestsDao;
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
public final class FcDbManager extends DbManager {

  protected static final MsfLogger logger = MsfLogger.getInstance(FcDbManager.class);

  private static final Comparator<FcL2Slice> COMPARATOR_L2SLICE = new Comparator<FcL2Slice>() {
    @Override
    public int compare(FcL2Slice o1, FcL2Slice o2) {
      return o1.getSliceId().compareTo(o2.getSliceId());
    }
  };

  private static final Comparator<FcL3Slice> COMPARATOR_L3SLICE = new Comparator<FcL3Slice>() {
    @Override
    public int compare(FcL3Slice o1, FcL3Slice o2) {
      return o1.getSliceId().compareTo(o2.getSliceId());
    }
  };

  private static final Comparator<FcNode> COMPARATOR_NODE = new Comparator<FcNode>() {
    @Override
    public int compare(FcNode o1, FcNode o2) {
      return o1.getNodeId() - o2.getNodeId();
    }
  };

  private FcDbManager() {

  }

  /**
   * Get the instance of FcDbManager.
   *
   * @return FcDbManager instance
   */
  public static FcDbManager getInstance() {
    try {
      logger.methodStart();
      if (instance == null) {
        instance = new FcDbManager();
      }
      return (FcDbManager) instance;
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
   * Get the FcL2Slice record lock <br>
   * <br>
   * Lock a record of entity list until transaction completes.
   *
   * @param l2slices
   *          {@link FcL2Slice} entity list
   * @param sessionWrapper
   *          Session
   * @throws MsfException
   *           DB exclusive control error
   */
  public void getL2SlicesLock(List<FcL2Slice> l2slices, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2slices", "sessionWrapper" }, new Object[] { l2slices, sessionWrapper });
      if (CollectionUtils.isEmpty(l2slices)) {
        throw new IllegalArgumentException("l2slices is empty.");
      }
      l2slices.sort(COMPARATOR_L2SLICE);
      Session session = sessionWrapper.getSession();

      for (FcL2Slice l2Slice : new LinkedHashSet<>(l2slices)) {
        getLock(FcL2Slice.class, l2Slice.getSliceId(), session);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the FcL3Slice record lock <br>
   * <br>
   * Lock until transaction completes for a record of entity list.
   *
   * @param l3slices
   *          {@link FcL3Slice} entity list
   * @param sessionWrapper
   *          Session
   * @throws MsfException
   *           DB exclusive control error
   */
  public void getL3SlicesLock(List<FcL3Slice> l3slices, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3slices", "sessionWrapper" }, new Object[] { l3slices, sessionWrapper });
      if (CollectionUtils.isEmpty(l3slices)) {
        throw new IllegalArgumentException("l3slices is empty.");
      }
      l3slices.sort(COMPARATOR_L3SLICE);
      Session session = sessionWrapper.getSession();

      for (FcL3Slice l3Slice : new LinkedHashSet<>(l3slices)) {
        getLock(FcL3Slice.class, l3Slice.getSliceId(), session);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the FcNode (Leaf) record lock <br>
   * <br>
   * Lock until transaction completes for a record of entity list.
   *
   * @param leafs
   *          {@link FcNode} entity list
   * @param sessionWrapper
   *          Session
   * @throws MsfException
   *           DB exclusive control error
   */
  public void getLeafsLock(List<FcNode> leafs, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "leafs", "sessionWrapper" }, new Object[] { leafs, sessionWrapper });
      if (CollectionUtils.isEmpty(leafs)) {
        throw new IllegalArgumentException("leafs is empty.");
      }
      leafs.sort(COMPARATOR_NODE);
      Session session = sessionWrapper.getSession();

      for (FcNode leaf : new LinkedHashSet<>(leafs)) {
        if (NodeType.LEAF != leaf.getNodeTypeEnum()) {
          throw new MsfException(ErrorCode.EXCLUSIVE_CONTROL_ERROR, "node type is " + leaf.getNodeTypeEnum());
        }
        getLock(FcNode.class, leaf.getNodeInfoId(), session);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the FcNode (Spine) record lock <br>
   * <br>
   * Lock until transaction completes for a record of entity list.
   *
   * @param spines
   *          {@link FcNode} entity list
   * @param sessionWrapper
   *          Session
   * @throws MsfException
   *           DB exclusive control error
   */
  public void getSpinesLock(List<FcNode> spines, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "spines", "sessionWrapper" }, new Object[] { spines, sessionWrapper });
      if (CollectionUtils.isEmpty(spines)) {
        throw new IllegalArgumentException("spines is empty.");
      }
      spines.sort(COMPARATOR_NODE);
      Session session = sessionWrapper.getSession();

      for (FcNode spine : new LinkedHashSet<>(spines)) {
        if (NodeType.SPINE != spine.getNodeTypeEnum()) {
          throw new MsfException(ErrorCode.EXCLUSIVE_CONTROL_ERROR, "node type is " + spine.getNodeTypeEnum());
        }
        getLock(FcNode.class, spine.getNodeInfoId(), session);
      }
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Get the record lock <br>
   * <br>
   * Lock until transaction completes for a record of entity list.
   *
   * @param l2slices
   *          {@link FcL2Slice} entity list
   * @param l3slices
   *          {@link FcL3Slice} entity list
   * @param leafs
   *          {@link FcNode} (Leaf) entity list
   * @param spines
   *          {@link FcNode} (Spine) entity list
   * @param sessionWrapper
   *          Session
   * @throws MsfException
   *           DB exclusive control error
   */
  public void getResourceLock(List<FcL2Slice> l2slices, List<FcL3Slice> l3slices, List<FcNode> leafs,
      List<FcNode> spines, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2slices", "l3slices", "leafs", "spines", "sessionWrapper" },
          new Object[] { l2slices, l3slices, leafs, spines, sessionWrapper });

      boolean verify = false;

      if (CollectionUtils.isNotEmpty(l2slices)) {
        getL2SlicesLock(l2slices, sessionWrapper);
        verify = true;
      }

      if (CollectionUtils.isNotEmpty(l3slices)) {
        getL3SlicesLock(l3slices, sessionWrapper);
        verify = true;
      }

      if (CollectionUtils.isNotEmpty(spines)) {
        getSpinesLock(spines, sessionWrapper);
        verify = true;
      }

      if (CollectionUtils.isNotEmpty(leafs)) {
        getLeafsLock(leafs, sessionWrapper);
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

      configuration.addAnnotatedClass(FcAsyncRequest.class);
      configuration.addAnnotatedClass(FcBreakoutIf.class);
      configuration.addAnnotatedClass(FcClusterLinkIf.class);
      configuration.addAnnotatedClass(FcCpId.class);
      configuration.addAnnotatedClass(FcCpIdPK.class);
      configuration.addAnnotatedClass(FcEdgePoint.class);
      configuration.addAnnotatedClass(FcEquipment.class);
      configuration.addAnnotatedClass(FcEsiId.class);
      configuration.addAnnotatedClass(FcEsiIdPK.class);
      configuration.addAnnotatedClass(FcInternalLinkIf.class);
      configuration.addAnnotatedClass(FcL2Cp.class);
      configuration.addAnnotatedClass(FcL2CpPK.class);
      configuration.addAnnotatedClass(FcL2Slice.class);
      configuration.addAnnotatedClass(FcL3Cp.class);
      configuration.addAnnotatedClass(FcL3CpPK.class);
      configuration.addAnnotatedClass(FcL3Slice.class);
      configuration.addAnnotatedClass(FcLagIf.class);
      configuration.addAnnotatedClass(FcLagIfId.class);
      configuration.addAnnotatedClass(FcLeafNode.class);
      configuration.addAnnotatedClass(FcNode.class);
      configuration.addAnnotatedClass(FcPhysicalIf.class);
      configuration.addAnnotatedClass(FcSliceId.class);
      configuration.addAnnotatedClass(FcSwCluster.class);
      configuration.addAnnotatedClass(FcSystemStatus.class);
      configuration.addAnnotatedClass(FcVlanIf.class);
      configuration.addAnnotatedClass(FcVlanIfId.class);
      configuration.addAnnotatedClass(FcVlanIfPK.class);
      configuration.addAnnotatedClass(FcVrfId.class);
      return configuration;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public AsyncRequestsDao createAsyncRequestsDao() {
    return new AsyncRequestsDao() {
      private FcAsyncRequestsDao dao = new FcAsyncRequestsDao();

      @Override
      public AsyncRequest read(SessionWrapper session, String operationId) throws MsfException {
        FcAsyncRequest entity = dao.read(session, operationId);
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
        for (FcAsyncRequest request : dao.readList(session)) {
          list.add(request.getCommonEntity());
        }
        return list;
      }

      @Override
      public void create(SessionWrapper session, AsyncRequest entity) throws MsfException {
        dao.create(session, new FcAsyncRequest(entity));
      }

      @Override
      public void update(SessionWrapper session, AsyncRequest entity) throws MsfException {
        FcAsyncRequest updateEntity = dao.read(session, entity.getOperationId());
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
  public SystemStatusDao createSystemStatusDao() {
    return new SystemStatusDao() {
      private FcSystemStatusDao dao = new FcSystemStatusDao();

      @Override
      public void create(SessionWrapper session, SystemStatus entity) throws MsfException {
        dao.create(session, new FcSystemStatus(entity));
      }

      @Override
      public SystemStatus read(SessionWrapper session, Integer systemId) throws MsfException {
        FcSystemStatus entity = dao.read(session, systemId);
        if (entity != null) {
          return entity.getCommonEntity();
        } else {
          return null;
        }
      }

      @Override
      public void update(SessionWrapper session, SystemStatus entity) throws MsfException {
        FcSystemStatus updateEntity = dao.read(session, entity.getSystemId());
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
      private FcCpIdDao dao = new FcCpIdDao();

      @Override
      public void create(SessionWrapper session, CpId entity) throws MsfException {
        dao.create(session, new FcCpId(entity));
      }

      @Override
      public CpId read(SessionWrapper session, CpIdPK pk) throws MsfException {
        FcCpId entity = dao.read(session, new FcCpIdPK(pk));
        if (entity != null) {
          return entity.getCommonEntity();
        } else {
          return null;
        }
      }

      @Override
      public void update(SessionWrapper session, CpId entity) throws MsfException {
        FcCpId updateEntity = dao.read(session, new FcCpIdPK(entity.getId()));
        updateEntity.setCommonEntity(entity);
        dao.update(session, updateEntity);
      }

      @Override
      public void delete(SessionWrapper session, CpIdPK pk) throws MsfException {
        dao.delete(session, new FcCpIdPK(pk));
      }
    };
  }

  @Override
  public EsiDao createEsiDao() {
    return new EsiDao() {
      private FcEsiDao dao = new FcEsiDao();

      @Override
      public void create(SessionWrapper session, EsiId entity) throws MsfException {
        dao.create(session, new FcEsiId(entity));
      }

      @Override
      public EsiId read(SessionWrapper session, EsiIdPK pk) throws MsfException {
        FcEsiId entity = dao.read(session, new FcEsiIdPK(pk));
        if (entity != null) {
          return entity.getCommonEntity();
        } else {
          return null;
        }
      }

      @Override
      public void update(SessionWrapper session, EsiId entity) throws MsfException {
        FcEsiId updateEntity = dao.read(session, new FcEsiIdPK(entity.getId()));
        updateEntity.setCommonEntity(entity);
        dao.update(session, updateEntity);
      }

      @Override
      public void delete(SessionWrapper session, EsiIdPK pk) throws MsfException {
        dao.delete(session, new FcEsiIdPK(pk));
      }
    };
  }

  @Override
  public SliceIdDao createSliceIdDao() {
    return new SliceIdDao() {
      private FcSliceIdDao dao = new FcSliceIdDao();

      @Override
      public void create(SessionWrapper session, SliceId entity) throws MsfException {
        dao.create(session, new FcSliceId(entity));
      }

      @Override
      public SliceId read(SessionWrapper session, Integer pk) throws MsfException {
        FcSliceId entity = dao.read(session, pk);
        if (entity != null) {
          return entity.getCommonEntity();
        } else {
          return null;
        }
      }

      @Override
      public void update(SessionWrapper session, SliceId entity) throws MsfException {
        FcSliceId updateEntity = dao.read(session, entity.getLayerType());
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
  public VlanIfIdDao createVlanIfIdDao() {
    return new VlanIfIdDao() {
      private FcVlanIfIdDao dao = new FcVlanIfIdDao();

      @Override
      public void create(SessionWrapper session, VlanIfId entity) throws MsfException {
        dao.create(session, new FcVlanIfId(entity));
      }

      @Override
      public VlanIfId read(SessionWrapper session, Integer pk) throws MsfException {
        FcVlanIfId entity = dao.read(session, pk);
        if (entity != null) {
          return entity.getCommonEntity();
        } else {
          return null;
        }
      }

      @Override
      public void update(SessionWrapper session, VlanIfId entity) throws MsfException {
        FcVlanIfId updateEntity = dao.read(session, entity.getNodeInfoId());
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
      private FcVrfIdDao dao = new FcVrfIdDao();

      @Override
      public void create(SessionWrapper session, VrfId entity) throws MsfException {
        dao.create(session, new FcVrfId(entity));
      }

      @Override
      public VrfId read(SessionWrapper session, Integer pk) throws MsfException {
        FcVrfId entity = dao.read(session, pk);
        if (entity != null) {
          return entity.getCommonEntity();
        } else {
          return null;
        }
      }

      @Override
      public void update(SessionWrapper session, VrfId entity) throws MsfException {
        FcVrfId updateEntity = dao.read(session, entity.getLayerType());
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
