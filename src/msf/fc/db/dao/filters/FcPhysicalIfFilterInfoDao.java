
package msf.fc.db.dao.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.common.data.FcPhysicalIfFilterInfo;
import msf.fc.common.data.FcPhysicalIfFilterInfoPK;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.fc.db.dao.clusters.FcPhysicalIfDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class FcPhysicalIfFilterInfoDao extends FcAbstractCommonDao<FcPhysicalIfFilterInfo, FcPhysicalIfFilterInfoPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcPhysicalIfFilterInfoDao.class);

  public List<FcPhysicalIfFilterInfo> readList(SessionWrapper session, Integer nodeType, Integer nodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeType", "nodeId" }, new Object[] { session, nodeType, nodeId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.read(session, nodeType, nodeId);

      if (Objects.isNull(fcNode)) {
        return new ArrayList<>();
      }

      List<FcPhysicalIfFilterInfo> fcPhysicalIfFilterInfos = new ArrayList<>();
      for (FcPhysicalIf fcPhysicalIf : fcNode.getPhysicalIfs()) {
        fcPhysicalIfFilterInfos.addAll(fcPhysicalIf.getPhysicalIfFilterInfos());
      }
      return fcPhysicalIfFilterInfos;
    } finally {
      logger.methodEnd();
    }
  }

  public List<FcPhysicalIfFilterInfo> readList(SessionWrapper session, Integer nodeType, Integer nodeId,
      String physicalIfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeType", "nodeId", "physicalIfId" },
          new Object[] { session, nodeType, nodeId, physicalIfId });

      FcPhysicalIfDao fcPhysicalIfDao = new FcPhysicalIfDao();
      FcPhysicalIf fcPhysicalIf = fcPhysicalIfDao.read(session, nodeType, nodeId, physicalIfId);

      if (Objects.isNull(fcPhysicalIf)) {
        return new ArrayList<>();
      } else {
        return fcPhysicalIf.getPhysicalIfFilterInfos();
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcPhysicalIfFilterInfo read(SessionWrapper session, FcPhysicalIfFilterInfoPK id) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcPhysicalIfFilterInfo.class)
        .add(Restrictions.eq("id", id));
    return readByCriteria(session, criteria);
  }

}
