
package msf.mfc.core.operation.scenario;

import java.util.regex.Matcher;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.data.MfcAsyncRequest;
import msf.mfc.common.data.MfcAsyncRequestsForLower;
import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.scenario.AbstractOperationScenarioBase;
import msf.mfcfc.core.operation.scenario.data.OperationReadResponseBody;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.node.nodes.leafs.data.LeafNodeCreateRequestBody;
import msf.mfcfc.node.nodes.spines.data.SpineNodeCreateRequestBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Abstract class to implement the common process of operation-related
 * processing in system basic function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class MfcAbstractOperationScenarioBase<T extends RestRequestBase>
    extends AbstractOperationScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractOperationScenarioBase.class);

  protected MfcFcRequestUri getUriInCaseOfLeafSpineAdd(MfcAsyncRequest asyncRequest) {
    try {
      logger.methodStart(new String[] { "asyncRequest" }, new Object[] { asyncRequest });

      Matcher leafAddMatcher = MfcFcRequestUri.LEAF_NODE_CREATE.getUriPattern().matcher(asyncRequest.getRequestUri());
      if (leafAddMatcher.matches()) {

        if (asyncRequest.getRequestMethod().equals(MfcFcRequestUri.LEAF_NODE_CREATE.getHttpMethod().getMessage())) {

          logger.debug("request is leaf create.");
          return MfcFcRequestUri.LEAF_NODE_CREATE;
        }
      }

      Matcher spineAddMatcher = MfcFcRequestUri.SPINE_NODE_CREATE.getUriPattern().matcher(asyncRequest.getRequestUri());
      if (spineAddMatcher.matches()) {

        if (asyncRequest.getRequestMethod().equals(MfcFcRequestUri.SPINE_NODE_CREATE.getHttpMethod().getMessage())) {

          logger.debug("request is spine create.");
          return MfcFcRequestUri.SPINE_NODE_CREATE;
        }
      }

      return null;
    } finally {
      logger.methodEnd();
    }
  }

  protected boolean getProvisioningStatusFromRequestBody(String requestBody, boolean isLeafAdd) throws MsfException {
    try {
      logger.methodStart(new String[] { "requestBody", "isLeafAdd" }, new Object[] { requestBody, isLeafAdd });
      if (isLeafAdd) {
        LeafNodeCreateRequestBody leafNodeCreateRequestBody = JsonUtil.fromJson(requestBody,
            LeafNodeCreateRequestBody.class, ErrorCode.UNDEFINED_ERROR);
        return leafNodeCreateRequestBody.getProvisioning();
      } else {
        SpineNodeCreateRequestBody spineNodeCreateRequestBody = JsonUtil.fromJson(requestBody,
            SpineNodeCreateRequestBody.class, ErrorCode.UNDEFINED_ERROR);
        return spineNodeCreateRequestBody.getProvisioning();
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected OperationReadResponseBody getOperationReadResponseFromFc(MfcAsyncRequestsForLower lower)
      throws MsfException {
    try {
      logger.methodStart();
      String ipAddress = MfcConfigManager.getInstance().getSystemConfSwClusterData(lower.getId().getClusterId())
          .getSwCluster().getFcControlAddress();
      int port = MfcConfigManager.getInstance().getSystemConfSwClusterData(lower.getId().getClusterId()).getSwCluster()
          .getFcControlPort();

      RestResponseBase responseBase = RestClient.sendRequest(MfcFcRequestUri.OPERATION_READ.getHttpMethod(),
          MfcFcRequestUri.OPERATION_READ.getUri(lower.getId().getRequestOperationId()), null, ipAddress, port);
      OperationReadResponseBody responseBody = JsonUtil.fromJson(responseBase.getResponseBody(),
          OperationReadResponseBody.class, ErrorCode.FC_CONTROL_ERROR);
      return responseBody;
    } finally {
      logger.methodEnd();
    }
  }

  protected String getSubStatus(MfcAsyncRequest asyncRequest) throws MsfException {
    try {
      logger.methodStart();
      String subStatus = null;

      MfcFcRequestUri requestUri = getUriInCaseOfLeafSpineAdd(asyncRequest);
      if (requestUri != null) {

        if (asyncRequest.getStatusEnum().equals(AsyncProcessStatus.RUNNING)
            && asyncRequest.getAsyncRequestsForLowers() != null
            && asyncRequest.getAsyncRequestsForLowers().size() != 0) {

          MfcAsyncRequestsForLower asyncRequestLower = asyncRequest.getAsyncRequestsForLowers().get(0);

          boolean provisioning = getProvisioningStatusFromRequestBody(asyncRequest.getRequestBody(),
              requestUri.equals(MfcFcRequestUri.LEAF_NODE_CREATE));

          if (provisioning) {
            OperationReadResponseBody operationReadResponseBody = getOperationReadResponseFromFc(asyncRequestLower);
            subStatus = operationReadResponseBody.getSubStatus();
          }
        }
      }
      return subStatus;
    } finally {
      logger.methodEnd();
    }
  }
}
