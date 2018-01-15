package msf.fc.db.dao.slices;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcVlanIfId;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;


public class FcVlanIfIdDao extends FcAbstractCommonDao<FcVlanIfId, Integer> {

  @Override
  public FcVlanIfId read(SessionWrapper session, Integer nodeInfoId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcVlanIfId.class)
        .add(Restrictions.eq("nodeInfoId", nodeInfoId));
    return readByCriteria(session, criteria);
  }
}
