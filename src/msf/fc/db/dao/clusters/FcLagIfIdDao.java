
package msf.fc.db.dao.clusters;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcLagIfId;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class FcLagIfIdDao extends FcAbstractCommonDao<FcLagIfId, Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLagIfIdDao.class);

  public List<FcLagIfId> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(FcLagIfId.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcLagIfId read(SessionWrapper session, Integer lagIfNextId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcLagIfId.class)
        .add(Restrictions.eq("nextId", lagIfNextId));
    return readByCriteria(session, criteria);
  }
}
