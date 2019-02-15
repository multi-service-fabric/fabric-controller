
package msf.fc.node.nodes.leafs;

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
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeDeleteResponseBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeRequest;

/**
 * Implementation class for the Leaf node deletion.
 *
 * @author NTT
 *
 */
public class FcLeafNodeDeleteScenario extends FcAbstractLeafNodeScenarioBase<LeafNodeRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLeafNodeDeleteScenario.class);

  private LeafNodeRequest request;

  /**
   * Constructor.
   *
   * <p>
   * Set the "operation type" and "system interface type" as arguments
   * </p>
   *
   * @param operationType
   *          Operation type
   *
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public FcLeafNodeDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
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
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();

        boolean isCreateNodeCancelled = false;

        boolean hasChangeNodeOperationStatus = FcNodeOperationInfoDao
            .hasChangeNodeOperationStatus(NodeOperationStatus.RUNNING.getCode());
        if (!hasChangeNodeOperationStatus) {

          isCreateNodeCancelled = checkForCancelExecNodeInfo(sessionWrapper, request.getClusterId(), NodeType.LEAF,
              request.getNodeId());
        }

        FcLeafNodeDeleteRunner fcLeafNodeDeleteRunner = new FcLeafNodeDeleteRunner(request, isCreateNodeCancelled);
        execAsyncRunner(fcLeafNodeDeleteRunner);

        responseBase = responseLeafNodeDeleteData();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLeafNodeDeleteData() {
    try {
      logger.methodStart();
      LeafNodeDeleteResponseBody body = new LeafNodeDeleteResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }

}
