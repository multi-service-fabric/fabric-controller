
package msf.mfc.db.dao.slices;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcL2Cp;
import msf.mfc.common.data.MfcL2CpPK;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class MfcL2CpDao extends MfcAbstractCommonDao<MfcL2Cp, MfcL2CpPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2CpDao.class);

  public List<MfcL2Cp> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(MfcL2Cp.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<MfcL2Cp> readListByEsi(SessionWrapper session, String esi) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "esi" }, new Object[] { session, esi });
      Criteria criteria = session.getSession().createCriteria(MfcL2Cp.class).add(Restrictions.eq("esi", esi));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<MfcL2Cp> readListBySliceId(SessionWrapper session, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId" }, new Object[] { session, sliceId });
      Criteria criteria = session.getSession().createCriteria(MfcL2Cp.class)
          .add(Restrictions.eq("id.sliceId", sliceId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public MfcL2Cp read(SessionWrapper session, MfcL2CpPK pk) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcL2Cp.class)
        .add(Restrictions.eq("id.sliceId", pk.getSliceId())).add(Restrictions.eq("id.cpId", pk.getCpId()));
    return readByCriteria(session, criteria);
  }

}
