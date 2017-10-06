package msf.fc.db.dao.slices;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.L2Cp;
import msf.fc.common.data.L2CpPK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class L2CpDao extends AbstractCommonDao<L2Cp, L2CpPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(L2CpDao.class);

  public List<L2Cp> readList(SessionWrapper session, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(L2Cp.class)
          .add(Restrictions.eq("id.sliceId", sliceId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<L2Cp> readList(SessionWrapper session, String sliceId, Integer reservationStatus) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId", "reservationStatus" },
          new Object[] { session, sliceId, reservationStatus });
      Criteria criteria = session.getSession().createCriteria(L2Cp.class)
          .add(Restrictions.eq("id.sliceId", sliceId))
          .add(Restrictions.eq("reservationStatus", reservationStatus));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<L2Cp> readList(SessionWrapper session, String sliceId, String swClusterId, Integer status)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId", "swClusterId", "status" },
          new Object[] { session, sliceId, swClusterId, status });
      Criteria criteria = session.getSession().createCriteria(L2Cp.class)
          .add(Restrictions.eq("id.sliceId", sliceId))
          .add(Restrictions.eq("edgePoint.id.swClusterId", swClusterId))
          .add(Restrictions.eq("status", status));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<L2Cp> readListBySwClusterAndCpStatus(SessionWrapper session, String swClusterId, Integer status)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId", "status" },
          new Object[] { session, swClusterId, status });
      Criteria criteria = session.getSession().createCriteria(L2Cp.class)
          .add(Restrictions.eq("edgePoint.id.swClusterId", swClusterId))
          .add(Restrictions.eq("status", status));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public void updateList(SessionWrapper session, List<L2Cp> l2Cps) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "l2Cps" }, new Object[] { session, l2Cps });
      for (L2Cp l2Cp : l2Cps) {
        update(session, l2Cp);
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, L2Cp entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public L2Cp read(SessionWrapper session, L2CpPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      Criteria criteria = session.getSession().createCriteria(L2Cp.class)
          .add(Restrictions.eq("id.sliceId", pk.getSliceId()))
          .add(Restrictions.eq("id.cpId", pk.getCpId()));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void update(SessionWrapper session, L2Cp entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.update(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, L2CpPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      super.delete(session, pk);
    } finally {
      logger.methodEnd();
    }
  }

}
