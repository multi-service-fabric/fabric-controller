
package msf.mfc.db.dao.slices;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcL3Cp;
import msf.mfc.common.data.MfcL3CpPK;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class MfcL3CpDao extends MfcAbstractCommonDao<MfcL3Cp, MfcL3CpPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3CpDao.class);

  public List<MfcL3Cp> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(MfcL3Cp.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<MfcL3Cp> readListBySliceId(SessionWrapper session, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId" }, new Object[] { session, sliceId });
      Criteria criteria = session.getSession().createCriteria(MfcL3Cp.class)
          .add(Restrictions.eq("id.sliceId", sliceId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public MfcL3Cp read(SessionWrapper session, MfcL3CpPK pk) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcL3Cp.class)
        .add(Restrictions.eq("id.sliceId", pk.getSliceId())).add(Restrictions.eq("id.cpId", pk.getCpId()));
    return readByCriteria(session, criteria);
  }

}
