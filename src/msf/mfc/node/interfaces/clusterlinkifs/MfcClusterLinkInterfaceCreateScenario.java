
package msf.mfc.node.interfaces.clusterlinkifs;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfCreateRequestBody;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfCreateResponseBody;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfRequest;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for inter-cluster link interface registration.
 *
 * @author NTT
 *
 */
public class MfcClusterLinkInterfaceCreateScenario
    extends MfcAbstractClusterLinkInterfaceScenarioBase<ClusterLinkIfRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterLinkInterfaceCreateScenario.class);

  private ClusterLinkIfRequest request;
  private ClusterLinkIfCreateRequestBody requestBody;

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
  public MfcClusterLinkInterfaceCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
    this.lowerSystemSyncType = SynchronousType.ASYNC;
  }

  @Override
  protected void checkParameter(ClusterLinkIfRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkIpv4Address(request.getNotificationAddress());
      ParameterCheckUtil.checkPortNumber(request.getNotificationPort());

      ClusterLinkIfCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          ClusterLinkIfCreateRequestBody.class);

      requestBody.validate();

      if (request.getClusterId().equals(requestBody.getOppositeClusterId())) {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR,
            "\"cluster_id\" and \"opposite_cluster_id\" is same value.");
      }

      logger.debug("requestBody=" + request.getRequestBody());

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

      MfcClusterLinkInterfaceCreateRunner clusterLinkInterfaceCreateRunner = new MfcClusterLinkInterfaceCreateRunner(
          request, requestBody);
      execAsyncRunner(clusterLinkInterfaceCreateRunner);

      responseBase = responseClusterLinkInterfaceCreateData();
      return responseBase;
    } finally {
      logger.methodEnd();
    }

  }

  private RestResponseBase responseClusterLinkInterfaceCreateData() {
    try {
      logger.methodStart();
      ClusterLinkIfCreateResponseBody body = new ClusterLinkIfCreateResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }

}
