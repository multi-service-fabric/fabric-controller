package msf.fc.db.dao.clusters;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.EquipmentIf;
import msf.fc.common.data.EquipmentIfPK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class EquipmentIfDao extends AbstractCommonDao<EquipmentIf, EquipmentIfPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(EquipmentIfDao.class);

  @Override
  public EquipmentIf read(SessionWrapper session, EquipmentIfPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      Criteria criteria = session.getSession().createCriteria(EquipmentIf.class)
          .add(Restrictions.eq("id.swClusterId", pk.getSwClusterId()))
          .add(Restrictions.eq("id.equipmentTypeId", pk.getEquipmentTypeId()))
          .add(Restrictions.eq("id.physicalIfId", pk.getPhysicalIfId()))
          .add(Restrictions.eq("id.speedCapability", pk.getSpeedCapability()));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, EquipmentIfPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      super.delete(session, pk);
    } finally {
      logger.methodEnd();
    }
  }

}
