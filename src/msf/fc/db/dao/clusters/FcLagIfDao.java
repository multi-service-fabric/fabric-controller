
package msf.fc.db.dao.clusters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class FcLagIfDao extends FcAbstractCommonDao<FcLagIf, Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLagIfDao.class);

  public List<FcLagIf> readList(SessionWrapper session, Integer nodeType, Integer nodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeType", "nodeId" }, new Object[] { session, nodeType, nodeId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.read(session, nodeType, nodeId);

      if (Objects.isNull(fcNode)) {
        return new ArrayList<>();
      }

      return fcNode.getLagIfs();
    } finally {
      logger.methodEnd();
    }
  }

  public List<FcLagIf> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(FcLagIf.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public FcLagIf read(SessionWrapper session, Integer nodeType, Integer nodeId, Integer lagIfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeType", "nodeId", "lagIfId" },
          new Object[] { session, nodeType, nodeId, lagIfId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.read(session, nodeType, nodeId);

      if (Objects.isNull(fcNode)) {
        return null;
      }

      Criteria criteria = session.getSession().createCriteria(FcLagIf.class)
          .add(Restrictions.eq("node.nodeInfoId", fcNode.getNodeInfoId())).add(Restrictions.eq("lagIfId", lagIfId));
      return readByCriteria(session, criteria);

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcLagIf read(SessionWrapper session, Integer lagIfId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcLagIf.class).add(Restrictions.eq("lagIfId", lagIfId));
    return readByCriteria(session, criteria);
  }
}
