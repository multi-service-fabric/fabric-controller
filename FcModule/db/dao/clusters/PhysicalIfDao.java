package msf.fc.db.dao.clusters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class PhysicalIfDao extends AbstractCommonDao<PhysicalIf, Long> {

  private static final MsfLogger logger = MsfLogger.getInstance(PhysicalIfDao.class);

  public List<PhysicalIf> readList(SessionWrapper session, String swClusterId, Integer nodeType, Integer nodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId", "nodeType", "nodeId" },
          new Object[] { session, swClusterId, nodeType, nodeId });
      NodeDao nodeDao = new NodeDao();
      Node node = nodeDao.read(session, swClusterId, nodeType, nodeId);

      if (Objects.isNull(node)) {
        return new ArrayList<>();
      }

      return node.getPhysicalIfs();
    } finally {
      logger.methodEnd();
    }
  }

  public PhysicalIf read(SessionWrapper session, String swClusterId, Integer nodeType, Integer nodeId,
      String physicalIfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId", "nodeType", "nodeId", "physicalIfId" },
          new Object[] { session, swClusterId, nodeType, nodeId, physicalIfId });
      NodeDao nodeDao = new NodeDao();
      Node node = nodeDao.read(session, swClusterId, nodeType, nodeId);

      if (Objects.isNull(node)) {
        return null;
      }

      return read(session, node.getNodeInfoId(), physicalIfId);
    } finally {
      logger.methodEnd();
    }
  }

  public PhysicalIf read(SessionWrapper session, Long nodeInfoId, String physicalIfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeInfoId", "physicalIfId" },
          new Object[] { session, nodeInfoId, physicalIfId });
      Criteria criteria = session.getSession().createCriteria(PhysicalIf.class)
          .add(Restrictions.eq("node.nodeInfoId", nodeInfoId))
          .add(Restrictions.eq("physicalIfId", physicalIfId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public PhysicalIf read(SessionWrapper session, Long physicalIfInfoId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "physicalIfInfoId" }, new Object[] { session, physicalIfInfoId });
      Criteria criteria = session.getSession().createCriteria(PhysicalIf.class)
          .add(Restrictions.eq("physicalIfInfoId", physicalIfInfoId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void update(SessionWrapper session, PhysicalIf entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.update(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

}
