package msf.fc.node.interfaces.edgepoints;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.ErrorCode;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.EdgePoint;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.common.util.ParameterCheckUtil;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.DbManager;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.EdgePointDao;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.node.interfaces.edgepoints.data.EdgePointRequest;

public class EdgePointDeleteScenario extends AbstractEdgePointScenarioBase<EdgePointRequest> {

  private EdgePointRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(EdgePointDeleteScenario.class);

  public EdgePointDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(EdgePointRequest request) throws MsfException {
    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getEdgePointId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);

      this.request = request;

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
        Node node = getNodeByEdgePoint(sessionWrapper, nodeDao, request.getClusterId(),
            Integer.parseInt(request.getEdgePointId()));

        logger.performance("start get leaf resources lock.");
        sessionWrapper.beginTransaction();
        List<Node> nodes = new ArrayList<>();
        nodes.add(node);
        DbManager.getInstance().getLeafsLock(nodes, sessionWrapper);
        logger.performance("end get leaf resources lock.");

        EdgePointDao edgePointDao = new EdgePointDao();
        EdgePoint edgePoint = checkEdgePoint(sessionWrapper, edgePointDao, request.getClusterId(),
            Integer.parseInt(request.getEdgePointId()));

        edgePointDao.delete(sessionWrapper, edgePoint.getId());

        responseBase = responsEdgePointDeleteData();

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

  private Node getNodeByEdgePoint(SessionWrapper sessionWrapper, NodeDao nodeDao, String swClusterId,
      Integer edgePointId) throws MsfException {
    try {
      logger.methodStart();
      Node node = nodeDao.read(sessionWrapper, swClusterId, edgePointId);

      if (node == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND, "target resource not found. parameters = node");
      }
      return node;
    } finally {
      logger.methodEnd();
    }
  }

  private EdgePoint checkEdgePoint(SessionWrapper sessionWrapper, EdgePointDao edgePointDao, String swClusterId,
      Integer edgePointId) throws MsfException {
    try {
      logger.methodStart();
      EdgePoint edgePoint = getEdgePoint(sessionWrapper, edgePointDao, swClusterId, edgePointId);
      if (edgePoint == null) {
        throw new MsfException(ErrorCode.TARGET_RESOURCE_NOT_FOUND,
            "target resource not found. parameters = edgePoint");
      } else if ((!edgePoint.getL2Cps().isEmpty()) || (!edgePoint.getL3Cps().isEmpty())) {
        throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, "cps exist related to the edgepoint.");
      }
      return edgePoint;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responsEdgePointDeleteData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.NO_CONTENT_204, (String) null);
    } finally {
      logger.methodEnd();
    }
  }

}
