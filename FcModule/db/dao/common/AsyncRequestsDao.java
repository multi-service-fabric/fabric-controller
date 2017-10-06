package msf.fc.db.dao.common;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.AsyncRequest;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class AsyncRequestsDao extends AbstractCommonDao<AsyncRequest, String> {

  private static final MsfLogger logger = MsfLogger.getInstance(AsyncRequestsDao.class);

  public List<AsyncRequest> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(AsyncRequest.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public void updateList(SessionWrapper session, Integer beforeStatus, Integer afterStatus, String afterSubStatus)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "beforeStatus", "afterStatus", "afterSubStatus" },
          new Object[] { session, beforeStatus, afterStatus, afterSubStatus });
      String sql = "update AsyncRequest "
          + "set status = :afterStatus , subStatus = :afterSubStatus "
          + "where status = :beforeStatus";
      Query query = session.getSession().createQuery(sql)
          .setParameter("beforeStatus", beforeStatus)
          .setParameter("afterStatus", afterStatus)
          .setParameter("afterSubStatus", afterSubStatus);
      updateByQuery(session, query);
    } finally {
      logger.methodEnd();
    }
  }

  public void delete(SessionWrapper session, Timestamp targetTime) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "targetTime" }, new Object[] { session, targetTime });
      String sql = "delete AsyncRequest where occurredTime < :targetTime";
      Query query = session.getSession().createQuery(sql)
          .setTimestamp("targetTime", targetTime);
      deleteByQuery(session, query);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, AsyncRequest entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public AsyncRequest read(SessionWrapper session, String operationId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "operationId" }, new Object[] { session, operationId });
      Criteria criteria = session.getSession().createCriteria(AsyncRequest.class)
          .add(Restrictions.eq("operationId", operationId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void update(SessionWrapper session, AsyncRequest entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.update(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, String operationId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "operationId" }, new Object[] { session, operationId });
      super.delete(session, operationId);
    } finally {
      logger.methodEnd();
    }
  }

}
