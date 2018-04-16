
package msf.mfc.db.dao.slices;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcEsiId;
import msf.mfc.common.data.MfcEsiIdPK;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

public class MfcEsiDao extends MfcAbstractCommonDao<MfcEsiId, MfcEsiIdPK> {

  @Override
  public MfcEsiId read(SessionWrapper session, MfcEsiIdPK pk) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcEsiId.class)
        .add(Restrictions.eq("id.swClusterId1", pk.getSwClusterId1()))
        .add(Restrictions.eq("id.swClusterId2", pk.getSwClusterId2()));
    return readByCriteria(session, criteria);
  }

}
