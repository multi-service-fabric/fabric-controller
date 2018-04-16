
package msf.mfc.node.clusters;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.common.config.type.system.SwClusterData;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.ControllerStatus;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.status.scenario.data.SystemStatusReadResponseBody;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.clusters.data.SwClusterCreateRequestBody;
import msf.mfcfc.node.clusters.data.SwClusterCreateResponseBody;
import msf.mfcfc.node.clusters.data.SwClusterRequest;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for the SW cluster addition process.
 *
 * @author NTT
 *
 */
public class MfcClusterCreateScenario extends MfcAbstractClusterScenarioBase<SwClusterRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterCreateScenario.class);

  private SwClusterRequest request;
  private SwClusterCreateRequestBody requestBody;

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
  public MfcClusterCreateScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.ASYNC;

  }

  @Override
  protected void checkParameter(SwClusterRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkIpv4Address(request.getNotificationAddress());
      ParameterCheckUtil.checkPortNumber(request.getNotificationPort());

      SwClusterCreateRequestBody requestBody = JsonUtil.fromJson(request.getRequestBody(),
          SwClusterCreateRequestBody.class);

      requestBody.validate();

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
      SessionWrapper sessionWrapper = new SessionWrapper();
      try {
        sessionWrapper.openSession();

        checkForExecClusterInfo(sessionWrapper, false);

        SwCluster swCluster = checkForConfigInfo();

        checkForClusterId(sessionWrapper);

        checkForControllerStatus(sessionWrapper, swCluster);

        MfcClusterCreateRunner mfcClusterCreateRunner = new MfcClusterCreateRunner(request, requestBody);
        execAsyncRunner(mfcClusterCreateRunner);

        responseBase = responseSwClusterCreateData();
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

  private SwCluster checkForConfigInfo() throws MsfException {
    try {
      logger.methodStart();
      MfcConfigManager.getInstance().reloadConfig();
      SwClusterData systemConfSwClusterData = MfcConfigManager.getInstance()
          .getSystemConfSwClusterData(Integer.valueOf(requestBody.getCluster().getClusterId()));
      if (systemConfSwClusterData == null) {

        throw new MsfException(ErrorCode.RELATED_RESOURCE_NOT_FOUND,
            "related resource not found. parameters = config info");
      }
      return systemConfSwClusterData.getSwCluster();
    } finally {
      logger.methodEnd();
    }
  }

  private void checkForClusterId(SessionWrapper sessionWrapper) throws MsfException {
    try {
      logger.methodStart();
      MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();
      MfcSwCluster mfcSwCluster = mfcSwClusterDao.read(sessionWrapper,
          Integer.valueOf(requestBody.getCluster().getClusterId()));
      if (mfcSwCluster != null) {

        throw new MsfException(ErrorCode.TARGET_RESOURCE_ALREADY_EXIST,
            "target resouece already exist. target = cluster");
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void checkForControllerStatus(SessionWrapper sessionWrapper, SwCluster swCluster) throws MsfException {
    try {
      logger.methodStart(new String[] { "swCluster" }, new Object[] { swCluster });
      SystemStatusReadResponseBody statusReadResponseBody = sendControllerStatus(swCluster);
      if (!ControllerStatus.RUNNING
          .equals(ControllerStatus.getEnumFromMessage(statusReadResponseBody.getServiceStatus()))) {

        throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "controller status check error.");
      }
    } finally {
      logger.methodEnd();
    }

  }

  private SystemStatusReadResponseBody sendControllerStatus(SwCluster swCluster) throws MsfException {
    try {
      logger.methodStart(new String[] { "swCluster" }, new Object[] { swCluster });
      SystemStatusReadResponseBody statusReadResponseBody = new SystemStatusReadResponseBody();

      String fcControlAddress = swCluster.getFcControlAddress();
      int fcControlPort = swCluster.getFcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(MfcFcRequestUri.STATUS_READ.getHttpMethod(),
          MfcFcRequestUri.STATUS_READ.getUri(), null, fcControlAddress, fcControlPort);

      statusReadResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(), SystemStatusReadResponseBody.class,
          ErrorCode.FC_CONTROL_ERROR);

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200,
          statusReadResponseBody.getErrorCode(), ErrorCode.FC_CONTROL_ERROR);

      return statusReadResponseBody;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseSwClusterCreateData() {
    try {
      logger.methodStart();
      SwClusterCreateResponseBody body = new SwClusterCreateResponseBody();
      body.setOperationId(getOperationId());
      return createRestResponse(body, HttpStatus.ACCEPTED_202);
    } finally {
      logger.methodEnd();
    }
  }

}
