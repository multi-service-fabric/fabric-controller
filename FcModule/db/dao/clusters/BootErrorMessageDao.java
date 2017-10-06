package msf.fc.db.dao.clusters;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.BootErrorMessage;
import msf.fc.common.data.BootErrorMessagePK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class BootErrorMessageDao extends AbstractCommonDao<BootErrorMessage, BootErrorMessagePK> {

  private static final MsfLogger logger = MsfLogger.getInstance(BootErrorMessageDao.class);

  @Override
  public BootErrorMessage read(SessionWrapper session, BootErrorMessagePK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      Criteria criteria = session.getSession().createCriteria(BootErrorMessage.class)
          .add(Restrictions.eq("id.swClusterId", pk.getSwClusterId()))
          .add(Restrictions.eq("id.equipmentTypeId", pk.getEquipmentTypeId()))
          .add(Restrictions.eq("id.bootErrorMsg", pk.getBootErrorMsg()));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

}
