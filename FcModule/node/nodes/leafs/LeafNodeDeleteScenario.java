package msf.fc.node.nodes.leafs;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.node.nodes.leafs.data.LeafNodeDeleteResponseBody;
import msf.fc.node.nodes.leafs.data.LeafNodeRequest;

public class LeafNodeDeleteScenario extends AbstractLeafNodeScenarioBase<LeafNodeRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(LeafNodeDeleteScenario.class);

  private LeafNodeRequest request;

  public LeafNodeDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.ASYNC;

  }

  @Override
  protected void checkParameter(LeafNodeRequest request) throws MsfException {
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
      LeafNodeDeleteRunner leafNodeDeleteRunner = new LeafNodeDeleteRunner(request);
      execAsyncRunner(leafNodeDeleteRunner);

      responseBase = responseLeafNodeDeleteData();
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLeafNodeDeleteData() {
    try {
      logger.methodStart();
      LeafNodeDeleteResponseBody body = new LeafNodeDeleteResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }

}
