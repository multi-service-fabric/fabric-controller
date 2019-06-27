
package msf.fc.db.dao.slices;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcIrbInstance;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class FcIrbInstanceDao extends FcAbstractCommonDao<FcIrbInstance, Long> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcIrbInstanceDao.class);

  public List<FcIrbInstance> readList(SessionWrapper session, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId" }, new Object[] { session, sliceId });
      Criteria criteria = session.getSession().createCriteria(FcIrbInstance.class)
          .add(Restrictions.eq("l2Slice.sliceId", sliceId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<FcIrbInstance> readListByNodeInfoId(SessionWrapper session, Long nodeInfoId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeInfoId" }, new Object[] { session, nodeInfoId });
      Criteria criteria = session.getSession().createCriteria(FcIrbInstance.class)
          .add(Restrictions.eq("nodeInfoId", nodeInfoId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<FcIrbInstance> readListByVlanId(SessionWrapper session, Integer vlanId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "vlanId" }, new Object[] { session, vlanId });
      Criteria criteria = session.getSession().createCriteria(FcIrbInstance.class)
          .add(Restrictions.eq("vlanId", vlanId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public FcIrbInstance read(SessionWrapper session, Long nodeInfoId, Integer vlanId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeInfoId", "vlanId" },
          new Object[] { session, nodeInfoId, vlanId });
      Criteria criteria = session.getSession().createCriteria(FcIrbInstance.class)
          .add(Restrictions.eq("nodeInfoId", nodeInfoId)).add(Restrictions.eq("vlanId", vlanId));
      return readByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcIrbInstance read(SessionWrapper session, Long irbInstanceId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcIrbInstance.class)
        .add(Restrictions.eq("irbInstanceId", irbInstanceId));
    return readByCriteria(session, criteria);
  }
}
