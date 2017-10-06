package msf.fc.db.dao.slices;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.L3CpStaticRouteOption;
import msf.fc.common.data.L3CpStaticRouteOptionPK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class L3CpStaticRouteOptionDao extends AbstractCommonDao<L3CpStaticRouteOption, L3CpStaticRouteOptionPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(L3CpStaticRouteOptionDao.class);

  @Override
  public void create(SessionWrapper session, L3CpStaticRouteOption entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public L3CpStaticRouteOption read(SessionWrapper session, L3CpStaticRouteOptionPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      Criteria criteria = session.getSession().createCriteria(L3CpStaticRouteOption.class)
          .add(Restrictions.eq("id.sliceId", pk.getSliceId())).add(Restrictions.eq("id.cpId", pk.getCpId()))
          .add(Restrictions.eq("id.addressType", pk.getAddressType()))
          .add(Restrictions.eq("id.destinationAddress", pk.getDestinationAddress()))
          .add(Restrictions.eq("id.prefix", pk.getPrefix()))
          .add(Restrictions.eq("id.nexthopAddress", pk.getNexthopAddress()));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, L3CpStaticRouteOptionPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      super.delete(session, pk);
    } finally {
      logger.methodEnd();
    }
  }

}
