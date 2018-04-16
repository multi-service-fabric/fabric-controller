
package msf.mfc.db.dao.common;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcSystemStatus;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

public class MfcSystemStatusDao extends MfcAbstractCommonDao<MfcSystemStatus, Integer> {

  @Override
  public MfcSystemStatus read(SessionWrapper session, Integer systemId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcSystemStatus.class)
        .add(Restrictions.eq("systemId", systemId));
    return readByCriteria(session, criteria);
  }
}
