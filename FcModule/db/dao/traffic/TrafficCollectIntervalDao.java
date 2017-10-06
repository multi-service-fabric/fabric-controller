package msf.fc.db.dao.traffic;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.TrafficCollectInterval;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class TrafficCollectIntervalDao extends AbstractCommonDao<TrafficCollectInterval, Date> {

  private static final MsfLogger logger = MsfLogger.getInstance(TrafficCollectIntervalDao.class);

  public TrafficCollectInterval readNullData(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(TrafficCollectInterval.class)
          .add(Restrictions.isNull("endTime"));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public void deleteExpiredData(SessionWrapper session, Date outsideTime) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "outsideTime" }, new Object[] { session, outsideTime });
      String sql = "delete TrafficCollectInterval where endTime < :outsideTime";
      Query query = session.getSession().createQuery(sql)
          .setTimestamp("outsideTime", outsideTime);
      deleteByQuery(session, query);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, TrafficCollectInterval entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public TrafficCollectInterval read(SessionWrapper session, Date startTime) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "startTime" }, new Object[] { session, startTime });
      Criteria criteria = session.getSession().createCriteria(TrafficCollectInterval.class)
          .add(Restrictions.eq("startTime", startTime));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void update(SessionWrapper session, TrafficCollectInterval entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.update(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, Date startTime) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "startTime" }, new Object[] { session, startTime });
      super.delete(session, startTime);
    } finally {
      logger.methodEnd();
    }
  }

}
