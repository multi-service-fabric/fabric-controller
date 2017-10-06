package msf.fc.db.dao.clusters;

import java.util.List;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.EdgePoint;
import msf.fc.common.data.EdgePointPK;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class NodeDao extends AbstractCommonDao<Node, Long> {

  private static final MsfLogger logger = MsfLogger.getInstance(NodeDao.class);

  public List<Node> readList(SessionWrapper session, String swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId" }, new Object[] { session, swClusterId });
      Criteria criteria = session.getSession().createCriteria(Node.class)
          .add(Restrictions.eq("equipment.id.swClusterId", swClusterId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<Node> readList(SessionWrapper session, String swClusterId, Integer nodeType) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId", "nodeType" },
          new Object[] { session, swClusterId, nodeType });
      Criteria criteria = session.getSession().createCriteria(Node.class)
          .add(Restrictions.eq("equipment.id.swClusterId", swClusterId))
          .add(Restrictions.eq("nodeType", nodeType));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<Node> readList(SessionWrapper session, String routerId, String managementIfAddress) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "routerId", "managementIfAddress" },
          new Object[] { session, routerId, managementIfAddress });
      Criteria criteria = session.getSession().createCriteria(Node.class)
          .add(Restrictions.or(
              Restrictions.eq("routerId", routerId),
              Restrictions.eq("managementIfAddress", managementIfAddress)));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public Node read(SessionWrapper session, String swClusterId, Integer nodeType, Integer nodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId", "nodeType", "nodeId" },
          new Object[] { session, swClusterId, nodeType, nodeId });
      Criteria criteria = session.getSession().createCriteria(Node.class)
          .add(Restrictions.eq("equipment.id.swClusterId", swClusterId))
          .add(Restrictions.eq("nodeType", nodeType))
          .add(Restrictions.eq("nodeId", nodeId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public Node read(SessionWrapper session, String swClusterId, Integer edgePointId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId", "edgePointId" },
          new Object[] { session, swClusterId, edgePointId });
      EdgePointPK edgePointPk = new EdgePointPK();
      edgePointPk.setSwClusterId(swClusterId);
      edgePointPk.setEdgePointId(edgePointId);

      EdgePointDao edgePointDao = new EdgePointDao();
      EdgePoint edgePoint = edgePointDao.read(session, edgePointPk);

      if (Objects.isNull(edgePoint)) {
        return null;
      }

      PhysicalIfDao physicalIfDao = new PhysicalIfDao();
      PhysicalIf physicalIf = physicalIfDao.read(session, edgePoint.getPhysicalIfInfoId());

      if (Objects.nonNull(physicalIf)) {
        return physicalIf.getNode();
      }

      LagIfDao lagIfDao = new LagIfDao();
      LagIf lagIf = lagIfDao.read(session, edgePoint.getLagIfInfoId());

      if (Objects.nonNull(lagIf)) {
        return lagIf.getNode();
      }

      return null;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, Node entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);

      LagIfDao lagIfDao = new LagIfDao();
      for (LagIf lagIf : getList(entity.getLagIfs())) {
        lagIfDao.create(session, lagIf);
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public Node read(SessionWrapper session, Long nodeInfoId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeInfoId" }, new Object[] { session, nodeInfoId });
      Criteria criteria = session.getSession().createCriteria(Node.class)
          .add(Restrictions.eq("nodeInfoId", nodeInfoId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void update(SessionWrapper session, Node entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.update(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, Long nodeInfoId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeInfoId" }, new Object[] { session, nodeInfoId });
      Node node = read(session, nodeInfoId);

      EdgePointDao edgePointDao = new EdgePointDao();
      Criteria criteria = createEdgePointCriteria(session, node.getPhysicalIfs(), node.getLagIfs());
      edgePointDao.deleteByCriteria(session, criteria);

      super.delete(session, nodeInfoId);
    } finally {
      logger.methodEnd();
    }
  }

}
