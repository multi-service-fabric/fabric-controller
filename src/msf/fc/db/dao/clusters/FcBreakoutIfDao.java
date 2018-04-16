
package msf.fc.db.dao.clusters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcBreakoutIf;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class FcBreakoutIfDao extends FcAbstractCommonDao<FcBreakoutIf, Long> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcBreakoutIfDao.class);

  public List<FcBreakoutIf> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(FcBreakoutIf.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<FcBreakoutIf> readList(SessionWrapper session, Integer nodeType, Integer nodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeType", "nodeId" }, new Object[] { session, nodeType, nodeId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.read(session, nodeType, nodeId);

      if (Objects.isNull(fcNode)) {
        return new ArrayList<>();
      }
      return fcNode.getBreakoutIfs();
    } finally {
      logger.methodEnd();
    }
  }

  public FcBreakoutIf read(SessionWrapper session, Integer nodeType, Integer nodeId, String breakoutIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeType", "nodeId", "breakoutIfId" },
          new Object[] { session, nodeType, nodeId, breakoutIfId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.read(session, nodeType, nodeId);

      if (Objects.isNull(fcNode)) {
        return null;
      }

      Criteria criteria = session.getSession().createCriteria(FcBreakoutIf.class)
          .add(Restrictions.eq("node.nodeInfoId", fcNode.getNodeInfoId()))
          .add(Restrictions.eq("breakoutIfId", breakoutIfId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcBreakoutIf read(SessionWrapper session, Long pk) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcBreakoutIf.class)
        .add(Restrictions.eq("breakoutIfInfoId", pk));
    return readByCriteria(session, criteria);
  }
}
