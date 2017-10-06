package msf.fc.node.interfaces.lagifs;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.constant.OperationType;
import msf.fc.common.constant.SynchronousType;
import msf.fc.common.constant.SystemInterfaceType;
import msf.fc.common.exception.MsfException;
import msf.fc.common.log.MsfLogger;
import msf.fc.core.scenario.RestResponseBase;
import msf.fc.node.interfaces.lagifs.data.LagIfCreateRequestBody;
import msf.fc.node.interfaces.lagifs.data.LagIfCreateResponseBody;
import msf.fc.node.interfaces.lagifs.data.LagIfRequest;
import msf.fc.rest.common.JsonUtil;

public class LagInterfaceCreateScenario extends AbstractLagInterfaceScenarioBase<LagIfRequest> {

  private LagIfRequest request;
  private LagIfCreateRequestBody requestBody;

  private static final MsfLogger logger = MsfLogger.getInstance(LagInterfaceCreateScenario.class);

  public LagInterfaceCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;
    this.syncType = SynchronousType.ASYNC;

  }

  @Override
  protected void checkParameter(LagIfRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      checkFabricTypeLeaf(request.getFabricType());

      checkNodeId(request.getNodeId());

      LagIfCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(), LagIfCreateRequestBody.class);
      requestBody.validate();
      logger.debug("requestBody=" + request.getRequestBody());

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
      LagInterfaceCreateRunner lagInterfaceCreateRunner = new LagInterfaceCreateRunner(request, requestBody);
      execAsyncRunner(lagInterfaceCreateRunner);

      responseBase = responseLagInterfaceCreateData();
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLagInterfaceCreateData() {
    try {
      logger.methodStart();
      LagIfCreateResponseBody body = new LagIfCreateResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }

}
