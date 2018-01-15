package msf.fc.db.dao.slices;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcEsiId;
import msf.fc.common.data.FcEsiIdPK;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;


public class FcEsiDao extends FcAbstractCommonDao<FcEsiId, FcEsiIdPK> {


  @Override
  public FcEsiId read(SessionWrapper session, FcEsiIdPK pk) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcEsiId.class)
        .add(Restrictions.eq("id.swClusterId1", pk.getSwClusterId1()))
        .add(Restrictions.eq("id.swClusterId2", pk.getSwClusterId2()));
    return readByCriteria(session, criteria);
  }
}
