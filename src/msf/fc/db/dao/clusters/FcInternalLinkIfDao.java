
package msf.fc.db.dao.clusters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcInternalLinkIf;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class FcInternalLinkIfDao extends FcAbstractCommonDao<FcInternalLinkIf, Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalLinkIfDao.class);

  public List<FcInternalLinkIf> readList(SessionWrapper session, Integer nodeType, Integer nodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeType", "nodeId" }, new Object[] { session, nodeType, nodeId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.read(session, nodeType, nodeId);

      if (Objects.isNull(fcNode)) {
        return new ArrayList<>();
      }

      List<FcInternalLinkIf> fcInternalLinkIfs = new ArrayList<>();

      for (FcPhysicalIf fcPhysicalIf : fcNode.getPhysicalIfs()) {
        if (fcPhysicalIf != null && CollectionUtils.isNotEmpty(fcPhysicalIf.getInternalLinkIfs())) {
          fcInternalLinkIfs.addAll(fcPhysicalIf.getInternalLinkIfs());
        }
      }

      for (FcBreakoutIf fcBreakoutIf : fcNode.getBreakoutIfs()) {
        if (fcBreakoutIf != null && CollectionUtils.isNotEmpty(fcBreakoutIf.getInternalLinkIfs())) {
          fcInternalLinkIfs.addAll(fcBreakoutIf.getInternalLinkIfs());
        }
      }

      for (FcLagIf fcLagIf : fcNode.getLagIfs()) {
        if (fcLagIf != null && CollectionUtils.isNotEmpty(fcLagIf.getInternalLinkIfs())) {
          fcInternalLinkIfs.addAll(fcLagIf.getInternalLinkIfs());
        }
      }

      return fcInternalLinkIfs;
    } finally {
      logger.methodEnd();
    }
  }

  public List<FcInternalLinkIf> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(FcInternalLinkIf.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public FcInternalLinkIf readFromBiggestId(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(FcInternalLinkIf.class)
          .addOrder(Order.desc("internalLinkIfId"));
      List<FcInternalLinkIf> entities = readListByCriteria(session, criteria);
      return entities.isEmpty() ? null : entities.get(0);
    } finally {
      logger.methodEnd();
    }
  }

  public FcInternalLinkIf readByPhysicalIfId(SessionWrapper session, Integer ecNodeId, String physicalIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "ecNodeId", "physicalIfId" },
          new Object[] { session, ecNodeId, physicalIfId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.readByEcNodeId(session, ecNodeId);
      if (Objects.isNull(fcNode)) {
        return null;
      }

      FcInternalLinkIf fcInternalLinkIf = null;
      for (FcPhysicalIf fcPhysicalIf : fcNode.getPhysicalIfs()) {
        if (!fcPhysicalIf.getPhysicalIfId().equals(physicalIfId)) {
          continue;
        }
        List<FcInternalLinkIf> fcInternalLinkIfs = fcPhysicalIf.getInternalLinkIfs();
        fcInternalLinkIf = fcInternalLinkIfs.isEmpty() ? null : fcInternalLinkIfs.get(0);
        break;
      }
      return fcInternalLinkIf;
    } finally {
      logger.methodEnd();
    }
  }

  public FcInternalLinkIf readByLagIfId(SessionWrapper session, Integer ecNodeId, String lagIfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "ecNodeId", "lagIfId" },
          new Object[] { session, ecNodeId, lagIfId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.readByEcNodeId(session, ecNodeId);
      if (Objects.isNull(fcNode)) {
        return null;
      }

      FcInternalLinkIf fcInternalLinkIf = null;
      for (FcLagIf fcLagIf : fcNode.getLagIfs()) {
        if (!String.valueOf(fcLagIf.getLagIfId()).equals(lagIfId)) {
          continue;
        }
        List<FcInternalLinkIf> fcInternalLinkIfs = fcLagIf.getInternalLinkIfs();
        fcInternalLinkIf = fcInternalLinkIfs.isEmpty() ? null : fcInternalLinkIfs.get(0);
        break;
      }
      return fcInternalLinkIf;
    } finally {
      logger.methodEnd();
    }
  }

  public FcInternalLinkIf readByBreakoutIfId(SessionWrapper session, Integer ecNodeId, String breakoutIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "ecNodeId", "breakoutIfId" },
          new Object[] { session, ecNodeId, breakoutIfId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.readByEcNodeId(session, ecNodeId);
      if (Objects.isNull(fcNode)) {
        return null;
      }

      FcInternalLinkIf fcInternalLinkIf = null;
      for (FcBreakoutIf fcBreakoutIf : fcNode.getBreakoutIfs()) {
        if (!String.valueOf(fcBreakoutIf.getBreakoutIfId()).equals(breakoutIfId)) {
          continue;
        }
        List<FcInternalLinkIf> fcInternalLinkIfs = fcBreakoutIf.getInternalLinkIfs();
        fcInternalLinkIf = fcInternalLinkIfs.isEmpty() ? null : fcInternalLinkIfs.get(0);
        break;
      }
      return fcInternalLinkIf;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcInternalLinkIf read(SessionWrapper session, Integer internalLinkIfId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcInternalLinkIf.class)
        .add(Restrictions.eq("internalLinkIfId", internalLinkIfId));
    return readByCriteria(session, criteria);
  }
}
