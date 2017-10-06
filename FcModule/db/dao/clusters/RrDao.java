package msf.fc.db.dao.clusters;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.Rr;
import msf.fc.common.data.RrPK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class RrDao extends AbstractCommonDao<Rr, RrPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(RrDao.class);

  public List<Rr> readList(SessionWrapper session, String swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId" }, new Object[] { session, swClusterId });
      Criteria criteria = session.getSession().createCriteria(Rr.class)
          .add(Restrictions.eq("id.swClusterId", swClusterId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, Rr entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public Rr read(SessionWrapper session, RrPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      Criteria criteria = session.getSession().createCriteria(Rr.class)
          .add(Restrictions.eq("id.swClusterId", pk.getSwClusterId()))
          .add(Restrictions.eq("id.rrNodeId", pk.getRrNodeId()));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void update(SessionWrapper session, Rr entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.update(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, RrPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      super.delete(session, pk);
    } finally {
      logger.methodEnd();
    }
  }

}
