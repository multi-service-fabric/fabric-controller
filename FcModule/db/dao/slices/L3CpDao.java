package msf.fc.db.dao.slices;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.EdgePoint;
import msf.fc.common.data.L3Cp;
import msf.fc.common.data.L3CpPK;
import msf.fc.common.data.L3CpStaticRouteOption;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;
import msf.fc.db.dao.clusters.EdgePointDao;
import msf.fc.db.dao.clusters.NodeDao;

public class L3CpDao extends AbstractCommonDao<L3Cp, L3CpPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(L3CpDao.class);

  public List<L3Cp> readList(SessionWrapper session, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(L3Cp.class)
          .add(Restrictions.eq("id.sliceId", sliceId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<L3Cp> readList(SessionWrapper session, String sliceId, Integer reservationStatus) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId", "reservationStatus" },
          new Object[] { session, sliceId, reservationStatus });
      Criteria criteria = session.getSession().createCriteria(L3Cp.class)
          .add(Restrictions.eq("id.sliceId", sliceId))
          .add(Restrictions.eq("reservationStatus", reservationStatus));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<L3Cp> readList(SessionWrapper session, Long nodeInfoId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeInfoId" }, new Object[] { session, nodeInfoId });
      NodeDao nodeDao = new NodeDao();
      Node node = nodeDao.read(session, nodeInfoId);

      if (Objects.isNull(node)) {
        return new ArrayList<>();
      }

      EdgePointDao edgePointDao = new EdgePointDao();
      Criteria criteria = createEdgePointCriteria(session, node.getPhysicalIfs(), node.getLagIfs());
      List<EdgePoint> edgePoints = edgePointDao.readListByCriteria(session, criteria);

      List<L3Cp> l3Cps = new ArrayList<>();
      for (EdgePoint edgePoint : edgePoints) {
        l3Cps.addAll(getList(edgePoint.getL3Cps()));
      }
      return l3Cps;
    } finally {
      logger.methodEnd();
    }
  }

  public List<L3Cp> readList(SessionWrapper session, String sliceId, String swClusterId, Integer status)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId", "swClusterId", "status" },
          new Object[] { session, sliceId, swClusterId, status });
      Criteria criteria = session.getSession().createCriteria(L3Cp.class)
          .add(Restrictions.eq("id.sliceId", sliceId))
          .add(Restrictions.eq("edgePoint.id.swClusterId", swClusterId))
          .add(Restrictions.eq("status", status));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<L3Cp> readListBySwClusterAndCpStatus(SessionWrapper session, String swClusterId, Integer status)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId", "status" },
          new Object[] { session, swClusterId, status });
      Criteria criteria = session.getSession().createCriteria(L3Cp.class)
          .add(Restrictions.eq("edgePoint.id.swClusterId", swClusterId))
          .add(Restrictions.eq("status", status));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public void updateList(SessionWrapper session, List<L3Cp> l3Cps) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "l3Cps" }, new Object[] { session, l3Cps });
      for (L3Cp l3Cp : l3Cps) {
        update(session, l3Cp);
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, L3Cp entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);

      L3CpStaticRouteOptionDao l3CpStaticRouteOptionDao = new L3CpStaticRouteOptionDao();
      for (L3CpStaticRouteOption l3CpStaticRouteOption : getList(entity.getL3CpStaticRouteOptions())) {
        l3CpStaticRouteOptionDao.create(session, l3CpStaticRouteOption);
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public L3Cp read(SessionWrapper session, L3CpPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      Criteria criteria = session.getSession().createCriteria(L3Cp.class)
          .add(Restrictions.eq("id.sliceId", pk.getSliceId()))
          .add(Restrictions.eq("id.cpId", pk.getCpId()));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void update(SessionWrapper session, L3Cp entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.update(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, L3CpPK pk) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "pk" }, new Object[] { session, pk });
      super.delete(session, pk);
    } finally {
      logger.methodEnd();
    }
  }

}
