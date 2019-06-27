
package msf.fc.node.nodes.spines;

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
import msf.mfcfc.node.nodes.spines.data.SpineNodeCreateRequestBody;
import msf.mfcfc.node.nodes.spines.data.SpineNodeCreateResponseBody;
import msf.mfcfc.node.nodes.spines.data.SpineNodeRequest;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for the Spine node addition.
 *
 * @author NTT
 *
 */
public class FcSpineNodeCreateScenario extends FcAbstractSpineNodeScenarioBase<SpineNodeRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcSpineNodeCreateScenario.class);

  private SpineNodeRequest request;
  private SpineNodeCreateRequestBody requestBody;

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
  public FcSpineNodeCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected void checkParameter(SpineNodeRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNotificationAddressAndPort(request.getNotificationAddress(),
          request.getNotificationPort());

      SpineNodeCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          SpineNodeCreateRequestBody.class);

      requestBody.validate();

      logger.debug("requestBody=" + request.getRequestBody());

      int maxSpineNum = FcConfigManager.getInstance().getDataConfSwClusterData().getSwCluster().getMaxSpineNum();
      ParameterCheckUtil.checkNumberRange(Integer.parseInt(requestBody.getNodeId()), 1, maxSpineNum);

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

        FcSpineNodeCreateRunner fcLeafNodeCreateRunner = new FcSpineNodeCreateRunner(request, requestBody);
        execAsyncRunner(fcLeafNodeCreateRunner);

        responseBase = responseSpineNodeCreateData();
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

  private RestResponseBase responseSpineNodeCreateData() {
    try {
      logger.methodStart();
      SpineNodeCreateResponseBody body = new SpineNodeCreateResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }
}
