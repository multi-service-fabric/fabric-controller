package msf.fc.db.dao.clusters;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcLeafNode;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;


public class FcLeafNodeDao extends FcAbstractCommonDao<FcLeafNode, Long> {

  @Override
  public FcLeafNode read(SessionWrapper session, Long pk) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcLeafNode.class).add(Restrictions.eq("nodeInfoId", pk));
    return readByCriteria(session, criteria);
  }
}
