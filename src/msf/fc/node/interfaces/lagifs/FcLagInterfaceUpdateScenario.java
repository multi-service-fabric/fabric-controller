
package msf.fc.node.interfaces.lagifs;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfRequest;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfUpdateRequestBody;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfUpdateResponseBody;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for the Lag interface modification.
 *
 * @author NTT
 *
 */
public class FcLagInterfaceUpdateScenario extends FcAbstractLagInterfaceScenarioBase<LagIfRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLagInterfaceUpdateScenario.class);

  private LagIfRequest request;
  private LagIfUpdateRequestBody requestBody;

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments.
   * </p>
   *
   * @param operationType
   *          Operation type
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public FcLagInterfaceUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected void checkParameter(LagIfRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNotNull(request.getFabricTypeEnum());
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkNumericId(request.getLagIfId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);

      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

      LagIfUpdateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(), LagIfUpdateRequestBody.class);
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

      FcLagInterfaceUpdateRunner fcLagInterfaceUpdateRunner = new FcLagInterfaceUpdateRunner(request, requestBody);
      execAsyncRunner(fcLagInterfaceUpdateRunner);

      responseBase = responseLagInterfaceUpdateData();
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLagInterfaceUpdateData() {
    try {
      logger.methodStart();
      LagIfUpdateResponseBody body = new LagIfUpdateResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }
}
