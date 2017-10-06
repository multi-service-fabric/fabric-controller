package msf.fc.db.dao.common;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.SystemStatus;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class SystemStatusDao extends AbstractCommonDao<SystemStatus, Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(SystemStatusDao.class);

  @Override
  public void create(SessionWrapper session, SystemStatus entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public SystemStatus read(SessionWrapper session, Integer systemId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "systemId" }, new Object[] { session, systemId });
      Criteria criteria = session.getSession().createCriteria(SystemStatus.class)
          .add(Restrictions.eq("systemId", systemId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void update(SessionWrapper session, SystemStatus entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.update(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, Integer systemId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "systemId" }, new Object[] { session, systemId });
      super.delete(session, systemId);
    } finally {
      logger.methodEnd();
    }
  }

}
