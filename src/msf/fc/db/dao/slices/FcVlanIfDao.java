
package msf.fc.db.dao.slices;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcVlanIf;
import msf.fc.common.data.FcVlanIfPK;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class FcVlanIfDao extends FcAbstractCommonDao<FcVlanIf, FcVlanIfPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcVlanIfDao.class);

  public List<FcVlanIf> readList(SessionWrapper session, Long nodeInfoId) throws MsfException {
    try {
      logger.methodStart();
      Criteria criteria = session.getSession().createCriteria(FcVlanIf.class)
          .add(Restrictions.eq("id.nodeInfoId", nodeInfoId.intValue()));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcVlanIf read(SessionWrapper session, FcVlanIfPK pk) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcVlanIf.class)
        .add(Restrictions.eq("id.nodeInfoId", pk.getNodeInfoId()))
        .add(Restrictions.eq("id.vlanIfId", pk.getVlanIfId()));
    return readByCriteria(session, criteria);
  }
}
