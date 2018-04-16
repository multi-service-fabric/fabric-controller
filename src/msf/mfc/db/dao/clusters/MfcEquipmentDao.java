
package msf.mfc.db.dao.clusters;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcEquipment;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class MfcEquipmentDao extends MfcAbstractCommonDao<MfcEquipment, Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcEquipmentDao.class);

  public List<MfcEquipment> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(MfcEquipment.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public void deleteAll(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });

      Criteria criteria = session.getSession().createCriteria(MfcEquipment.class);
      deleteByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public MfcEquipment readFromBiggestId(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(MfcEquipment.class)
          .addOrder(Order.desc("equipmentTypeInfoId"));
      List<MfcEquipment> entities = readListByCriteria(session, criteria);
      return entities.isEmpty() ? null : entities.get(0);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public MfcEquipment read(SessionWrapper session, Integer pk) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcEquipment.class)
        .add(Restrictions.eq("equipmentTypeInfoId", pk));
    return readByCriteria(session, criteria);
  }
}
