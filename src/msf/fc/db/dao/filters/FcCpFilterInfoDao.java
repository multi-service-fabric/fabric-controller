
package msf.fc.db.dao.filters;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcCpFilterInfo;
import msf.fc.common.data.FcCpFilterInfoPK;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class FcCpFilterInfoDao extends FcAbstractCommonDao<FcCpFilterInfo, FcCpFilterInfoPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcCpFilterInfoDao.class);

  public List<FcCpFilterInfo> readList(SessionWrapper session, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId" }, new Object[] { session, sliceId });
      Criteria criteria = session.getSession().createCriteria(FcCpFilterInfo.class)
          .add(Restrictions.eq("l2Cp.id.sliceId", sliceId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<FcCpFilterInfo> readList(SessionWrapper session, String sliceId, String cpId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId", "cpId" }, new Object[] { session, sliceId, cpId });
      Criteria criteria = session.getSession().createCriteria(FcCpFilterInfo.class)
          .add(Restrictions.eq("l2Cp.id.sliceId", sliceId)).add(Restrictions.eq("l2Cp.id.cpId", cpId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcCpFilterInfo read(SessionWrapper session, FcCpFilterInfoPK id) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcCpFilterInfo.class).add(Restrictions.eq("id", id));
    return readByCriteria(session, criteria);
  }
}
