package msf.fc.node.interfaces.edgepoints;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.data.EdgePoint;
import msf.fc.common.data.EdgePointPK;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestRequestBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EdgePointDao;
import msf.fc.db.dao.clusters.LagIfDao;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.db.dao.clusters.PhysicalIfDao;
import msf.fc.node.interfaces.AbstractInterfaceScenarioBase;
import msf.fc.node.interfaces.edgepoints.data.entity.BaseIfEntity;

public abstract class AbstractEdgePointScenarioBase<T extends RestRequestBase>
    extends AbstractInterfaceScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(AbstractEdgePointScenarioBase.class);

  private static final Integer FIRST_ID_NUM = 1;

  protected EdgePoint getEdgePoint(SessionWrapper sessionWrapper, EdgePointDao edgePointDao, String swClusterId,
      Integer edgePointId) throws MsfException {
    try {
      logger.methodStart();
      EdgePointPK edgePointPk = new EdgePointPK();
      edgePointPk.setSwClusterId(swClusterId);
      edgePointPk.setEdgePointId(edgePointId);
      EdgePoint edgePoint = edgePointDao.read(sessionWrapper, edgePointPk);
      if (edgePoint == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = edgePoint");
      }
      return edgePoint;
    } finally {
      logger.methodEnd();
    }
  }

  protected BaseIfEntity getBaseIf(EdgePoint edgePoint, SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart(new String[] { "edgePoint", "sessionWrapper" }, new Object[] { edgePoint, sessionWrapper });
      BaseIfEntity baseIf = new BaseIfEntity();
      NodeDao nodeDao = new NodeDao();
      Node node = nodeDao.read(sessionWrapper, edgePoint.getSwCluster().getSwClusterId(),
          edgePoint.getId().getEdgePointId());
      baseIf.setLeafNodeId(String.valueOf(node.getNodeId()));
      if (null != edgePoint.getLagIfInfoId()) {
        LagIfDao lagIfDao = new LagIfDao();
        LagIf lagIf = lagIfDao.read(sessionWrapper, edgePoint.getLagIfInfoId());
        baseIf.setLagIfId(String.valueOf(lagIf.getLagIfId()));
      } else {
        PhysicalIfDao physicalIfDao = new PhysicalIfDao();
        PhysicalIf physicalIf = physicalIfDao.read(sessionWrapper, edgePoint.getPhysicalIfInfoId());
        baseIf.setPhysicalIfId(physicalIf.getPhysicalIfId());
      }
      return baseIf;
    } finally {
      logger.methodEnd();
    }
  }

  protected Integer getNextEdgePointId(SessionWrapper sessionWrapper, EdgePointDao edgePointDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "edgePointDao" },
          new Object[] { sessionWrapper, edgePointDao });
      EdgePoint biggestIdEdgePoint = edgePointDao.readFromBiggestId(sessionWrapper);
      if (null == biggestIdEdgePoint) {
        return FIRST_ID_NUM;
      } else {
        return biggestIdEdgePoint.getId().getEdgePointId() + 1;
      }
    } finally {
      logger.methodEnd();
    }
  }

}
