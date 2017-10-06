package msf.fc.db.dao.clusters;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.LagConstruction;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class LagConstructionDao extends AbstractCommonDao<LagConstruction, Long> {

  private static final MsfLogger logger = MsfLogger.getInstance(LagConstructionDao.class);

  @Override
  public LagConstruction read(SessionWrapper session, Long physicalIfInfoId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "physicalIfInfoId" }, new Object[] { session, physicalIfInfoId });
      Criteria criteria = session.getSession().createCriteria(LagConstruction.class)
          .add(Restrictions.eq("physicalIfInfoId", physicalIfInfoId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, Long physicalIfInfoId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "physicalIfInfoId" }, new Object[] { session, physicalIfInfoId });
      super.delete(session, physicalIfInfoId);
    } finally {
      logger.methodEnd();
    }
  }

}
