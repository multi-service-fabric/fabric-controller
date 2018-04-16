
package msf.mfc.db.dao.clusters;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.constant.ClusterProvisioningStatus;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class MfcSwClusterDao extends MfcAbstractCommonDao<MfcSwCluster, Integer> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcSwClusterDao.class);

  public List<MfcSwCluster> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(MfcSwCluster.class)
          .add(Restrictions.eq("clusterStatus", ClusterProvisioningStatus.COMPLETED.getCode()));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public MfcSwCluster readAfterDeleteProcess(SessionWrapper session, Integer swClusterId) throws MsfException {

    try {
      logger.methodStart(new String[] { "session", "swClusterId" }, new Object[] { session, swClusterId });
      Criteria criteria = session.getSession().createCriteria(MfcSwCluster.class)
          .add(Restrictions.eq("swClusterId", swClusterId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public MfcSwCluster read(SessionWrapper session, Integer swClusterId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcSwCluster.class)
        .add(Restrictions.eq("swClusterId", swClusterId))
        .add(Restrictions.eq("clusterStatus", ClusterProvisioningStatus.COMPLETED.getCode()));
    return readByCriteria(session, criteria);
  }

  @Override
  public void delete(SessionWrapper session, Integer swClusterId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "swClusterId" }, new Object[] { session, swClusterId });
      MfcSwCluster entity = readAfterDeleteProcess(session, swClusterId);
      if (entity != null) {
        session.getSession().delete(entity);
      }
    } catch (HibernateException he) {
      logger.error("Error delete.", he);
      throw new MsfException(ErrorCode.DATABASE_OPERATION_ERROR, "Database record delete failed.");
    } finally {
      logger.methodEnd();
    }
  }
}
