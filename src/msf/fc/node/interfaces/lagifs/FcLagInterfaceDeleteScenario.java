
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
import msf.mfcfc.node.interfaces.lagifs.data.LagIfDeleteResponseBody;
import msf.mfcfc.node.interfaces.lagifs.data.LagIfRequest;

/**
 * Implementation class for the Lag interface deletion.
 *
 * @author NTT
 *
 */
public class FcLagInterfaceDeleteScenario extends FcAbstractLagInterfaceScenarioBase<LagIfRequest> {

  private LagIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(FcLagInterfaceCreateRunner.class);

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
  public FcLagInterfaceDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;

  }

  @Override
  protected void checkParameter(LagIfRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNotNull(NodeType.getEnumFromPluralMessage(request.getFabricType()));
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkNumericId(request.getLagIfId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

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

      FcLagInterfaceDeleteRunner fcLagInterfaceDeleteRunner = new FcLagInterfaceDeleteRunner(request);
      execAsyncRunner(fcLagInterfaceDeleteRunner);

      responseBase = responseLagInterfaceDeleteData();
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLagInterfaceDeleteData() {
    try {
      logger.methodStart();
      LagIfDeleteResponseBody body = new LagIfDeleteResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }

}
