
package msf.fc.node.interfaces.lagifs;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfCreateRequestBody;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfCreateResponseBody;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfRequest;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for the Lag interface addition.
 *
 * @author NTT
 *
 */
public class FcLagInterfaceCreateScenario extends FcAbstractLagInterfaceScenarioBase<LagIfRequest> {

  private LagIfRequest request;
  private LagIfCreateRequestBody requestBody;

  private static final MsfLogger logger = MsfLogger.getInstance(FcLagInterfaceCreateScenario.class);

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public FcLagInterfaceCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;

  }

  @Override
  protected void checkParameter(LagIfRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      if (!NodeType.LEAF.equals(NodeType.getEnumFromPluralMessage(request.getFabricType()))) {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "fabricType = " + request.getFabricType());
      }
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

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

      FcLagInterfaceCreateRunner fcLagInterfaceCreateRunner = new FcLagInterfaceCreateRunner(request, requestBody);
      execAsyncRunner(fcLagInterfaceCreateRunner);

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
