package msf.fc.db.dao.clusters;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcEquipment;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;


public class FcEquipmentDao extends FcAbstractCommonDao<FcEquipment, Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcEquipmentDao.class);


  
  public List<FcEquipment> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(FcEquipment.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  
  public FcEquipment readFromBiggestId(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(FcEquipment.class)
          .addOrder(Order.desc("equipmentTypeId"));
      List<FcEquipment> entities = readListByCriteria(session, criteria);
      return entities.isEmpty() ? null : entities.get(0);
    } finally {
      logger.methodEnd();
    }
  }


  @Override
  public FcEquipment read(SessionWrapper session, Integer pk) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcEquipment.class)
        .add(Restrictions.eq("equipmentTypeId", pk));
    return readByCriteria(session, criteria);
  }

}
