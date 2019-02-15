
package msf.fc.node.nodes.leafs;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.db.dao.clusters.FcNodeOperationInfoDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.LeafNodeUpdateAction;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeRequest;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeUpdateRequestBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeUpdateResponseBody;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for the Leaf node modification.
 *
 * @author NTT
 *
 */
public class FcLeafNodeUpdateScenario extends FcAbstractLeafNodeScenarioBase<LeafNodeRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLeafNodeUpdateScenario.class);

  private LeafNodeRequest request;
  private LeafNodeUpdateRequestBody requestBody;

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
  public FcLeafNodeUpdateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected void checkParameter(LeafNodeRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);

      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

      LeafNodeUpdateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          LeafNodeUpdateRequestBody.class);
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

      if (LeafNodeUpdateAction.RECOVER_NODE.equals(requestBody.getActionEnum())) {

        boolean hasChangeNodeOperationStatus = FcNodeOperationInfoDao
            .hasChangeNodeOperationStatus(NodeOperationStatus.RUNNING.getCode());
        if (!hasChangeNodeOperationStatus) {

          throw new MsfException(ErrorCode.UPDATE_INFORMATION_ERROR,
              "Another node related operation is currently in progress.");
        }
      }

      RestResponseBase responseBase = null;

      FcLeafNodeUpdateRunner fcLeafNodeUpdateRunner = new FcLeafNodeUpdateRunner(request, requestBody);
      execAsyncRunner(fcLeafNodeUpdateRunner);

      responseBase = responseLeafNodeUpdateData();

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLeafNodeUpdateData() {
    try {
      logger.methodStart();
      LeafNodeUpdateResponseBody body = new LeafNodeUpdateResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }
}
