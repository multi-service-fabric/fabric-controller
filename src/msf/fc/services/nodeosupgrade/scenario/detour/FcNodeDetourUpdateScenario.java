
package msf.fc.services.nodeosupgrade.scenario.detour;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.db.dao.clusters.FcNodeOperationInfoDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SpecialOperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.services.nodeosupgrade.scenario.detour.data.NodeDetourRequest;
import msf.mfcfc.services.nodeosupgrade.scenario.detour.data.NodeDetourUpdateRequestBody;
import msf.mfcfc.services.nodeosupgrade.scenario.detour.data.NodeDetourUpdateResponseBody;

/**
 * Implementation class for the node detour.
 *
 * @author NTT
 *
 */
public class FcNodeDetourUpdateScenario extends FcAbstractNodeDetourScenarioBase<NodeDetourRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcNodeDetourUpdateScenario.class);

  private NodeDetourRequest request;
  private NodeDetourUpdateRequestBody requestBody;

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
  public FcNodeDetourUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
    this.specialOperationType = SpecialOperationType.SPECIALOPERATION;

  }

  @Override
  protected void checkParameter(NodeDetourRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      if (!NodeType.LEAF.equals(NodeType.getEnumFromPluralMessage(request.getFabricType()))
          && !NodeType.SPINE.equals(NodeType.getEnumFromPluralMessage(request.getFabricType()))) {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "fabricType = " + request.getFabricType());
      }
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

      NodeDetourUpdateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          NodeDetourUpdateRequestBody.class);
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

      boolean hasChangeNodeOperationStatus = FcNodeOperationInfoDao
          .hasChangeNodeOperationStatus(NodeOperationStatus.RUNNING.getCode());
      if (!hasChangeNodeOperationStatus) {

        throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR,
            "Another node related operation is currently in progress.");
      }

      RestResponseBase responseBase = null;

      FcNodeDetourUpdateRunner updateRunner = new FcNodeDetourUpdateRunner(request, requestBody);
      execAsyncRunner(updateRunner);

      responseBase = responseNodeDetourUpdateData();
      return responseBase;
    } finally {
      logger.methodEnd();
    }

  }

  private RestResponseBase responseNodeDetourUpdateData() {
    try {
      logger.methodStart();
      NodeDetourUpdateResponseBody body = new NodeDetourUpdateResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }

}
