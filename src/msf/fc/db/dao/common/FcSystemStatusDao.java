package msf.fc.db.dao.common;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcSystemStatus;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;


public class FcSystemStatusDao extends FcAbstractCommonDao<FcSystemStatus, Integer> {

  @Override
  public FcSystemStatus read(SessionWrapper session, Integer systemId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcSystemStatus.class)
        .add(Restrictions.eq("systemId", systemId));
    return readByCriteria(session, criteria);
  }
}
