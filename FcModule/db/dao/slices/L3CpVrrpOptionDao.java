package msf.fc.db.dao.slices;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.L3CpVrrpOption;
import msf.fc.common.data.L3CpVrrpOptionPK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class L3CpVrrpOptionDao extends AbstractCommonDao<L3CpVrrpOption, L3CpVrrpOptionPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(L3CpVrrpOptionDao.class);

  @Override
  public void create(SessionWrapper session, L3CpVrrpOption entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public L3CpVrrpOption read(SessionWrapper session, L3CpVrrpOptionPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      Criteria criteria = session.getSession().createCriteria(L3CpVrrpOption.class)
          .add(Restrictions.eq("id.sliceId", pk.getSliceId()))
          .add(Restrictions.eq("id.cpId", pk.getCpId()));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, L3CpVrrpOptionPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      super.delete(session, pk);
    } finally {
      logger.methodEnd();
    }
  }

}
