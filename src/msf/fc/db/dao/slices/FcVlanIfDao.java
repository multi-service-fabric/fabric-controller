
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
          .add(Restrictions.eq("id.nodeInfoId", nodeInfoId));
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

  @Override
  public void create(SessionWrapper session, FcVlanIf entity) throws MsfException {

    if (entity.getIrbInstance() != null) {
      FcIrbInstanceDao irbInstanceDao = new FcIrbInstanceDao();
      irbInstanceDao.create(session, entity.getIrbInstance());
    }
    super.create(session, entity);
  }

  @Override
  public void delete(SessionWrapper session, FcVlanIfPK pk) throws MsfException {
    FcVlanIfDao vlanIfDao = new FcVlanIfDao();
    FcVlanIf vlanIf = vlanIfDao.read(session, pk);
    super.delete(session, pk);

    if (vlanIf.getIrbInstance() != null && vlanIf.getIrbInstance().getVlanIfs().size() == 1) {
      FcIrbInstanceDao irbInstanceDao = new FcIrbInstanceDao();
      irbInstanceDao.delete(session, vlanIf.getIrbInstance().getIrbInstanceId());
    }

  }
}
