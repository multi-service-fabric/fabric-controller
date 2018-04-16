
package msf.mfc.node.interfaces.edgepoints;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestUserTypeOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.edgepoints.data.EdgePointReadOwnerResponseBody;
import msf.mfcfc.node.interfaces.edgepoints.data.EdgePointReadUserResponseBody;
import msf.mfcfc.node.interfaces.edgepoints.data.EdgePointRequest;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for Edge-Point interface information acquisition.
 *
 * @author NTT
 *
 */
public class MfcEdgePointReadScenario extends MfcAbstractEdgePointScenarioBase<EdgePointRequest> {

  private EdgePointRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(MfcEdgePointReadScenario.class);

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
  public MfcEdgePointReadScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(EdgePointRequest request) throws MsfException {
    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      ParameterCheckUtil.checkNumericId(request.getEdgePointId(), ErrorCode.TARGET_RESOURCE_NOT_FOUND);

      if (request.getUserType() != null) {

        ParameterCheckUtil.checkNotNull(request.getUserTypeEnum());

      }

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

        MfcSwClusterDao mfcSwClusterDao = new MfcSwClusterDao();

        getSwCluster(sessionWrapper, mfcSwClusterDao, Integer.valueOf(request.getClusterId()));

        responseBase = sendEdgePointRead(request.getClusterId(),
            RestUserTypeOption.OPERATOR.equals(request.getUserTypeEnum()));

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

  private RestResponseBase sendEdgePointRead(String swClusterId, boolean isOwner) throws MsfException {
    try {
      logger.methodStart(new String[] { "swClusterId" }, new Object[] { swClusterId });

      SwCluster swCluster = MfcConfigManager.getInstance().getSystemConfSwClusterData(Integer.valueOf(swClusterId))
          .getSwCluster();
      String fcControlAddress = swCluster.getFcControlAddress();
      int fcControlPort = swCluster.getFcControlPort();

      String targetUri = MfcFcRequestUri.EDGE_POINT_READ.getUri(swClusterId, request.getEdgePointId());
      if (isOwner) {
        targetUri = targetUri + "?user-type=" + RestUserTypeOption.OPERATOR.getMessage();
      }
      RestResponseBase restResponseBase = RestClient.sendRequest(MfcFcRequestUri.EDGE_POINT_READ.getHttpMethod(),
          targetUri, null, fcControlAddress, fcControlPort);

      String errorCode = null;
      if (isOwner) {
        EdgePointReadOwnerResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            EdgePointReadOwnerResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
        errorCode = responseBody.getErrorCode();
      } else {
        EdgePointReadUserResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            EdgePointReadUserResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
        errorCode = responseBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, errorCode,
          ErrorCode.FC_CONTROL_ERROR);

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }
}
