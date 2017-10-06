package msf.fc.db.dao.slices;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.constant.SliceType;
import msf.fc.common.data.CpId;
import msf.fc.common.data.CpIdPK;
import msf.fc.common.data.L3Slice;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.AbstractCommonDao;

public class L3SliceDao extends AbstractCommonDao<L3Slice, String> {

  private static final MsfLogger logger = MsfLogger.getInstance(L3SliceDao.class);

  public List<L3Slice> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(L3Slice.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void create(SessionWrapper session, L3Slice entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.create(session, entity);

      CpId cpId = new CpId();
      CpIdPK pk = new CpIdPK();
      pk.setLayerTypeEnum(SliceType.L3_SLICE);
      pk.setSliceId(entity.getSliceId());
      cpId.setId(pk);
      cpId.setNextId(1);

      CpIdDao cpIdDao = new CpIdDao();
      cpIdDao.create(session, cpId);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void update(SessionWrapper session, L3Slice entity) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "entity" }, new Object[] { session, entity });
      super.update(session, entity);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public L3Slice read(SessionWrapper session, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId" }, new Object[] { session, sliceId });
      Criteria criteria = session.getSession().createCriteria(L3Slice.class).add(Restrictions.eq("sliceId", sliceId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public void delete(SessionWrapper session, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId" }, new Object[] { session, sliceId });
      super.delete(session, sliceId);

      CpIdDao cpIdDao = new CpIdDao();
      CpIdPK pk = new CpIdPK();
      pk.setLayerTypeEnum(SliceType.L3_SLICE);
      pk.setSliceId(sliceId);
      cpIdDao.delete(session, pk);
    } finally {
      logger.methodEnd();
    }
  }

}
