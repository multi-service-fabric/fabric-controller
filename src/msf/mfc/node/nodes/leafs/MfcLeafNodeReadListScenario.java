
package msf.mfc.node.nodes.leafs;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.RestUserTypeOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeReadListResponseBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeReadOwnerDetailListResponseBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeReadUserDetailListResponseBody;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeRequest;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Implementation class for Leaf node information list acquisition.
 *
 * @author NTT
 *
 */
public class MfcLeafNodeReadListScenario extends MfcAbstractLeafNodeScenarioBase<LeafNodeRequest> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcLeafNodeReadListScenario.class);

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
   * @param systemInterfaceType
   *          System interface type
   *
   */
  public MfcLeafNodeReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(LeafNodeRequest request) throws MsfException {

    try {
      logger.methodStart(new String[] { "request" }, new Object[] { request });

      ParameterCheckUtil.checkNumericId(request.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

      if (request.getFormat() != null) {
        ParameterCheckUtil.checkNotNull(request.getFormatEnum());

      }

      if (request.getUserType() != null) {

        if (!RestFormatOption.DETAIL_LIST.equals(request.getFormatEnum())) {
          throw new MsfException(ErrorCode.PARAMETER_VALUE_ERROR,
              "To set the \"userType\" must be set to \"format\" = detail-list. ");
        }

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

        responseBase = sendLeafNodeReadList(request.getClusterId(),
            RestFormatOption.DETAIL_LIST.equals(request.getFormatEnum()),
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

  private RestResponseBase sendLeafNodeReadList(String swClusterId, boolean isDetailList, boolean isOwner)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "swClusterId" }, new Object[] { swClusterId });

      SwCluster swCluster = MfcConfigManager.getInstance().getSystemConfSwClusterData(Integer.valueOf(swClusterId))
          .getSwCluster();
      String fcControlAddress = swCluster.getFcControlAddress();
      int fcControlPort = swCluster.getFcControlPort();

      String targetUri = MfcFcRequestUri.LEAF_NODE_READ_LIST.getUri(swClusterId);
      if (isDetailList) {
        targetUri = targetUri + "?format=" + RestFormatOption.DETAIL_LIST.getMessage();
        if (isOwner) {
          targetUri = targetUri + "&user-type=" + RestUserTypeOption.OPERATOR.getMessage();
        }
      }

      RestResponseBase restResponseBase = RestClient.sendRequest(MfcFcRequestUri.LEAF_NODE_READ_LIST.getHttpMethod(),
          targetUri, null, fcControlAddress, fcControlPort);

      String errorCode = null;
      if (isDetailList) {
        if (isOwner) {
          LeafNodeReadOwnerDetailListResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
              LeafNodeReadOwnerDetailListResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
          errorCode = responseBody.getErrorCode();
        } else {
          LeafNodeReadUserDetailListResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
              LeafNodeReadUserDetailListResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
          errorCode = responseBody.getErrorCode();
        }
      } else {
        LeafNodeReadListResponseBody responseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            LeafNodeReadListResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
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
