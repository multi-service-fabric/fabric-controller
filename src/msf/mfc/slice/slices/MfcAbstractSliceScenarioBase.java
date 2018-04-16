
package msf.mfc.slice.slices;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.config.type.data.Rr;
import msf.mfc.common.config.type.system.SwClusterData;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.slices.AbstractSliceScenarioBase;

/**
 * Abstract class to implement the common process of L2/L3 slice-related
 * processing in slice management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class MfcAbstractSliceScenarioBase<T extends RestRequestBase> extends AbstractSliceScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractSliceScenarioBase.class);

  protected void checkRrConfiguration() throws MsfException {
    try {
      logger.methodStart();

      List<SwClusterData> swClusterDataList = MfcConfigManager.getInstance().getSystemConfSwClusterDataList();
      for (SwClusterData swClusterData : swClusterDataList) {

        int swClusterId = swClusterData.getSwCluster().getSwClusterId();

        int peerSwClusterId = swClusterData.getRrs().getPeerCluster();

        if (swClusterId != peerSwClusterId) {
          List<Rr> rrList = MfcConfigManager.getInstance().getDataConfSwClusterData(peerSwClusterId).getRrs().getRr();

          if (rrList == null || rrList.size() == 0) {
            String errorMessage = MessageFormat.format(
                "RR configuration is not found ,even though peer target from other cluster(ID:{0}).", swClusterId);
            logger.error(errorMessage);
            throw new MsfException(ErrorCode.TRANSITION_STATUS_ERROR, errorMessage);
          }
        }

      }
    } finally {
      logger.methodEnd();
    }
  }

  protected List<Integer> getSwClusterIdListFromDb(List<MfcSwCluster> swClusterList) {
    try {
      logger.methodStart();
      List<Integer> idList = new ArrayList<>();
      for (MfcSwCluster swCluster : swClusterList) {
        idList.add(swCluster.getSwClusterId());
      }
      return idList;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<Integer> getSwClusterIdListFromResponse(List<RestResponseData> restResponseDataList) {
    try {
      logger.methodStart();
      List<Integer> idList = new ArrayList<>();
      for (RestResponseData data : restResponseDataList) {
        if (HttpStatus.isSuccess(data.getResponse().getHttpStatusCode())) {
          idList.add(data.getRequest().getClusterId());
        }
      }
      return idList;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<RestRequestData> makeRequestDataListForSendToFc(SessionWrapper sessionWrapper,
      List<Integer> clusterIdList, String requestBody, HttpMethod httpMethod, String targetUri,
      int expectHttpStatusCode) {
    try {
      logger.methodStart();
      List<RestRequestData> restRequestDataList = new ArrayList<>();

      for (Integer clusterId : clusterIdList) {
        RestRequestData restRequestData = makeRequestDataForSendToFc(clusterId, requestBody, httpMethod, targetUri,
            expectHttpStatusCode);
        restRequestDataList.add(restRequestData);
      }
      return restRequestDataList;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestRequestData makeRequestDataForSendToFc(int clusterId, String requestBody, HttpMethod httpMethod,
      String targetUri, int expectHttpStatusCode) {
    try {
      logger.methodStart(new String[] { "clusterId", "requestBody", "httpMethod", "targetUri", "expectHttpStatusCode" },
          new Object[] { clusterId, requestBody, httpMethod, targetUri, expectHttpStatusCode });
      RestRequestData restRequestData = new RestRequestData();
      restRequestData.setClusterId(clusterId);
      restRequestData.setIpAddress(
          MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlAddress());
      restRequestData.setPort(
          MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlPort());
      restRequestData.setExpectHttpStatusCode(expectHttpStatusCode);
      restRequestData.setHttpMethod(httpMethod);
      restRequestData.setTargetUri(targetUri);
      RestRequestBase requestBase = new RestRequestBase();
      requestBase.setRequestBody(requestBody);
      restRequestData.setRequest(requestBase);
      return restRequestData;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendSliceCreateDeleteRequest(SessionWrapper sessionWrapper, SliceType sliceType,
      boolean isCreateRequest, String createRequestBody, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sessionWrapper", "sliceType", "isCreateRequest", "createRequestBody" },
          new Object[] { sessionWrapper, sliceType, isCreateRequest, createRequestBody });
      MfcSwClusterDao dao = new MfcSwClusterDao();
      List<MfcSwCluster> swClusterList = dao.readList(sessionWrapper);
      List<RestRequestData> restRequestDataList;

      if (isCreateRequest) {
        restRequestDataList = makeRequestDataListForSendToFc(sessionWrapper, getSwClusterIdListFromDb(swClusterList),
            createRequestBody, MfcFcRequestUri.SLICE_CREATE.getHttpMethod(),
            MfcFcRequestUri.SLICE_CREATE.getUri(sliceType.getMessage()), HttpStatus.CREATED_201);
      } else {
        restRequestDataList = makeRequestDataListForSendToFc(sessionWrapper, getSwClusterIdListFromDb(swClusterList),
            null, MfcFcRequestUri.SLICE_DELETE.getHttpMethod(),
            MfcFcRequestUri.SLICE_DELETE.getUri(sliceType.getMessage(), sliceId), HttpStatus.NO_CONTENT_204);
      }

      List<RestResponseData> restResponseDataList = sendRequest(restRequestDataList, RequestType.REQUEST);

      if (!checkRollbackRequired(restResponseDataList)) {

        if (checkResponseAllSuccess(restResponseDataList)) {

          if (isCreateRequest) {
            sessionWrapper.commit(MfcFcRequestUri.SLICE_CREATE);
            return createL3SliceCreateResponse(sliceId);
          } else {
            sessionWrapper.commit(MfcFcRequestUri.SLICE_DELETE);
            return createL3SliceDeleteResponse();
          }
        } else {

          return createErrorResponse(restResponseDataList, null);
        }
      } else {

        List<RestRequestData> rollbackRequestDataList;

        if (isCreateRequest) {
          rollbackRequestDataList = makeRequestDataListForSendToFc(sessionWrapper,
              getSwClusterIdListFromResponse(restResponseDataList), null, MfcFcRequestUri.SLICE_DELETE.getHttpMethod(),
              MfcFcRequestUri.SLICE_DELETE.getUri(sliceType.getMessage(), sliceId), HttpStatus.NO_CONTENT_204);
        } else {
          rollbackRequestDataList = makeRequestDataListForSendToFc(sessionWrapper,
              getSwClusterIdListFromResponse(restResponseDataList), createRequestBody,
              MfcFcRequestUri.SLICE_CREATE.getHttpMethod(), MfcFcRequestUri.SLICE_CREATE.getUri(sliceType.getMessage()),
              HttpStatus.CREATED_201);
        }
        List<RestResponseData> restRollbackResponseDataList = sendRequest(rollbackRequestDataList,
            RequestType.ROLLBACK);

        sessionWrapper.rollback();

        return createErrorResponse(restResponseDataList, restRollbackResponseDataList);
      }
    } finally {
      logger.methodEnd();
    }
  }

}
