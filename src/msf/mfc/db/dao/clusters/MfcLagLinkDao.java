
package msf.mfc.db.dao.clusters;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcLagLink;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

public class MfcLagLinkDao extends MfcAbstractCommonDao<MfcLagLink, Integer> {

  @Override
  public MfcLagLink read(SessionWrapper session, Integer clusterLinkIfId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcLagLink.class)
        .add(Restrictions.eq("clusterLinkIfId", clusterLinkIfId));
    return readByCriteria(session, criteria);
  }
}
