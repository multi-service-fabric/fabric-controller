
package msf.fc.db.dao.clusters;

import java.util.List;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcEdgePoint;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.common.data.FcVlanIfId;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.fc.db.dao.slices.FcVlanIfIdDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class FcNodeDao extends FcAbstractCommonDao<FcNode, Long> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeDao.class);

  public List<FcNode> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(FcNode.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<FcNode> readList(SessionWrapper session, Integer nodeType) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeType" }, new Object[] { session, nodeType });
      Criteria criteria = session.getSession().createCriteria(FcNode.class).add(Restrictions.eq("nodeType", nodeType));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public FcNode read(SessionWrapper session, Integer nodeType, Integer nodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeType", "nodeId" }, new Object[] { session, nodeType, nodeId });
      Criteria criteria = session.getSession().createCriteria(FcNode.class).add(Restrictions.eq("nodeType", nodeType))
          .add(Restrictions.eq("nodeId", nodeId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public FcNode read(SessionWrapper session, Integer edgePointId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "edgePointId" }, new Object[] { session, edgePointId });

      FcEdgePointDao fcEdgePointDao = new FcEdgePointDao();
      FcEdgePoint fcEdgePoint = fcEdgePointDao.read(session, edgePointId);

      if (Objects.isNull(fcEdgePoint)) {
        return null;
      }

      FcPhysicalIf physicalIf = fcEdgePoint.getPhysicalIf();
      if (Objects.nonNull(physicalIf)) {
        return physicalIf.getNode();
      }

      FcBreakoutIf breakoutIf = fcEdgePoint.getBreakoutIf();
      if (Objects.nonNull(breakoutIf)) {
        return breakoutIf.getNode();
      }

      FcLagIf lagIf = fcEdgePoint.getLagIf();
      if (Objects.nonNull(lagIf)) {
        return lagIf.getNode();
      }

      return null;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcNode read(SessionWrapper session, Long nodeInfoId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcNode.class)
        .add(Restrictions.eq("nodeInfoId", nodeInfoId));
    return readByCriteria(session, criteria);
  }

  public FcNode readByEcNodeId(SessionWrapper session, Integer ecNodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "ecNodeId" }, new Object[] { session, ecNodeId });
      Criteria criteria = session.getSession().createCriteria(FcNode.class).add(Restrictions.eq("ecNodeId", ecNodeId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, FcNode entity) throws MsfException {

    entity.setIsPriorityNodeGroupMember(false);

    entity.setDetoured(false);

    super.create(session, entity);

    FcVlanIfIdDao fcVlanIfIdDao = new FcVlanIfIdDao();
    FcVlanIfId fcVlanIfId = new FcVlanIfId();

    fcVlanIfId.setNodeInfoId(entity.getNodeInfoId());

    fcVlanIfId.setNextId(1);
    fcVlanIfIdDao.create(session, fcVlanIfId);

    if (entity.getLeafNode() != null) {

      FcLeafNodeDao fcLeafNodeDao = new FcLeafNodeDao();
      entity.getLeafNode().setNodeInfoId((entity.getNodeInfoId()));
      fcLeafNodeDao.create(session, entity.getLeafNode());
    }
  }

  @Override
  public void delete(SessionWrapper session, Long nodeInfoId) throws MsfException {
    super.delete(session, nodeInfoId);

    FcVlanIfIdDao fcVlanIfIdDao = new FcVlanIfIdDao();
    fcVlanIfIdDao.delete(session, nodeInfoId);
  }

}
