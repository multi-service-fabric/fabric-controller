package msf.fc.db.dao.slices;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcSliceId;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;


public class FcSliceIdDao extends FcAbstractCommonDao<FcSliceId, Integer> {

  @Override
  public FcSliceId read(SessionWrapper session, Integer layerType) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcSliceId.class)
        .add(Restrictions.eq("layerType", layerType));
    return readByCriteria(session, criteria);
  }

}
