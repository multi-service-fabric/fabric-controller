package msf.fc.db;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;

import msf.fc.common.FunctionBlockBase;
import msf.fc.common.config.ConfigManager;
import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.NodeType;
import msf.fc.common.data.L2Slice;
import msf.fc.common.data.L3Slice;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;

public final class DbManager implements FunctionBlockBase {

  private static final MsfLogger logger = MsfLogger.getInstance(DbManager.class);

  private static final DbManager instance = new DbManager();

  private static final Comparator<L2Slice> COMPARATOR_L2SLICE = new Comparator<L2Slice>() {
    @Override
    public int compare(L2Slice o1, L2Slice o2) {
      return o1.getSliceId().compareTo(o2.getSliceId());
    }
  };

  private static final Comparator<L3Slice> COMPARATOR_L3SLICE = new Comparator<L3Slice>() {
    @Override
    public int compare(L3Slice o1, L3Slice o2) {
      return o1.getSliceId().compareTo(o2.getSliceId());
    }
  };

  private static final Comparator<Node> COMPARATOR_NODE = new Comparator<Node>() {
    @Override
    public int compare(Node o1, Node o2) {
      return o1.getNodeId() - o2.getNodeId();
    }
  };

  private DbManager() {
  }

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
      return SessionWrapper.buildSessionFactory();
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

  public void getL2SlicesLock(List<L2Slice> l2slices, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "l2slices", "sessionWrapper" }, new Object[] { l2slices, sessionWrapper });
      if (CollectionUtils.isEmpty(l2slices)) {
        throw new IllegalArgumentException("l2slices is empty.");
      }
      l2slices.sort(COMPARATOR_L2SLICE);
      Session session = sessionWrapper.getSession();

      for (L2Slice l2Slice : new LinkedHashSet<>(l2slices)) {
        getLock(L2Slice.class, l2Slice.getSliceId(), session);
      }
    } finally {
      logger.methodEnd();
    }
  }

  public void getL3SlicesLock(List<L3Slice> l3slices, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "l3slices", "sessionWrapper" }, new Object[] { l3slices, sessionWrapper });
      if (CollectionUtils.isEmpty(l3slices)) {
        throw new IllegalArgumentException("l3slices is empty.");
      }
      l3slices.sort(COMPARATOR_L3SLICE);
      Session session = sessionWrapper.getSession();

      for (L3Slice l3Slice : new LinkedHashSet<>(l3slices)) {
        getLock(L3Slice.class, l3Slice.getSliceId(), session);
      }
    } finally {
      logger.methodEnd();
    }
  }

  public void getLeafsLock(List<Node> leafs, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "leafs", "sessionWrapper" }, new Object[] { leafs, sessionWrapper });
      if (CollectionUtils.isEmpty(leafs)) {
        throw new IllegalArgumentException("leafs is empty.");
      }
      leafs.sort(COMPARATOR_NODE);
      Session session = sessionWrapper.getSession();

      for (Node leaf : new LinkedHashSet<>(leafs)) {
        if (NodeType.LEAF != leaf.getNodeTypeEnum()) {
          throw new MsfException(ErrorCode.EXCLUSIVE_CONTROL_ERROR, "node type is " + leaf.getNodeTypeEnum());
        }
        getLock(Node.class, leaf.getNodeInfoId(), session);
      }
    } finally {
      logger.methodEnd();
    }
  }

  public void getSpinesLock(List<Node> spines, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "spines", "sessionWrapper" }, new Object[] { spines, sessionWrapper });
      if (CollectionUtils.isEmpty(spines)) {
        throw new IllegalArgumentException("spines is empty.");
      }
      spines.sort(COMPARATOR_NODE);
      Session session = sessionWrapper.getSession();

      for (Node spine : new LinkedHashSet<>(spines)) {
        if (NodeType.SPINE != spine.getNodeTypeEnum()) {
          throw new MsfException(ErrorCode.EXCLUSIVE_CONTROL_ERROR, "node type is " + spine.getNodeTypeEnum());
        }
        getLock(Node.class, spine.getNodeInfoId(), session);
      }
    } finally {
      logger.methodEnd();
    }
  }

  public void getResourceLock(List<L2Slice> l2slices, List<L3Slice> l3slices, List<Node> leafs, List<Node> spines,
      SessionWrapper sessionWrapper) throws MsfException {
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

  private <E> void getLock(Class<E> cls, Serializable pk, Session session) throws MsfException {
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

}
