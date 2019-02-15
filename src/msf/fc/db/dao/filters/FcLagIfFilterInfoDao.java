
package msf.fc.db.dao.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcLagIf;
import msf.fc.common.data.FcLagIfFilterInfo;
import msf.fc.common.data.FcLagIfFilterInfoPK;
import msf.fc.common.data.FcNode;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.fc.db.dao.clusters.FcLagIfDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class FcLagIfFilterInfoDao extends FcAbstractCommonDao<FcLagIfFilterInfo, FcLagIfFilterInfoPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLagIfFilterInfoDao.class);

  public List<FcLagIfFilterInfo> readList(SessionWrapper session, Integer nodeType, Integer nodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeType", "nodeId" }, new Object[] { session, nodeType, nodeId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.read(session, nodeType, nodeId);

      if (Objects.isNull(fcNode)) {
        return new ArrayList<>();
      }

      List<FcLagIfFilterInfo> fcLagIfFilterInfos = new ArrayList<>();
      for (FcLagIf fcLagIf : fcNode.getLagIfs()) {
        fcLagIfFilterInfos.addAll(fcLagIf.getLagIfFilterInfos());
      }
      return fcLagIfFilterInfos;
    } finally {
      logger.methodEnd();
    }
  }

  public List<FcLagIfFilterInfo> readList(SessionWrapper session, Integer nodeType, Integer nodeId, Integer lagIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeType", "nodeId", "lagIfId" },
          new Object[] { session, nodeType, nodeId, lagIfId });

      FcLagIfDao fcLagIfDao = new FcLagIfDao();
      FcLagIf fcLagIf = fcLagIfDao.read(session, nodeType, nodeId, lagIfId);

      if (Objects.isNull(fcLagIf)) {
        return new ArrayList<>();
      } else {
        return fcLagIf.getLagIfFilterInfos();
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcLagIfFilterInfo read(SessionWrapper session, FcLagIfFilterInfoPK id) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcLagIfFilterInfo.class).add(Restrictions.eq("id", id));
    return readByCriteria(session, criteria);
  }
}
