
package msf.mfc.db.dao.clusters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcClusterLinkIf;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class MfcClusterLinkIfDao extends MfcAbstractCommonDao<MfcClusterLinkIf, Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterLinkIfDao.class);

  public List<MfcClusterLinkIf> readListBySwClusterId(SessionWrapper session, String swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId" }, new Object[] { session, swClusterId });

      MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();
      MfcSwCluster mfcSwCluster = mfcSwClusterDao.read(session, Integer.valueOf(swClusterId));

      if (Objects.isNull(mfcSwCluster)) {
        return new ArrayList<>();
      }
      return mfcSwCluster.getClusterLinkIfs();
    } finally {
      logger.methodEnd();
    }
  }

  public List<MfcClusterLinkIf> readListByOppositeSwClusterId(SessionWrapper session, String oppositeSwClusterId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "oppositeSwClusterId" },
          new Object[] { session, oppositeSwClusterId });

      Criteria criteria = session.getSession().createCriteria(MfcClusterLinkIf.class)
          .add(Restrictions.eq("oppositeSwClusterId", Integer.valueOf(oppositeSwClusterId)));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<MfcClusterLinkIf> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(MfcClusterLinkIf.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, MfcClusterLinkIf entity) throws MsfException {
    super.create(session, entity);

    if (entity.getPhysicalLink() != null) {

      MfcPhysicalLinkDao mfcPhysicalLinkDao = new MfcPhysicalLinkDao();
      mfcPhysicalLinkDao.create(session, entity.getPhysicalLink());
    } else {

      MfcLagLinkDao mfcLagLinkDao = new MfcLagLinkDao();
      mfcLagLinkDao.create(session, entity.getLagLink());
    }
  }

  @Override
  public MfcClusterLinkIf read(SessionWrapper session, Integer clusterLinkIfId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcClusterLinkIf.class)
        .add(Restrictions.eq("clusterLinkIfId", clusterLinkIfId));
    return readByCriteria(session, criteria);
  }
}
