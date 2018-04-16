
package msf.mfc.db.dao.common;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcAsyncRequestsForLowerPK;
import msf.mfc.common.data.MfcAsyncRequestsForLowerRollback;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

public class MfcAsyncRequestsForLowerRollbackDao
    extends MfcAbstractCommonDao<MfcAsyncRequestsForLowerRollback, MfcAsyncRequestsForLowerPK> {

  @Override
  public MfcAsyncRequestsForLowerRollback read(SessionWrapper session, MfcAsyncRequestsForLowerPK pk)
      throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcAsyncRequestsForLowerRollback.class)
        .add(Restrictions.eq("id.clusterId", pk.getClusterId()))
        .add(Restrictions.eq("id.requestOperationId", pk.getRequestOperationId()));
    return readByCriteria(session, criteria);
  }

  public MfcAsyncRequestsForLowerRollback read(SessionWrapper session, String rollbackOperationId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcAsyncRequestsForLowerRollback.class)
        .add(Restrictions.eq("rollbackOperationId", rollbackOperationId));
    return readByCriteria(session, criteria);
  }
}
