package msf.fc.node.nodes.spines;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.NodeType;
import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.data.Node;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.db.SessionWrapper;
import msf.fc.db.dao.clusters.NodeDao;
import msf.fc.node.nodes.spines.data.SpineNodeReadResponseBody;
import msf.fc.node.nodes.spines.data.SpineNodeRequest;

public class SpineNodeReadScenario extends AbstractSpineNodeScenarioBase<SpineNodeRequest> {

  private SpineNodeRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(SpineNodeReadScenario.class);

  public SpineNodeReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.SYNC;

  }

  @Override
  protected void checkParameter(SpineNodeRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      checkNodeId(request.getNodeId());

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
        Node node = getNode(sessionWrapper, nodeDao, request.getClusterId(), NodeType.SPINE.getCode(),
            Integer.parseInt(request.getNodeId()));

        responseBase = responseSpineNodeReadData(node);

      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseSpineNodeReadData(Node node) throws MsfException {
    try {
      logger.methodStart(new String[] { "node" }, new Object[] { node });
      SpineNodeReadResponseBody body = new SpineNodeReadResponseBody();
      body.setSpine(getSpineEntity(node));
      return createRestResponse(body, HttpStatus.OK_200);
    } finally {
      logger.methodEnd();
    }
  }

}
