
package msf.mfc.db.dao.slices;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcCpId;
import msf.mfc.common.data.MfcCpIdPK;
import msf.mfc.common.data.MfcL3Slice;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class MfcL3SliceDao extends MfcAbstractCommonDao<MfcL3Slice, String> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL3SliceDao.class);

  public List<MfcL3Slice> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart();
      Criteria criteria = session.getSession().createCriteria(MfcL3Slice.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public MfcL3Slice readByVrfId(SessionWrapper session, int vrfId) throws MsfException {
    try {
      logger.methodStart();
      Criteria criteria = session.getSession().createCriteria(MfcL3Slice.class)
          .add(Restrictions.eqOrIsNull("vrfId", vrfId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public void deleteAll(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });

      Criteria criteria = session.getSession().createCriteria(MfcL3Slice.class);
      deleteByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, MfcL3Slice entity) throws MsfException {
    super.create(session, entity);

    MfcCpId cpId = new MfcCpId();
    MfcCpIdPK pk = new MfcCpIdPK();
    pk.setLayerType(SliceType.L3_SLICE.getCode());
    pk.setSliceId(entity.getSliceId());
    cpId.setId(pk);

    cpId.setNextId(1);
    MfcCpIdDao cpIdDao = new MfcCpIdDao();
    cpIdDao.create(session, cpId);
  }

  @Override
  public MfcL3Slice read(SessionWrapper session, String sliceId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcL3Slice.class).add(Restrictions.eq("sliceId", sliceId));
    return readByCriteria(session, criteria);
  }

  @Override
  public void delete(SessionWrapper session, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId" }, new Object[] { session, sliceId });
      MfcCpIdDao cpIdDao = new MfcCpIdDao();
      MfcCpIdPK pk = new MfcCpIdPK();
      pk.setLayerType(SliceType.L3_SLICE.getCode());
      pk.setSliceId(sliceId);
      cpIdDao.delete(session, pk);

      super.delete(session, sliceId);
    } finally {
      logger.methodEnd();
    }
  }

}
