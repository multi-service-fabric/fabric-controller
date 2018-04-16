
package msf.mfc.node.interfaces.breakoutifs;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.NodeType;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfReadDetailListResponseBody;
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfReadListResponseBody;
import msf.mfcfc.node.interfaces.breakoutifs.data.BreakoutIfRequest;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for breakout interface information list acquisition.
 *
 * @author NTT
 *
 */
public class MfcBreakoutInterfaceReadListScenario extends MfcAbstractBreakoutInterfaceScenarioBase<BreakoutIfRequest> {

  private BreakoutIfRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(MfcBreakoutInterfaceReadListScenario.class);

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
  public MfcBreakoutInterfaceReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(BreakoutIfRequest request) throws MsfException {
    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);
      if (!NodeType.LEAF.equals(NodeType.getEnumFromPluralMessage(request.getFabricType()))
          && !NodeType.SPINE.equals(NodeType.getEnumFromPluralMessage(request.getFabricType()))) {
        throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR, "fabricType = " + request.getFabricType());
      }
      ParameterCheckUtil.checkNumericId(request.getNodeId(), ErrorCode.RELATED_RESOURCE_NOT_FOUND);

      if (request.getFormat() != null) {
        ParameterCheckUtil.checkNotNull(request.getFormatEnum());
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

        responseBase = sendBreakoutIfReadList(request.getClusterId(),
            RestFormatOption.DETAIL_LIST.equals(request.getFormatEnum()));

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

  private RestResponseBase sendBreakoutIfReadList(String swClusterId, boolean isDetailList) throws MsfException {
    try {
      logger.methodStart();

      SwCluster swCluster = MfcConfigManager.getInstance().getSystemConfSwClusterData(Integer.valueOf(swClusterId))
          .getSwCluster();
      String fcControlAddress = swCluster.getFcControlAddress();
      int fcControlPort = swCluster.getFcControlPort();

      String targetUri = MfcFcRequestUri.BREAKOUT_IF_READ_LIST.getUri(swClusterId, request.getFabricType(),
          request.getNodeId());
      if (isDetailList) {
        targetUri = targetUri + "?format=" + RestFormatOption.DETAIL_LIST.getMessage();
      }

      RestResponseBase restResponseBase = RestClient.sendRequest(MfcFcRequestUri.BREAKOUT_IF_READ_LIST.getHttpMethod(),
          targetUri, null, fcControlAddress, fcControlPort);

      String errorCode = null;
      if (isDetailList) {
        BreakoutIfReadDetailListResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            BreakoutIfReadDetailListResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
        errorCode = responseBody.getErrorCode();
      } else {
        BreakoutIfReadListResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            BreakoutIfReadListResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
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
