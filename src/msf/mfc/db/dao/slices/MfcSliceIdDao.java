
package msf.mfc.db.dao.slices;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcSliceId;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

public class MfcSliceIdDao extends MfcAbstractCommonDao<MfcSliceId, Integer> {

  @Override
  public MfcSliceId read(SessionWrapper session, Integer layerType) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcSliceId.class)
        .add(Restrictions.eq("layerType", layerType));
    return readByCriteria(session, criteria);
  }
}
