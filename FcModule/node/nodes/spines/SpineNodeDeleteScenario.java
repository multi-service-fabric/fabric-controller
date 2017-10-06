package msf.fc.node.nodes.spines;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.node.nodes.spines.data.SpineNodeDeleteResponseBody;
import msf.fc.node.nodes.spines.data.SpineNodeRequest;

public class SpineNodeDeleteScenario extends AbstractSpineNodeScenarioBase<SpineNodeRequest> {

  private SpineNodeRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(SpineNodeDeleteScenario.class);

  public SpineNodeDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.ASYNC;

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
      SpineNodeDeleteRunner spineNodeDeleteRunner = new SpineNodeDeleteRunner(request);
      execAsyncRunner(spineNodeDeleteRunner);

      responseBase = responseSpineNodeDeleteData();
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseSpineNodeDeleteData() {
    try {
      logger.methodStart();
      SpineNodeDeleteResponseBody body = new SpineNodeDeleteResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }

}
