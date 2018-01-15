package msf.mfcfc.db.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;


public abstract class AbstractCommonDao<E, K> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractCommonDao.class);


  
  public void create(SessionWrapper session, E entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      session.getSession().save(entity);
    } catch (HibernateException he) {
      logger.error("Error create.", he);
      throw new MsfException(ErrorCode.DATABASE_OPERATION_ERROR, "Database record create failed.");
    } finally {
      logger.methodEnd();
    }
  }

  
  public abstract E read(SessionWrapper session, K primaryKey) throws MsfException;

  
  public void update(SessionWrapper session, E entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      session.getSession().update(entity);
    } catch (HibernateException he) {
      logger.error("Error update.", he);
      throw new MsfException(ErrorCode.DATABASE_OPERATION_ERROR, "Database record update failed.");
    } finally {
      logger.methodEnd();
    }
  }

  
  public void delete(SessionWrapper session, K primaryKey) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "primaryKey" }, new Object[] { session, primaryKey });
      E entity = read(session, primaryKey);
      if (entity != null) {
        session.getSession().delete(entity);
      }
    } catch (HibernateException he) {
      logger.error("Error delete.", he);
      throw new MsfException(ErrorCode.DATABASE_OPERATION_ERROR, "Database record delete failed.");
    } finally {
      logger.methodEnd();
    }
  }


  
  public E readByCriteria(SessionWrapper session, Criteria criteria) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "criteria" }, new Object[] { session, criteria });
      @SuppressWarnings("unchecked")
      E entity = (E) criteria.uniqueResult();
      return entity;
    } catch (HibernateException he) {
      logger.error("Error readByCriteria.", he);
      throw new MsfException(ErrorCode.DATABASE_OPERATION_ERROR, "Database record read failed.");
    } finally {
      logger.methodEnd();
    }
  }

  
  public List<E> readListByCriteria(SessionWrapper session, Criteria criteria) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "criteria" }, new Object[] { session, criteria });
      @SuppressWarnings("unchecked")
      List<E> entities = criteria.list();
      return entities;
    } catch (HibernateException he) {
      logger.error("Error readListByCriteria.", he);
      throw new MsfException(ErrorCode.DATABASE_OPERATION_ERROR, "Database record read failed.");
    } finally {
      logger.methodEnd();
    }
  }

  
  public void deleteByCriteria(SessionWrapper session, Criteria criteria) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "criteria" }, new Object[] { session, criteria });
      List<E> entities = readListByCriteria(session, criteria);
      for (E entity : entities) {
        session.getSession().delete(entity);
      }
    } catch (HibernateException he) {
      logger.error("Error deleteByCriteria.", he);
      throw new MsfException(ErrorCode.DATABASE_OPERATION_ERROR, "Database record delete failed.");
    } finally {
      logger.methodEnd();
    }
  }


  
  protected void updateByQuery(SessionWrapper session, Query query) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "query" }, new Object[] { session, query });
      query.executeUpdate();
    } catch (HibernateException he) {
      logger.error("Error updateByQuery.", he);
      throw new MsfException(ErrorCode.DATABASE_OPERATION_ERROR, "Database record update failed.");
    } finally {
      logger.methodEnd();
    }
  }

  
  protected void deleteByQuery(SessionWrapper session, Query query) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "query" }, new Object[] { session, query });
      query.executeUpdate();
    } catch (HibernateException he) {
      logger.error("Error deleteByQuery.", he);
      throw new MsfException(ErrorCode.DATABASE_OPERATION_ERROR, "Database record delete failed.");
    } finally {
      logger.methodEnd();
    }
  }

  protected static <T> List<T> getList(List<T> list) {
    return Objects.isNull(list) ? new ArrayList<>() : list;
  }

}
