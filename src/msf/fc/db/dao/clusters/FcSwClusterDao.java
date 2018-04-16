
package msf.fc.db.dao.clusters;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcSwCluster;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

public class FcSwClusterDao extends FcAbstractCommonDao<FcSwCluster, Integer> {

  @Override
  public FcSwCluster read(SessionWrapper session, Integer swClusterId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcSwCluster.class)
        .add(Restrictions.eq("swClusterId", swClusterId));
    return readByCriteria(session, criteria);
  }

}
