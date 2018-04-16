
package msf.mfc.db.dao.slices;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.mfc.common.data.MfcCpId;
import msf.mfc.common.data.MfcCpIdPK;
import msf.mfc.common.data.MfcL2Slice;
import msf.mfc.db.dao.MfcAbstractCommonDao;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class MfcL2SliceDao extends MfcAbstractCommonDao<MfcL2Slice, String> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcL2SliceDao.class);

  public List<MfcL2Slice> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart();
      Criteria criteria = session.getSession().createCriteria(MfcL2Slice.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public MfcL2Slice readByVrfId(SessionWrapper session, int vrfId) throws MsfException {
    try {
      logger.methodStart();
      Criteria criteria = session.getSession().createCriteria(MfcL2Slice.class)
          .add(Restrictions.eqOrIsNull("vrfId", vrfId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public void deleteAll(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });

      Criteria criteria = session.getSession().createCriteria(MfcL2Slice.class);
      deleteByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, MfcL2Slice entity) throws MsfException {
    super.create(session, entity);

    MfcCpId cpId = new MfcCpId();
    MfcCpIdPK pk = new MfcCpIdPK();
    pk.setLayerType(SliceType.L2_SLICE.getCode());
    pk.setSliceId(entity.getSliceId());
    cpId.setId(pk);

    cpId.setNextId(1);
    MfcCpIdDao cpIdDao = new MfcCpIdDao();
    cpIdDao.create(session, cpId);
  }

  @Override
  public MfcL2Slice read(SessionWrapper session, String sliceId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(MfcL2Slice.class).add(Restrictions.eq("sliceId", sliceId));
    return readByCriteria(session, criteria);
  }

  @Override
  public void delete(SessionWrapper session, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId" }, new Object[] { session, sliceId });
      MfcCpIdDao cpIdDao = new MfcCpIdDao();
      MfcCpIdPK pk = new MfcCpIdPK();
      pk.setLayerType(SliceType.L2_SLICE.getCode());
      pk.setSliceId(sliceId);
      cpIdDao.delete(session, pk);

      super.delete(session, sliceId);
    } finally {
      logger.methodEnd();
    }
  }

}
