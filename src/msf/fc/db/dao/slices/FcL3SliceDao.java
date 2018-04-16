
package msf.fc.db.dao.slices;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcCpId;
import msf.fc.common.data.FcCpIdPK;
import msf.fc.common.data.FcL3Slice;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class FcL3SliceDao extends FcAbstractCommonDao<FcL3Slice, String> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3SliceDao.class);

  public List<FcL3Slice> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart();
      Criteria criteria = session.getSession().createCriteria(FcL3Slice.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public FcL3Slice readByVrfId(SessionWrapper session, int vrfId) throws MsfException {
    try {
      logger.methodStart();
      Criteria criteria = session.getSession().createCriteria(FcL3Slice.class)
          .add(Restrictions.eqOrIsNull("vrfId", vrfId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, FcL3Slice entity) throws MsfException {
    super.create(session, entity);

    FcCpId cpId = new FcCpId();
    FcCpIdPK pk = new FcCpIdPK();
    pk.setLayerType(SliceType.L3_SLICE.getCode());
    pk.setSliceId(entity.getSliceId());
    cpId.setId(pk);

    cpId.setNextId(1);
    FcCpIdDao cpIdDao = new FcCpIdDao();
    cpIdDao.create(session, cpId);
  }

  @Override
  public FcL3Slice read(SessionWrapper session, String sliceId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcL3Slice.class).add(Restrictions.eq("sliceId", sliceId));
    return readByCriteria(session, criteria);
  }

  @Override
  public void delete(SessionWrapper session, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId" }, new Object[] { session, sliceId });
      FcCpIdDao cpIdDao = new FcCpIdDao();
      FcCpIdPK pk = new FcCpIdPK();
      pk.setLayerType(SliceType.L3_SLICE.getCode());
      pk.setSliceId(sliceId);
      cpIdDao.delete(session, pk);

      super.delete(session, sliceId);
    } finally {
      logger.methodEnd();
    }
  }

}
