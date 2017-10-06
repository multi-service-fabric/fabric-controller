package msf.fc.db.dao.clusters;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.Equipment;
import msf.fc.common.data.EquipmentPK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class EquipmentDao extends AbstractCommonDao<Equipment, EquipmentPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(EquipmentDao.class);

  public List<Equipment> readList(SessionWrapper session, String swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId" }, new Object[] { session, swClusterId });
      Criteria criteria = session.getSession().createCriteria(Equipment.class)
          .add(Restrictions.eq("id.swClusterId", swClusterId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<Equipment> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(Equipment.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public Equipment read(SessionWrapper session, String platformName, String osName, String firmwareVersion)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "platformName", "osName", "firmwareVersion" },
          new Object[] { session, platformName, osName, firmwareVersion });
      Criteria criteria = session.getSession().createCriteria(Equipment.class)
          .add(Restrictions.eq("platformName", platformName))
          .add(Restrictions.eq("osName", osName))
          .add(Restrictions.eq("firmwareVersion", firmwareVersion));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public Equipment readFromBiggestId(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(Equipment.class)
          .addOrder(Order.desc("id.equipmentTypeId"));
      List<Equipment> entities = readListByCriteria(session, criteria);
      return entities.isEmpty() ? null : entities.get(0);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, Equipment entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public Equipment read(SessionWrapper session, EquipmentPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      Criteria criteria = session.getSession().createCriteria(Equipment.class)
          .add(Restrictions.eq("id.swClusterId", pk.getSwClusterId()))
          .add(Restrictions.eq("id.equipmentTypeId", pk.getEquipmentTypeId()));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, EquipmentPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      super.delete(session, pk);
    } finally {
      logger.methodEnd();
    }
  }

}
