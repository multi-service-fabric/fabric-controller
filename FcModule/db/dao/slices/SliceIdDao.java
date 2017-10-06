package msf.fc.db.dao.slices;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.SliceId;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class SliceIdDao extends AbstractCommonDao<SliceId, Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(SliceIdDao.class);

  @Override
  public SliceId read(SessionWrapper session, Integer layerType) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "layerType" }, new Object[] { session, layerType });
      Criteria criteria = session.getSession().createCriteria(SliceId.class)
          .add(Restrictions.eq("layerType", layerType));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void update(SessionWrapper session, SliceId entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.update(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, Integer layerType) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "layerType" }, new Object[] { session, layerType });
      super.delete(session, layerType);
    } finally {
      logger.methodEnd();
    }
  }

}
