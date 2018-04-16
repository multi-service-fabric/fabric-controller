
package msf.fc.db.dao.clusters;

import java.util.List;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcClusterLinkIf;
import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class FcClusterLinkIfDao extends FcAbstractCommonDao<FcClusterLinkIf, Long> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcClusterLinkIfDao.class);

  public FcClusterLinkIf readByPhysicalIfId(SessionWrapper session, Integer ecNodeId, String physicalIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "ecNodeId", "physicalIfId" },
          new Object[] { session, ecNodeId, physicalIfId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.readByEcNodeId(session, ecNodeId);
      if (Objects.isNull(fcNode)) {
        return null;
      }

      FcClusterLinkIf fcClusterLinkIf = null;
      for (FcPhysicalIf fcPhysicalIf : fcNode.getPhysicalIfs()) {
        if (!fcPhysicalIf.getPhysicalIfId().equals(physicalIfId)) {
          continue;
        }
        List<FcClusterLinkIf> fcClusterLinkIfs = fcPhysicalIf.getClusterLinkIfs();
        fcClusterLinkIf = fcClusterLinkIfs.isEmpty() ? null : fcClusterLinkIfs.get(0);
        break;
      }
      return fcClusterLinkIf;
    } finally {
      logger.methodEnd();
    }
  }

  public FcClusterLinkIf readByLagIfId(SessionWrapper session, Integer ecNodeId, String lagIfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "ecNodeId", "lagIfId" },
          new Object[] { session, ecNodeId, lagIfId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.readByEcNodeId(session, ecNodeId);
      if (Objects.isNull(fcNode)) {
        return null;
      }

      FcClusterLinkIf fcClusterLinkIf = null;
      for (FcLagIf fcLagIf : fcNode.getLagIfs()) {
        if (!String.valueOf(fcLagIf.getLagIfId()).equals(lagIfId)) {
          continue;
        }
        List<FcClusterLinkIf> fcClusterLinkIfs = fcLagIf.getClusterLinkIfs();
        fcClusterLinkIf = fcClusterLinkIfs.isEmpty() ? null : fcClusterLinkIfs.get(0);
        break;
      }
      return fcClusterLinkIf;
    } finally {
      logger.methodEnd();
    }
  }

  public FcClusterLinkIf readByBreakoutIfId(SessionWrapper session, Integer ecNodeId, String breakoutIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "ecNodeId", "breakoutIfId" },
          new Object[] { session, ecNodeId, breakoutIfId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.readByEcNodeId(session, ecNodeId);
      if (Objects.isNull(fcNode)) {
        return null;
      }

      FcClusterLinkIf fcClusterLinkIf = null;
      for (FcBreakoutIf fcBreakoutIf : fcNode.getBreakoutIfs()) {
        if (!String.valueOf(fcBreakoutIf.getBreakoutIfId()).equals(breakoutIfId)) {
          continue;
        }
        List<FcClusterLinkIf> fcClusterLinkIfs = fcBreakoutIf.getClusterLinkIfs();
        fcClusterLinkIf = fcClusterLinkIfs.isEmpty() ? null : fcClusterLinkIfs.get(0);
        break;
      }
      return fcClusterLinkIf;
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcClusterLinkIf read(SessionWrapper session, Long clusterLinkIfId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcClusterLinkIf.class)
        .add(Restrictions.eq("clusterLinkIfId", clusterLinkIfId));
    return readByCriteria(session, criteria);
  }
}
