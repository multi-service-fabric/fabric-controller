
package msf.fc.db.dao.slices;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcL2Cp;
import msf.fc.common.data.FcL2CpPK;
import msf.fc.common.data.FcVlanIfPK;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;


public class FcL2CpDao extends FcAbstractCommonDao<FcL2Cp, FcL2CpPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL2CpDao.class);


  
  public List<FcL2Cp> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(FcL2Cp.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  
  public List<FcL2Cp> readListByNodeInfo(SessionWrapper session, String sliceId, Long nodeInfoId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId", "nodeInfoId" },
          new Object[] { session, sliceId, nodeInfoId });
      Criteria criteria = session.getSession().createCriteria(FcL2Cp.class).add(Restrictions.eq("id.sliceId", sliceId))
          .add(Restrictions.eq("vlanIf.id.nodeInfoId", nodeInfoId.intValue()));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  
  public List<FcL2Cp> readListByEdgePoint(SessionWrapper session, String sliceId, int edgePointId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId", "edgePointId" },
          new Object[] { session, sliceId, edgePointId });
      Criteria criteria = session.getSession().createCriteria(FcL2Cp.class).add(Restrictions.eq("id.sliceId", sliceId))
          .add(Restrictions.eq("edgePoint.edgePointId", edgePointId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  
  public List<FcL2Cp> readListByEsi(SessionWrapper session, String esi) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "esi" }, new Object[] { session, esi });
      Criteria criteria = session.getSession().createCriteria(FcL2Cp.class).add(Restrictions.eq("esi", esi));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  
  public List<FcL2Cp> readListBySliceId(SessionWrapper session, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId" }, new Object[] { session, sliceId });
      Criteria criteria = session.getSession().createCriteria(FcL2Cp.class).add(Restrictions.eq("id.sliceId", sliceId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcL2Cp read(SessionWrapper session, FcL2CpPK pk) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcL2Cp.class)
        .add(Restrictions.eq("id.sliceId", pk.getSliceId())).add(Restrictions.eq("id.cpId", pk.getCpId()));
    return readByCriteria(session, criteria);
  }

  @Override
  public void create(SessionWrapper session, FcL2Cp entity) throws MsfException {
    FcVlanIfDao vlanIfDao = new FcVlanIfDao();
    vlanIfDao.create(session, entity.getVlanIf());
    super.create(session, entity);
  }

  @Override
  public void delete(SessionWrapper session, FcL2CpPK primaryKey) throws MsfException {
    FcVlanIfDao vlanIfDao = new FcVlanIfDao();
    FcL2Cp l2Cp = read(session, primaryKey);
    FcVlanIfPK id = l2Cp.getVlanIf().getId();
    super.delete(session, primaryKey);


    vlanIfDao.delete(session, id);
  }
}