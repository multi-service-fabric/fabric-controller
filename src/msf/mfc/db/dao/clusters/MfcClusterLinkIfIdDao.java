
package msf.mfc.db.dao.clusters;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcClusterLinkIfId;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class MfcClusterLinkIfIdDao extends MfcAbstractCommonDao<MfcClusterLinkIfId, Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterLinkIfIdDao.class);

  public List<MfcClusterLinkIfId> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(MfcClusterLinkIfId.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public MfcClusterLinkIfId read(SessionWrapper session, Integer clusterLinkNextIfId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcClusterLinkIfId.class)
        .add(Restrictions.eq("nextId", clusterLinkNextIfId));
    return readByCriteria(session, criteria);
  }
}
