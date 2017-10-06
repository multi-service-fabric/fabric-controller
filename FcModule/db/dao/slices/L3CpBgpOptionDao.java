package msf.fc.db.dao.slices;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.L3CpBgpOption;
import msf.fc.common.data.L3CpBgpOptionPK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class L3CpBgpOptionDao extends AbstractCommonDao<L3CpBgpOption, L3CpBgpOptionPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(L3CpBgpOptionDao.class);

  @Override
  public void create(SessionWrapper session, L3CpBgpOption entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public L3CpBgpOption read(SessionWrapper session, L3CpBgpOptionPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      Criteria criteria = session.getSession().createCriteria(L3CpBgpOption.class)
          .add(Restrictions.eq("id.sliceId", pk.getSliceId()))
          .add(Restrictions.eq("id.cpId", pk.getCpId()));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, L3CpBgpOptionPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      super.delete(session, pk);
    } finally {
      logger.methodEnd();
    }
  }

}
