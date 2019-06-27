
package msf.fc.services.priorityroutes.scenario.internalifs;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.db.dao.clusters.FcNodeOperationInfoDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.services.priorityroutes.scenario.internalifs.data.InternalLinkIfPriorityRequest;
import msf.mfcfc.services.priorityroutes.scenario.internalifs.data.InternalLinkIfPriorityUpdateRequestBody;
import msf.mfcfc.services.priorityroutes.scenario.internalifs.data.InternalLinkIfPriorityUpdateResponseBody;

/**
 * Implementation class for the internal-link interface priority modification.
 *
 * @author NTT
 *
 */
public class FcInternalLinkIfPriorityUpdateScenario
    extends FcAbstractInternalLinkIfPriorityScenarioBase<InternalLinkIfPriorityRequest> {

  private InternalLinkIfPriorityRequest request;
  private InternalLinkIfPriorityUpdateRequestBody requestBody;

  private static final MsfLogger logger = MsfLogger.getInstance(FcInternalLinkIfPriorityUpdateScenario.class);

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
  public FcInternalLinkIfPriorityUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {

    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;

  }

  @Override
  protected void checkParameter(InternalLinkIfPriorityRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });
      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNotNull(NodeType.getEnumFromPluralMessage(request.getFabricType()));
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkNumericId(request.getInternalLinkIfId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

      InternalLinkIfPriorityUpdateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          InternalLinkIfPriorityUpdateRequestBody.class);
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

      FcInternalLinkIfPriorityUpdateRunner createDeleteRunner = new FcInternalLinkIfPriorityUpdateRunner(request,
          requestBody);
      execAsyncRunner(createDeleteRunner);

      responseBase = responseInternalLinkIfPriorityUpdateData();
      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseInternalLinkIfPriorityUpdateData() {
    try {
      logger.methodStart();
      InternalLinkIfPriorityUpdateResponseBody body = new InternalLinkIfPriorityUpdateResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }
}
