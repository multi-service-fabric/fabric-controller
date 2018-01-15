
package msf.fc.node.nodes;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.AbstractAsyncRunner;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.nodes.data.InternalNodeRequest;
import msf.mfcfc.node.nodes.data.NodeNotifyRequestBody;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class to receive startup completion notification from EC node.
 *
 * @author NTT
 *
 */
public class FcInternalNodeNotifyScenario extends FcAbstractNodeScenarioBase<InternalNodeRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalNodeNotifyScenario.class);

  private InternalNodeRequest request;
  private NodeNotifyRequestBody requestBody;

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
  public FcInternalNodeNotifyScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected void checkParameter(InternalNodeRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotNullAndLength(request.getNodeId());

      NodeNotifyRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(), NodeNotifyRequestBody.class);

      requestBody.validate();

      logger.debug("requestBody=" + request.getRequestBody());

      request.setNotificationAddress(AbstractAsyncRunner.NOT_NOTIFY_IP_ADDRESS);
      request.setNotificationPort(AbstractAsyncRunner.NOT_NOTIFY_PORT_NUMBER);

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

      FcInternalNodeNotifyRunner fcNodeNotifyRunner = new FcInternalNodeNotifyRunner(request, requestBody);
      execAsyncRunner(fcNodeNotifyRunner);

      responseBase = responseNodeNotifyData();

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseNodeNotifyData() {
    try {
      logger.methodStart();
      return new RestResponseBase(HttpStatus.OK_200, (String) null);
    } finally {
      logger.methodEnd();
    }
  }
}
