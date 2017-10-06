package msf.fc.db.dao.slices;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.SliceManagerBaseInfo;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class SliceManagerBaseInfoDao extends AbstractCommonDao<SliceManagerBaseInfo, Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(SliceManagerBaseInfoDao.class);

  @Override
  public void create(SessionWrapper session, SliceManagerBaseInfo entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public SliceManagerBaseInfo read(SessionWrapper session, Integer baseId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "baseId" }, new Object[] { session, baseId });
      Criteria criteria = session.getSession().createCriteria(SliceManagerBaseInfo.class)
          .add(Restrictions.eq("baseId", baseId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void update(SessionWrapper session, SliceManagerBaseInfo entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.update(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, Integer baseId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "baseId" }, new Object[] { session, baseId });
      super.delete(session, baseId);
    } finally {
      logger.methodEnd();
    }
  }

}
