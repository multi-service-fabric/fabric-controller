package msf.fc.node.interfaces.edgepoints;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.EdgePoint;
import msf.fc.common.data.EdgePointPK;
import msf.fc.common.data.LagIf;
import msf.fc.common.data.Node;
import msf.fc.common.data.PhysicalIf;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EdgePointDao;
import msf.fc.db.dao.clusters.LagIfDao;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.db.dao.clusters.PhysicalIfDao;
import msf.fc.node.interfaces.edgepoints.data.EdgePointCreateRequestBody;
import msf.fc.node.interfaces.edgepoints.data.EdgePointCreateResponseBody;
import msf.fc.node.interfaces.edgepoints.data.EdgePointRequest;
import msf.fc.rest.common.JsonUtil;

public class EdgePointCreateScenario extends AbstractEdgePointScenarioBase<EdgePointRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(EdgePointCreateScenario.class);

  private EdgePointRequest request;
  private EdgePointCreateRequestBody requestBody;

  public EdgePointCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(EdgePointRequest request) throws MsfException {
    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      EdgePointCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          EdgePointCreateRequestBody.class);

      requestBody.validate();

      this.request = request;
      this.requestBody = requestBody;

    } finally {
      logger.methodEnd();
    }

  }

  @Override
  protected RestResponseBase executeImpl() throws MsfException {
    try {
      logger.methodStart();

      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();
        NodeDao nodeDao = new NodeDao();
        Node node = getNode(sessionWrapper, nodeDao, request.getClusterId(), NodeType.LEAF.getCode(),
            Integer.parseInt(requestBody.getLeafNodeId()));

        logger.performance("start get leaf resources lock.");
        sessionWrapper.beginTransaction();
        List<Node> nodes = new ArrayList<>();
        nodes.add(node);
        DbManager.getInstance().getLeafsLock(nodes, sessionWrapper);
        logger.performance("end get leaf resources lock.");

        EdgePointDao edgePointDao = new EdgePointDao();
        EdgePoint edgePoint = createEdgePoint(sessionWrapper, edgePointDao);

        responseBase = responseEdgePointCreateData(edgePoint.getId().getEdgePointId());

        sessionWrapper.commit();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private Node getNode(SessionWrapper sessionWrapper, NodeDao nodeDao, String swClusterId, Integer nodeType,
      Integer nodeId) throws MsfException {
    try {
      logger.methodStart();
      Node node = nodeDao.read(sessionWrapper, swClusterId, nodeType, nodeId);
      if (node == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = node");
      }
      return node;
    } finally {
      logger.methodEnd();
    }
  }

  private EdgePoint createEdgePoint(SessionWrapper sessionWrapper, EdgePointDao edgePointDao) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "edgePointDao" },
          new Object[] { sessionWrapper, edgePointDao });
      EdgePoint edgePoint = new EdgePoint();
      EdgePointPK edgePointPk = new EdgePointPK();
      edgePointPk.setSwClusterId(request.getClusterId());
      edgePointPk.setEdgePointId(getNextEdgePointId(sessionWrapper, edgePointDao));
      edgePoint.setId(edgePointPk);
      if (requestBody.getLagIfId() != null) {
        LagIfDao lagIfDao = new LagIfDao();
        LagIf lagIf = getLagInterface(sessionWrapper, lagIfDao, request.getClusterId(), NodeType.LEAF.getCode(),
            Integer.parseInt(requestBody.getLeafNodeId()), Integer.parseInt(requestBody.getLagIfId()), edgePointDao);
        edgePoint.setLagIfInfoId(lagIf.getLagIfInfoId());
      } else {
        PhysicalIfDao physicalIfDao = new PhysicalIfDao();
        PhysicalIf physicalIf = getPhysicalInterface(sessionWrapper, physicalIfDao, request.getClusterId(),
            NodeType.LEAF.getCode(), Integer.parseInt(requestBody.getLeafNodeId()), requestBody.getPhysicalIfId(),
            edgePointDao);
        edgePoint.setPhysicalIfInfoId(physicalIf.getPhysicalIfInfoId());
      }
      edgePointDao.create(sessionWrapper, edgePoint);
      return edgePoint;
    } finally {
      logger.methodEnd();
    }
  }

  private LagIf getLagInterface(SessionWrapper sessionWrapper, LagIfDao lagIfDao, String swClusterId, Integer nodeType,
      Integer nodeId, Integer lagIfId, EdgePointDao edgePointDao) throws MsfException {
    try {
      logger.methodStart();
      LagIf lagIf = lagIfDao.read(sessionWrapper, swClusterId, nodeType, nodeId, lagIfId);
      if (lagIf == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = lagIf");
      }
      if (null != edgePointDao.readBylagIfInfoId(sessionWrapper, lagIf.getLagIfInfoId())) {
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the edge point.");

      } else if (null != lagIf.getInternalLinkIf()) {
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the internal link.");

      } else if ((null != lagIf.getOppositeLagIfId()) || (null != lagIf.getOppositeNode())) {
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the opposite node.");

      }
      return lagIf;
    } finally {
      logger.methodEnd();
    }
  }

  private PhysicalIf getPhysicalInterface(SessionWrapper sessionWrapper, PhysicalIfDao physicalIfDao,
      String swClusterId, Integer nodeType, Integer nodeId, String physicalIfId, EdgePointDao edgePointDao)
      throws MsfException {
    try {
      logger.methodStart();
      PhysicalIf physicalIf = physicalIfDao.read(sessionWrapper, swClusterId, nodeType, nodeId, physicalIfId);
      if (physicalIf == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = physicalIf");
      }
      if (null == physicalIf.getSpeed()) {
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Speed registration is not.");

      } else if (null != physicalIf.getLagConstruction()) {
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the lag if.");

      } else if (null != edgePointDao.readByPhysicalIfInfoId(sessionWrapper, physicalIf.getPhysicalIfInfoId())) {
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the edge point.");

      } else if ((null != physicalIf.getOppositePhysicalIfId()) || (null != physicalIf.getOppositeNode())) {
        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "Registered to the opposite node.");

      }
      return physicalIf;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseEdgePointCreateData(Integer edgePointId) {
    try {
      logger.methodStart(new String[] { "edgePointId" }, new Object[] { edgePointId });
      EdgePointCreateResponseBody body = new EdgePointCreateResponseBody();
      body.setEdgePointId(String.valueOf(edgePointId));
      return createRestResponse(body, HttpStatus.CREATED_201);
    } finally {
      logger.methodEnd();
    }
  }

}
