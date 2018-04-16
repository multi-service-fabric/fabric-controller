
package msf.mfc.db.dao.slices;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcCpId;
import msf.mfc.common.data.MfcCpIdPK;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

public class MfcCpIdDao extends MfcAbstractCommonDao<MfcCpId, MfcCpIdPK> {

  @Override
  public MfcCpId read(SessionWrapper session, MfcCpIdPK pk) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcCpId.class)
        .add(Restrictions.eq("id.layerType", pk.getLayerType())).add(Restrictions.eq("id.sliceId", pk.getSliceId()));
    return readByCriteria(session, criteria);
  }
}
