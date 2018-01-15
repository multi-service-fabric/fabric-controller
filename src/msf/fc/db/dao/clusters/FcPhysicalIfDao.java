package msf.fc.db.dao.clusters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import msf.fc.common.data.FcNode;
import msf.fc.common.data.FcPhysicalIf;
import msf.fc.db.dao.FcAbstractCommonDao;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.db.SessionWrapper;


public class FcPhysicalIfDao extends FcAbstractCommonDao<FcPhysicalIf, Long> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcPhysicalIfDao.class);


  
  public List<FcPhysicalIf> readList(SessionWrapper session, Integer nodeType, Integer nodeId) throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeType", "nodeId" }, new Object[] { session, nodeType, nodeId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.read(session, nodeType, nodeId);

      if (Objects.isNull(fcNode)) {
        return new ArrayList<>();
      }

      return fcNode.getPhysicalIfs();
    } finally {
      logger.methodEnd();
    }
  }

  
  public FcPhysicalIf read(SessionWrapper session, Integer nodeType, Integer nodeId, String physicalIfId)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "session", "nodeType", "nodeId", "physicalIfId" },
          new Object[] { session, nodeType, nodeId, physicalIfId });

      FcNodeDao fcNodeDao = new FcNodeDao();
      FcNode fcNode = fcNodeDao.read(session, nodeType, nodeId);

      if (Objects.isNull(fcNode)) {
        return null;
      }

      Criteria criteria = session.getSession().createCriteria(FcPhysicalIf.class)
          .add(Restrictions.eq("node.nodeInfoId", fcNode.getNodeInfoId()))
          .add(Restrictions.eq("physicalIfId", physicalIfId));
      return readByCriteria(session, criteria);

    } finally {
      logger.methodEnd();
    }
  }

  @Override
  public FcPhysicalIf read(SessionWrapper session, Long physicalIfInfoId) throws MsfException {
    Criteria criteria = session.getSession().createCriteria(FcPhysicalIf.class)
        .add(Restrictions.eq("physicalIfInfoId", physicalIfInfoId));
    return readByCriteria(session, criteria);
  }

}
