package msf.fc.db.dao.slices;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcCpId;
import msf.fc.common.data.FcCpIdPK;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;


public class FcCpIdDao extends FcAbstractCommonDao<FcCpId, FcCpIdPK> {


  @Override
  public FcCpId read(SessionWrapper session, FcCpIdPK pk) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcCpId.class)
        .add(Restrictions.eq("id.layerType", pk.getLayerType())).add(Restrictions.eq("id.sliceId", pk.getSliceId()));
    return readByCriteria(session, criteria);
  }

}
