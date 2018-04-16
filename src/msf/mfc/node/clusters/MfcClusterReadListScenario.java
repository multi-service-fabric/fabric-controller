
package msf.mfc.node.clusters;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.system.SwCluster;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.OperationType;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.RestUserTypeOption;
import msf.mfcfc.common.constant.SynchronousType;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.node.clusters.data.SwClusterReadListResponseBody;
import msf.mfcfc.node.clusters.data.SwClusterReadOwnerDetailListResponseBody;
import msf.mfcfc.node.clusters.data.SwClusterReadUserDetailListResponseBody;
import msf.mfcfc.node.clusters.data.SwClusterRequest;
import msf.mfcfc.node.clusters.data.entity.SwClusterForOwnerEntity;
import msf.mfcfc.node.clusters.data.entity.SwClusterForUserEntity;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Implementation class for SW cluster information list acquisition.
 *
 * @author NTT
 *
 */
public class MfcClusterReadListScenario extends MfcAbstractClusterScenarioBase<SwClusterRequest> {

  private SwClusterRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(MfcClusterReadListScenario.class);

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
  public MfcClusterReadListScenario(OperationType operationType, SystemInterfaceType systemInterfaceType) {
    this.operationType = operationType;
    this.systemIfType = systemInterfaceType;

    this.syncType = SynchronousType.SYNC;
  }

  @Override
  protected void checkParameter(SwClusterRequest request) throws MsfException {

    try {

      logger.methodStart(new String[] { "request" }, new Object[] { request });

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

        List<MfcSwCluster> mfcSwClusters = getSwClusterList(sessionWrapper, mfcSwClusterDao);

        responseBase = responseClusterReadListData(sessionWrapper, request.getFormat(), request.getUserType(),
            mfcSwClusters);

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

  private List<MfcSwCluster> getSwClusterList(SessionWrapper sessionWrapper, MfcSwClusterDao mfcSwClusterDao)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcSwClusterDao" }, new Object[] { mfcSwClusterDao });
      List<MfcSwCluster> mfcSwClusters = mfcSwClusterDao.readList(sessionWrapper);
      return mfcSwClusters;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase responseClusterReadListData(SessionWrapper sessionWrapper, String format, String userType,
      List<MfcSwCluster> mfcSwClusters) throws MsfException {
    try {
      logger.methodStart(new String[] { "format", "userType", "mfcSwClusters" },
          new Object[] { format, userType, mfcSwClusters });
      if (RestFormatOption.DETAIL_LIST.getMessage().equals(format)) {
        if (mfcSwClusters.isEmpty()) {

          SwClusterReadOwnerDetailListResponseBody body = new SwClusterReadOwnerDetailListResponseBody();
          body.setClusterList(new ArrayList<>());
          return createRestResponse(body, HttpStatus.OK_200);
        } else {
          if (RestUserTypeOption.OPERATOR.getMessage().equals(userType)) {
            List<RestRequestData> requestDataList = createClusterReadListRequestData(mfcSwClusters, true);
            List<RestResponseData> restResponseDatas = sendRequest(requestDataList, RequestType.REQUEST);
            SwClusterReadOwnerDetailListResponseBody body = new SwClusterReadOwnerDetailListResponseBody();
            List<SwClusterForOwnerEntity> swClusterList = new ArrayList<>();
            if (checkResponseAllSuccess(restResponseDatas)) {
              for (RestResponseData restResponseData : restResponseDatas) {
                SwClusterReadOwnerDetailListResponseBody clusterForOwnerEntity = JsonUtil.fromJson(
                    restResponseData.getResponse().getResponseBody(), SwClusterReadOwnerDetailListResponseBody.class,
                    ErrorCode.FC_CONTROL_ERROR);

                for (SwClusterForOwnerEntity swClusterForOwnerEntity : clusterForOwnerEntity.getClusterList()) {

                  swClusterForOwnerEntity
                      .setRrs(setSwClusterRrEntity(Integer.valueOf(swClusterForOwnerEntity.getClusterId())));
                  swClusterList.add(swClusterForOwnerEntity);
                }
              }
              body.setClusterList(swClusterList);
              return createRestResponse(body, HttpStatus.OK_200);
            } else {

              return createErrorResponse(restResponseDatas, null);
            }
          } else {
            List<RestRequestData> requestDataList = createClusterReadListRequestData(mfcSwClusters, false);
            List<RestResponseData> restResponseDatas = sendRequest(requestDataList, RequestType.REQUEST);
            SwClusterReadUserDetailListResponseBody body = new SwClusterReadUserDetailListResponseBody();
            List<SwClusterForUserEntity> swClusterList = new ArrayList<>();
            if (checkResponseAllSuccess(restResponseDatas)) {
              for (RestResponseData restResponseData : restResponseDatas) {
                SwClusterReadUserDetailListResponseBody clusterForUserEntity = JsonUtil.fromJson(
                    restResponseData.getResponse().getResponseBody(), SwClusterReadUserDetailListResponseBody.class,
                    ErrorCode.FC_CONTROL_ERROR);

                swClusterList.add(clusterForUserEntity.getClusterList().get(0));
              }
              body.setClusterList(swClusterList);
              return createRestResponse(body, HttpStatus.OK_200);
            } else {

              return createErrorResponse(restResponseDatas, null);
            }
          }
        }
      } else {
        SwClusterReadListResponseBody body = new SwClusterReadListResponseBody();
        ArrayList<String> swClusterIdList = new ArrayList<>();
        for (MfcSwCluster mfcSwCluster : mfcSwClusters) {
          swClusterIdList.add(String.valueOf(mfcSwCluster.getSwClusterId()));
        }
        body.setClusterIdList(swClusterIdList);
        return createRestResponse(body, HttpStatus.OK_200);
      }
    } finally {
      logger.methodEnd();
    }
  }

  private List<RestRequestData> createClusterReadListRequestData(List<MfcSwCluster> mfcSwClusters, boolean isOwner)
      throws MsfException {
    try {
      logger.methodStart(new String[] { "mfcSwClusters" }, new Object[] { mfcSwClusters });
      List<RestRequestData> requestDataList = new ArrayList<>();
      for (MfcSwCluster mfcSwCluster : mfcSwClusters) {

        SwCluster swCluster = MfcConfigManager.getInstance()
            .getSystemConfSwClusterData(Integer.valueOf(mfcSwCluster.getSwClusterId())).getSwCluster();
        String fcControlAddress = swCluster.getFcControlAddress();
        int fcControlPort = swCluster.getFcControlPort();

        String targetUri = MfcFcRequestUri.SW_CLUSTER_READ_LIST.getUri() + "?format="
            + RestFormatOption.DETAIL_LIST.getMessage();
        if (isOwner) {
          targetUri = targetUri + "&user-type=" + RestUserTypeOption.OPERATOR.getMessage();
        }
        RestRequestData requestData = new RestRequestData(Integer.valueOf(mfcSwCluster.getSwClusterId()),
            fcControlAddress, fcControlPort, MfcFcRequestUri.SW_CLUSTER_READ_LIST.getHttpMethod(), targetUri, null,
            HttpStatus.OK_200);

        requestDataList.add(requestData);
      }
      return requestDataList;
    } finally {
      logger.methodEnd();
    }
  }

}
