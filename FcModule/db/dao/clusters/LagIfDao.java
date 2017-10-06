package msf.fc.db.dao.clusters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.LagConstruction;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class LagIfDao extends AbstractCommonDao<LagIf, Long> {

  private static final MsfLogger logger = MsfLogger.getInstance(LagIfDao.class);

  public List<LagIf> readList(SessionWrapper session, String swClusterId, Integer nodeType, Integer nodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId", "nodeType", "nodeId" },
          new Object[] { session, swClusterId, nodeType, nodeId });
      NodeDao nodeDao = new NodeDao();
      Node node = nodeDao.read(session, swClusterId, nodeType, nodeId);

      if (Objects.isNull(node)) {
        return new ArrayList<>();
      }

      return node.getLagIfs();
    } finally {
      logger.methodEnd();
    }
  }

  public List<LagIf> readList(SessionWrapper session, String ipv4Address) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "ipv4Address" }, new Object[] { session, ipv4Address });
      Criteria criteria = session.getSession().createCriteria(LagIf.class)
          .add(Restrictions.eq("ipv4Address", ipv4Address));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public LagIf read(SessionWrapper session, String swClusterId, Integer nodeType, Integer nodeId, Integer lagIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId", "nodeType", "nodeId", "lagIfId" },
          new Object[] { session, swClusterId, nodeType, nodeId, lagIfId });
      NodeDao nodeDao = new NodeDao();
      Node node = nodeDao.read(session, swClusterId, nodeType, nodeId);

      if (Objects.isNull(node)) {
        return null;
      }

      Criteria criteria = session.getSession().createCriteria(LagIf.class)
          .add(Restrictions.eq("node.nodeInfoId", node.getNodeInfoId()))
          .add(Restrictions.eq("lagIfId", lagIfId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public LagIf readFromBiggestId(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(LagIf.class)
          .addOrder(Order.desc("lagIfId"));
      List<LagIf> entities = readListByCriteria(session, criteria);
      return entities.isEmpty() ? null : entities.get(0);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, LagIf entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);

      LagConstructionDao lagConstructionDao = new LagConstructionDao();
      for (LagConstruction lagConstruction : entity.getLagConstructions()) {
        lagConstruction.setPhysicalIfInfoId(lagConstruction.getPhysicalIf().getPhysicalIfInfoId());
        lagConstructionDao.create(session, lagConstruction);
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public LagIf read(SessionWrapper session, Long lagIfInfoId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "lagIfInfoId" }, new Object[] { session, lagIfInfoId });
      Criteria criteria = session.getSession().createCriteria(LagIf.class)
          .add(Restrictions.eq("lagIfInfoId", lagIfInfoId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void update(SessionWrapper session, LagIf entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.update(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, Long lagIfInfoId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "lagIfInfoId" }, new Object[] { session, lagIfInfoId });
      super.delete(session, lagIfInfoId);
    } finally {
      logger.methodEnd();
    }
  }

}
