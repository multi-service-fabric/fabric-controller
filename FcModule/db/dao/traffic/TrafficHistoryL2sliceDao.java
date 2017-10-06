package msf.fc.db.dao.traffic;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.TrafficHistoryL2slice;
import msf.fc.common.data.TrafficHistoryL2slicePK;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class TrafficHistoryL2sliceDao extends AbstractCommonDao<TrafficHistoryL2slice, TrafficHistoryL2slicePK> {

  private static final MsfLogger logger = MsfLogger.getInstance(TrafficHistoryL2sliceDao.class);

  public void create(SessionWrapper session, List<TrafficHistoryL2slice> entities) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entities" }, new Object[] { session, entities });
      for (TrafficHistoryL2slice entity : entities) {
        super.create(session, entity);
      }
    } finally {
      logger.methodEnd();
    }
  }

  public List<TrafficHistoryL2slice> readList(SessionWrapper session, Date startTime, Date endTime)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "startTime", "endTime" },
          new Object[] { session, startTime, endTime });
      Criteria criteria = session.getSession().createCriteria(TrafficHistoryL2slice.class)
          .add(Restrictions.between("id.occurredTime", startTime, endTime))
          .addOrder(Order.asc("id.occurredTime"));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public void deleteExpiredData(SessionWrapper session, Date outsideTime) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "outsideTime" }, new Object[] { session, outsideTime });
      String sql = "delete TrafficHistoryL2slice where id.occurredTime < :outsideTime";
      Query query = session.getSession().createQuery(sql)
          .setTimestamp("outsideTime", outsideTime);
      deleteByQuery(session, query);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, TrafficHistoryL2slice entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public TrafficHistoryL2slice read(SessionWrapper session, TrafficHistoryL2slicePK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      Criteria criteria = session.getSession().createCriteria(TrafficHistoryL2slice.class)
          .add(Restrictions.eq("id.occurredTime", pk.getOccurredTime()))
          .add(Restrictions.eq("id.startClusterId", pk.getStartClusterId()))
          .add(Restrictions.eq("id.startLeafNodeId", pk.getStartLeafNodeId()))
          .add(Restrictions.eq("id.startEdgePointId", pk.getStartEdgePointId()))
          .add(Restrictions.eq("id.endClusterId", pk.getEndClusterId()))
          .add(Restrictions.eq("id.endLeafNodeId", pk.getEndLeafNodeId()))
          .add(Restrictions.eq("id.endEdgePointId", pk.getEndEdgePointId()));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, TrafficHistoryL2slicePK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      super.delete(session, pk);
    } finally {
      logger.methodEnd();
    }
  }

}
