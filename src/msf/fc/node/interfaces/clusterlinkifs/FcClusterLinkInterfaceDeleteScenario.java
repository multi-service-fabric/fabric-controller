
package msf.fc.node.interfaces.clusterlinkifs;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfDeleteResponseBody;
import msf.mfcfc.node.interfaces.clusterlinkifs.data.ClusterLinkIfRequest;

/**
 * Implementation class for inter-cluster link interface deletion.
 *
 * @author NTT
 *
 */
public class FcClusterLinkInterfaceDeleteScenario
    extends FcAbstractClusterLinkInterfaceScenarioBase<ClusterLinkIfRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(FcClusterLinkInterfaceDeleteScenario.class);

  private ClusterLinkIfRequest request;

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
  public FcClusterLinkInterfaceDeleteScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;
  }

  @Override
  protected void checkParameter(ClusterLinkIfRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNumericId(request.getClusterLinkIfId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);
      ParameterCheckUtil.checkIpv4Address(request.getNotificationAddress());
      ParameterCheckUtil.checkPortNumber(request.getNotificationPort());

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

      FcClusterLinkInterfaceDeleteRunner clusterLinkInterfaceDeleteRunner = new FcClusterLinkInterfaceDeleteRunner(
          request);
      execAsyncRunner(clusterLinkInterfaceDeleteRunner);

      responseBase = responseClusterLinkInterfaceDeleteData();
      return responseBase;
    } finally {
      logger.methodEnd();
    }

  }

  private RestResponseBase responseClusterLinkInterfaceDeleteData() {
    try {
      logger.methodStart();
      ClusterLinkIfDeleteResponseBody body = new ClusterLinkIfDeleteResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }

}
