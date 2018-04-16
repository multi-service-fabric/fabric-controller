
package msf.fc.db.dao.slices;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcL3Cp;
import msf.fc.common.data.FcL3CpPK;
import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcVlanIf;
import msf.fc.common.data.FcVlanIfPK;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.fc.db.dao.clusters.FcNodeDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;

public class FcL3CpDao extends FcAbstractCommonDao<FcL3Cp, FcL3CpPK> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcL3CpDao.class);

  public List<FcL3Cp> readList(SessionWrapper session) throws MsfException {
    try {
      logger.methodStart(new String[] { "session" }, new Object[] { session });
      Criteria criteria = session.getSession().createCriteria(FcL3Cp.class);
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<FcL3Cp> readListBySliceId(SessionWrapper session, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId" }, new Object[] { session, sliceId });
      Criteria criteria = session.getSession().createCriteria(FcL3Cp.class).add(Restrictions.eq("id.sliceId", sliceId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public List<FcL3Cp> readListByEdgePoint(SessionWrapper session, String sliceId, int edgePointId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "sliceId", "edgePointId" },
          new Object[] { session, sliceId, edgePointId });
      Criteria criteria = session.getSession().createCriteria(FcL3Cp.class).add(Restrictions.eq("id.sliceId", sliceId))
          .add(Restrictions.eq("edgePoint.edgePointId", edgePointId));
      return readListByCriteria(session, criteria);
    } finally {
      logger.methodEnd();
    }
  }

  public FcL3Cp read(SessionWrapper session, Integer ecNodeId, String vlanIfId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "ecNodeId", "vlanIfId" },
          new Object[] { session, ecNodeId, vlanIfId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.readByEcNodeId(session, ecNodeId);
      if (fcNode == null) {
        return null;
      }

      FcVlanIfDao fcVlanIfDao = new FcVlanIfDao();
      FcVlanIfPK fcVlanIfPk = new FcVlanIfPK();
      fcVlanIfPk.setNodeInfoId(fcNode.getNodeInfoId().intValue());
      fcVlanIfPk.setVlanIfId(Integer.valueOf(vlanIfId));
      FcVlanIf fcVlanIf = fcVlanIfDao.read(session, fcVlanIfPk);
      if (fcVlanIf == null) {
        return null;
      }
      return fcVlanIf.getL3Cps().isEmpty() ? null : fcVlanIf.getL3Cps().get(0);

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcL3Cp read(SessionWrapper session, FcL3CpPK pk) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcL3Cp.class)
        .add(Restrictions.eq("id.sliceId", pk.getSliceId())).add(Restrictions.eq("id.cpId", pk.getCpId()));
    return readByCriteria(session, criteria);
  }

  @Override
  public void create(SessionWrapper session, FcL3Cp entity) throws MsfException {
    FcVlanIfDao vlanIfDao = new FcVlanIfDao();
    vlanIfDao.create(session, entity.getVlanIf());
    super.create(session, entity);
  }

  @Override
  public void delete(SessionWrapper session, FcL3CpPK primaryKey) throws MsfException {
    FcVlanIfDao vlanIfDao = new FcVlanIfDao();
    FcL3Cp l3Cp = read(session, primaryKey);
    FcVlanIfPK id = l3Cp.getVlanIf().getId();
    super.delete(session, primaryKey);

    vlanIfDao.delete(session, id);
  }

}
