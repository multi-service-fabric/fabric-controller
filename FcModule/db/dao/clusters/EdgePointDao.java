package msf.fc.db.dao.clusters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.EdgePoint;
import msf.fc.common.data.EdgePointPK;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class EdgePointDao extends AbstractCommonDao<EdgePoint, EdgePointPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(EdgePointDao.class);

  public List<EdgePoint> readList(SessionWrapper session, String swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId" }, new Object[] { session, swClusterId });
      Criteria criteria = session.getSession().createCriteria(EdgePoint.class)
          .add(Restrictions.eq("id.swClusterId", swClusterId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<EdgePoint> readList(SessionWrapper session, String swClusterId, Integer nodeType, Integer nodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId", "nodeType", "nodeId" },
          new Object[] { session, swClusterId, nodeType, nodeId });
      NodeDao nodeDao = new NodeDao();
      Node node = nodeDao.read(session, swClusterId, nodeType, nodeId);

      if (Objects.isNull(node)) {
        return new ArrayList<>();
      }

      Criteria criteria = createEdgePointCriteria(session, node.getPhysicalIfs(), node.getLagIfs());
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public EdgePoint readBylagIfInfoId(SessionWrapper session, Long lagIfInfoId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "lagIfInfoId" }, new Object[] { session, lagIfInfoId });
      Criteria criteria = session.getSession().createCriteria(EdgePoint.class)
          .add(Restrictions.eq("lagIfInfoId", lagIfInfoId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public EdgePoint readByPhysicalIfInfoId(SessionWrapper session, Long physicalIfInfoId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "physicalIfInfoId" }, new Object[] { session, physicalIfInfoId });
      Criteria criteria = session.getSession().createCriteria(EdgePoint.class)
          .add(Restrictions.eq("physicalIfInfoId", physicalIfInfoId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public EdgePoint readFromBiggestId(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(EdgePoint.class)
          .addOrder(Order.desc("id.edgePointId"));
      List<EdgePoint> entities = readListByCriteria(session, criteria);
      return entities.isEmpty() ? null : entities.get(0);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, EdgePoint entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public EdgePoint read(SessionWrapper session, EdgePointPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      Criteria criteria = session.getSession().createCriteria(EdgePoint.class)
          .add(Restrictions.eq("id.swClusterId", pk.getSwClusterId()))
          .add(Restrictions.eq("id.edgePointId", pk.getEdgePointId()));
      return (EdgePoint) readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, EdgePointPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      super.delete(session, pk);
    } finally {
      logger.methodEnd();
    }
  }

}
