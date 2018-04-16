
package msf.mfc.db.dao.common;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcAsyncRequestsForLower;
import msf.mfc.common.data.MfcAsyncRequestsForLowerPK;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

public class MfcAsyncRequestsForLowerDao
    extends MfcAbstractCommonDao<MfcAsyncRequestsForLower, MfcAsyncRequestsForLowerPK> {
  @Override
  public MfcAsyncRequestsForLower read(SessionWrapper session, MfcAsyncRequestsForLowerPK pk) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcAsyncRequestsForLower.class)
        .add(Restrictions.eq("id.clusterId", pk.getClusterId()))
        .add(Restrictions.eq("id.requestOperationId", pk.getRequestOperationId()));
    return readByCriteria(session, criteria);
  }

  public MfcAsyncRequestsForLower read(SessionWrapper session, String requestOperationId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcAsyncRequestsForLower.class)
        .add(Restrictions.eq("id.requestOperationId", requestOperationId));
    return readByCriteria(session, criteria);
  }
}
