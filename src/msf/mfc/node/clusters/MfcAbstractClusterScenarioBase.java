
package msf.mfc.node.clusters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.Rrs;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.common.data.MfcAsyncRequest;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.common.MfcAsyncRequestsDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RestUserTypeOption;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.clusters.AbstractClusterScenarioBase;
import msf.mfcfc.node.clusters.data.SwClusterReadOwnerResponseBody;
import msf.mfcfc.node.clusters.data.SwClusterReadUserResponseBody;
import msf.mfcfc.node.clusters.data.SwClusterRequest;
import msf.mfcfc.node.clusters.data.entity.SwClusterRrEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;

/**
 * Abstract class to implement the common process of SW cluster-related
 * processing in configuration management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 *
 */
public abstract class MfcAbstractClusterScenarioBase<T extends RestRequestBase> extends AbstractClusterScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractClusterScenarioBase.class);

  protected RestResponseBase sendSwClusterRead(MfcSwCluster mfcSwCluster, SwClusterRequest request, boolean isOwner)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcSwCluster", "request" }, new Object[] { mfcSwCluster, request });

      SwCluster swCluster = MfcConfigManager.getInstance().getSystemConfSwClusterData(mfcSwCluster.getSwClusterId())
          .getSwCluster();
      String fcControlAddress = swCluster.getFcControlAddress();
      int fcControlPort = swCluster.getFcControlPort();

      String targetUri = MfcFcRequestUri.SW_CLUSTER_READ.getUri(request.getClusterId());
      if (isOwner) {
        targetUri = targetUri + "?user-type=" + RestUserTypeOption.OPERATOR.getMessage();
      }
      RestResponseBase restResponseBase = RestClient.sendRequest(MfcFcRequestUri.SW_CLUSTER_READ.getHttpMethod(),
          targetUri, null, fcControlAddress, fcControlPort);

      String errorCode = null;

      SwClusterReadOwnerResponseBody ownerResponseBody = null;
      if (isOwner) {
        ownerResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(), SwClusterReadOwnerResponseBody.class,
            ErrorCode.FC_CONTROL_ERROR);
        errorCode = ownerResponseBody.getErrorCode();

      } else {
        SwClusterReadUserResponseBody userResponseBody = JsonUtil.fromJson(restResponseBase.getResponseBody(),
            SwClusterReadUserResponseBody.class, ErrorCode.FC_CONTROL_ERROR);

        errorCode = userResponseBody.getErrorCode();
      }

      checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, errorCode,
          ErrorCode.FC_CONTROL_ERROR);

      if (isOwner) {
        ownerResponseBody.getCluster().setRrs(setSwClusterRrEntity(mfcSwCluster.getSwClusterId()));
        restResponseBase.setResponseBody(JsonUtil.toJson(ownerResponseBody));
      }

      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  protected SwClusterRrEntity setSwClusterRrEntity(Integer swClusterId) {
    try {
      logger.methodStart(new String[] { "swClusterId" }, new Object[] { swClusterId });
      SwClusterRrEntity rrs = new SwClusterRrEntity();
      Rrs rrsConfig = MfcConfigManager.getInstance().getSystemConfSwClusterData(swClusterId).getRrs();
      rrs.setPeerCluster(String.valueOf(rrsConfig.getPeerCluster()));
      List<String> accommodatedClusterList = new ArrayList<>();
      for (Integer accommodatedCluster : rrsConfig.getAccommodatedClusters()) {
        accommodatedClusterList.add(String.valueOf(accommodatedCluster));
      }

      rrs.setAccommodatedClusterList(accommodatedClusterList);
      return rrs;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {
    try {
      logger.methodStart(new String[] { "body" }, new Object[] { ToStringBuilder.reflectionToString(body) });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkForExecClusterInfo(SessionWrapper sessionWrapper, boolean isDelete) throws MsfException {

    try {
      logger.methodStart(new String[] { "isDelete" }, new Object[] { isDelete });
      MfcAsyncRequestsDao mfcAsyncRequestsDao = new MfcAsyncRequestsDao();
      List<MfcAsyncRequest> mfcAsyncRequests = mfcAsyncRequestsDao.readListExecClusterInfo(sessionWrapper);
      for (MfcAsyncRequest mfcAsyncRequest : mfcAsyncRequests) {

        if (!mfcAsyncRequest.getOperationId().equals(getOperationId())) {

          if (isDelete) {
            throw new MsfException(ErrorCode.DELETE_INFORMATION_ERROR, "cluster delete status check error.");
          } else {
            throw new MsfException(ErrorCode.REGIST_INFORMATION_ERROR, "cluster regist status check error.");
          }
        }
      }
    } finally {
      logger.methodEnd();
    }
  }
}
