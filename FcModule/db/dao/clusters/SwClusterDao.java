package msf.fc.db.dao.clusters;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.SwCluster;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class SwClusterDao extends AbstractCommonDao<SwCluster, String> {

  private static final MsfLogger logger = MsfLogger.getInstance(SwClusterDao.class);

  public List<SwCluster> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(SwCluster.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, SwCluster entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public SwCluster read(SessionWrapper session, String swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId" }, new Object[] { session, swClusterId });
      Criteria criteria = session.getSession().createCriteria(SwCluster.class)
          .add(Restrictions.eq("swClusterId", swClusterId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void update(SessionWrapper session, SwCluster entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.update(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, String swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId" }, new Object[] { session, swClusterId });
      super.delete(session, swClusterId);
    } finally {
      logger.methodEnd();
    }
  }

}
