
package msf.fc.node.nodes.spines;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.nodes.spines.data.SpineNodeRequest;
import msf.mfcfc.node.nodes.spines.data.SpineNodeUpdateRequestBody;
import msf.mfcfc.node.nodes.spines.data.SpineNodeUpdateResponseBody;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for the Spine node modification.
 *
 * @author NTT
 *
 */
public class FcSpineNodeUpdateScenario extends FcAbstractSpineNodeScenarioBase<SpineNodeRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcSpineNodeUpdateScenario.class);

  private SpineNodeRequest request;
  private SpineNodeUpdateRequestBody requestBody;

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
  public FcSpineNodeUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected void checkParameter(SpineNodeRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);

      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

      SpineNodeUpdateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          SpineNodeUpdateRequestBody.class);
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

      FcSpineNodeUpdateRunner fcSpineNodeUpdateRunner = new FcSpineNodeUpdateRunner(request, requestBody);
      execAsyncRunner(fcSpineNodeUpdateRunner);

      responseBase = responseSpineNodeUpdateData();

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseSpineNodeUpdateData() {
    try {
      logger.methodStart();
      SpineNodeUpdateResponseBody body = new SpineNodeUpdateResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }
}
