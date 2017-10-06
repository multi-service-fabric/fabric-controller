package msf.fc.db.dao.clusters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.InternalLinkIf;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class InternalLinkIfDao extends AbstractCommonDao<InternalLinkIf, Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(InternalLinkIfDao.class);

  public List<InternalLinkIf> readList(SessionWrapper session, String swClusterId, Integer nodeType, Integer nodeId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId", "nodeType", "nodeId" },
          new Object[] { session, swClusterId, nodeType, nodeId });
      LagIfDao lagIfDao = new LagIfDao();
      List<LagIf> lagIfs = lagIfDao.readList(session, swClusterId, nodeType, nodeId);

      if (CollectionUtils.isEmpty(lagIfs)) {
        return new ArrayList<>();
      }

      List<Long> lagIfInfoIds = new ArrayList<>();
      for (LagIf lagIf : lagIfs) {
        lagIfInfoIds.add(lagIf.getLagIfInfoId());
      }

      Criteria criteria = session.getSession().createCriteria(InternalLinkIf.class)
          .add(Restrictions.in("lagIf.lagIfInfoId", lagIfInfoIds));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<InternalLinkIf> readlListBySwCluster(SessionWrapper session, String swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId" }, new Object[] { session, swClusterId });
      NodeDao nodeDao = new NodeDao();
      List<Node> nodeList = nodeDao.readList(session, swClusterId);

      List<InternalLinkIf> internalLinkIfs = new ArrayList<>();
      for (Node node : nodeList) {
        for (LagIf lagIf : getList(node.getLagIfs())) {
          if (lagIf.getInternalLinkIf() != null) {
            internalLinkIfs.add(lagIf.getInternalLinkIf());
          }
        }
      }
      return internalLinkIfs;
    } finally {
      logger.methodEnd();
    }
  }

  public InternalLinkIf readFromBiggestId(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(InternalLinkIf.class)
          .addOrder(Order.desc("internalLinkIfId"));
      List<InternalLinkIf> entities = readListByCriteria(session, criteria);
      return entities.isEmpty() ? null : entities.get(0);
    } finally {
      logger.methodEnd();
    }
  }

  public void updateList(SessionWrapper session, List<InternalLinkIf> internalLinkIfs) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "internalLinkIfs" }, new Object[] { session, internalLinkIfs });
      for (InternalLinkIf internalLinkIf : internalLinkIfs) {
        update(session, internalLinkIf);
      }
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public InternalLinkIf read(SessionWrapper session, Integer internalLinkIfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "internalLinkIfId" }, new Object[] { session, internalLinkIfId });
      Criteria criteria = session.getSession().createCriteria(InternalLinkIf.class)
          .add(Restrictions.eq("internalLinkIfId", internalLinkIfId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void update(SessionWrapper session, InternalLinkIf entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.update(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, Integer internalLinkIfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "internalLinkIfId" }, new Object[] { session, internalLinkIfId });
      super.delete(session, internalLinkIfId);
    } finally {
      logger.methodEnd();
    }
  }

}
