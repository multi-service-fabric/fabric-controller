package msf.fc.db.dao.clusters;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcEdgePoint;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;


public class FcEdgePointDao extends FcAbstractCommonDao<FcEdgePoint, Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcEdgePointDao.class);


  
  public List<FcEdgePoint> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(FcEdgePoint.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  
  public FcEdgePoint readFromBiggestId(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(FcEdgePoint.class).addOrder(Order.desc("edgePointId"));
      List<FcEdgePoint> entities = readListByCriteria(session, criteria);
      return entities.isEmpty() ? null : entities.get(0);
    } finally {
      logger.methodEnd();
    }
  }

  
  public FcEdgePoint readByPhysicalIfId(SessionWrapper session, Integer ecNodeId, String physicalIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "ecNodeId", "physicalIfId" },
          new Object[] { session, ecNodeId, physicalIfId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.readByEcNodeId(session, ecNodeId);

      FcEdgePoint fcEdgePoint = null;
      for (FcPhysicalIf fcPhysicalIf : fcNode.getPhysicalIfs()) {
        if (!fcPhysicalIf.getPhysicalIfId().equals(physicalIfId)) {
          continue;
        }
        List<FcEdgePoint> fcEdgePoints = fcPhysicalIf.getEdgePoints();
        fcEdgePoint = fcEdgePoints.isEmpty() ? null : fcEdgePoints.get(0);
        break;
      }
      return fcEdgePoint;
    } finally {
      logger.methodEnd();
    }
  }

  
  public FcEdgePoint readByLagIfId(SessionWrapper session, Integer ecNodeId, String lagIfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "ecNodeId", "lagIfId" },
          new Object[] { session, ecNodeId, lagIfId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.readByEcNodeId(session, ecNodeId);

      FcEdgePoint fcEdgePoint = null;
      for (FcLagIf fcLagIf : fcNode.getLagIfs()) {
        if (!String.valueOf(fcLagIf.getLagIfId()).equals(lagIfId)) {
          continue;
        }
        List<FcEdgePoint> fcEdgePoints = fcLagIf.getEdgePoints();
        fcEdgePoint = fcEdgePoints.isEmpty() ? null : fcEdgePoints.get(0);
        break;
      }
      return fcEdgePoint;
    } finally {
      logger.methodEnd();
    }
  }

  
  public FcEdgePoint readByBreakoutIfId(SessionWrapper session, Integer ecNodeId, String breakoutIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "ecNodeId", "breakoutIfId" },
          new Object[] { session, ecNodeId, breakoutIfId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.readByEcNodeId(session, ecNodeId);

      FcEdgePoint fcEdgePoint = null;
      for (FcBreakoutIf fcBreakoutIf : fcNode.getBreakoutIfs()) {
        if (!String.valueOf(fcBreakoutIf.getBreakoutIfId()).equals(breakoutIfId)) {
          continue;
        }
        List<FcEdgePoint> fcEdgePoints = fcBreakoutIf.getEdgePoints();
        fcEdgePoint = fcEdgePoints.isEmpty() ? null : fcEdgePoints.get(0);
        break;
      }
      return fcEdgePoint;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcEdgePoint read(SessionWrapper session, Integer pk) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcEdgePoint.class).add(Restrictions.eq("edgePointId", pk));
    return readByCriteria(session, criteria);
  }
}
