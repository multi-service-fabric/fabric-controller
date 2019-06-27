
package msf.fc.node.interfaces.ifmaintenance;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.InterfaceType;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.interfaces.ifmaintenance.data.InterfaceChangeStateRequest;
import msf.mfcfc.node.interfaces.ifmaintenance.data.InterfaceChangeStateRequestBody;
import msf.mfcfc.node.interfaces.ifmaintenance.data.InterfaceChangeStateResponseBody;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for the interface blockade status modification.
 *
 * @author NTT
 *
 */
public class FcInterfaceMaintenanceUpdateScenario
    extends FcAbstractInterfaceMaintenanceScenarioBase<InterfaceChangeStateRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcInterfaceMaintenanceUpdateScenario.class);

  private InterfaceChangeStateRequest request;
  private InterfaceChangeStateRequestBody requestBody;

  /**
   * Constructor
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
  public FcInterfaceMaintenanceUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  public void checkParameter(InterfaceChangeStateRequest request) throws MsfException {
    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNotNullAndLength(request.getClusterId());
      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      if (!NodeType.LEAF.equals(request.getFabricTypeEnum()) && !NodeType.SPINE.equals(request.getFabricTypeEnum())) {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "fabricType = " + request.getFabricType());
      }
      ParameterCheckUtil.checkNotNullAndLength(request.getNodeId());
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      if (!InterfaceType.PHYSICAL_IF.equals(request.getIfTypeEnum())
          && !InterfaceType.BREAKOUT_IF.equals(request.getIfTypeEnum())
          && !InterfaceType.LAG_IF.equals(request.getIfTypeEnum())) {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "InterfaceType=" + request.getIfType());
      }
      ParameterCheckUtil.checkIdSpecifiedByUri(request.getIfId());

      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

      InterfaceChangeStateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          InterfaceChangeStateRequestBody.class);
      ParameterCheckUtil.checkNotNull(requestBody);
      requestBody.validate();

      logger.debug("requestBody" + request.getRequestBody());

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

      FcInterfaceMaintenanceUpdateRunnner updateRunnner = new FcInterfaceMaintenanceUpdateRunnner(request, requestBody);
      execAsyncRunner(updateRunnner);
      responseBase = responseIIfBlockageStatusUpdateData();
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseIIfBlockageStatusUpdateData() {
    try {
      logger.methodStart();
      InterfaceChangeStateResponseBody body = new InterfaceChangeStateResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }
}
