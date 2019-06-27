
package msf.fc.node.nodes.leafs;

import org.eclipse.jetty.http.HttpStatus;

import msf.fc.common.config.FcConfigManager;
import msf.fc.db.dao.clusters.FcNodeOperationInfoDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.NodeOperationStatus;
import msf.mfcfc.common.constant.NodeSubStatus;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeCreateRequestBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeCreateResponseBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeRequest;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for the Leaf node addition.
 *
 * @author NTT
 *
 */
public class FcLeafNodeCreateScenario extends FcAbstractLeafNodeScenarioBase<LeafNodeRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcLeafNodeCreateScenario.class);

  private LeafNodeRequest request;
  private LeafNodeCreateRequestBody requestBody;

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
  public FcLeafNodeCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected void checkParameter(LeafNodeRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

      LeafNodeCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          LeafNodeCreateRequestBody.class);

      requestBody.validate();

      logger.debug("requestBody=" + request.getRequestBody());

      int maxLeafNum = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster().getMaxLeafNum();
      ParameterCheckUtil.checkNumberRange(Integer.parseInt(requestBody.getNodeId()), 1, maxLeafNum);

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

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR,
            "Another node related operation is currently in progress.");
      }

      RestResponseBase responseBase = null;
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();

        if (requestBody.getProvisioning()) {

          sessionWrapper.beginTransaction();
          updateNodeSubStatus(sessionWrapper, NodeSubStatus.ZTP_INFEASIBLE, getOperationId());
          sessionWrapper.commit();
        }

        FcLeafNodeCreateRunner fcLeafNodeCreateRunner = new FcLeafNodeCreateRunner(request, requestBody);
        execAsyncRunner(fcLeafNodeCreateRunner);

        responseBase = responseLeafNodeCreateData();
      } catch (MsfException msfException) {
        logger.error(msfException.getMessage(), msfException);
        sessionWrapper.rollback();

        FcNodeOperationInfoDao.hasChangeNodeOperationStatus(NodeOperationStatus.WAITING.getCode());
        throw msfException;
      } finally {
        sessionWrapper.closeSession();
      }

      return responseBase;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseLeafNodeCreateData() {
    try {
      logger.methodStart();
      LeafNodeCreateResponseBody body = new LeafNodeCreateResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }

}
