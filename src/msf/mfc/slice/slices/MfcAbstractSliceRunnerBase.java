
package msf.mfc.slice.slices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfc.common.data.MfcSwCluster;
import msf.mfc.db.dao.clusters.MfcSwClusterDao;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.slice.slices.AbstractSliceRunnerBase;

/**
 * Abstract class to implement the common process of asynchronous runner process
 * in slice management.
 *
 * @author NTT
 *
 */
public abstract class MfcAbstractSliceRunnerBase extends AbstractSliceRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractSliceRunnerBase.class);

  protected List<RestRequestData> makeRequestDataListForSendToFc(SessionWrapper sessionWrapper,
      List<Integer> clusterIdList, Map<Integer, String> lowerOperationIdMap, String requestBody, HttpMethod httpMethod,
      String targetUri, int expectHttpStatusCode) {
    try {
      logger.methodStart();
      List<RestRequestData> restRequestDataList = new ArrayList<>();

      for (Integer clusterId : clusterIdList) {
        RestRequestData restRequestData = makeRequestDataForSendToFc(clusterId, requestBody, httpMethod, targetUri,
            expectHttpStatusCode);

        if (lowerOperationIdMap != null) {
          restRequestData.setLowerOperationId(lowerOperationIdMap.get(clusterId));
        }
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

  protected List<Integer> getAllSwClusterIdListFromDb(SessionWrapper sessionWrapper) throws MsfException {

    MfcSwClusterDao dao = new MfcSwClusterDao();
    List<MfcSwCluster> swClusterList = dao.readList(sessionWrapper);

    return getSwClusterIdListFromDb(swClusterList);
  }

  protected RestResponseBase sendSliceUpdateRequest(SessionWrapper sessionWrapper, SliceType sliceType,
      String updateRequestBody, String sliceId, List<Integer> clusterIdList, Map<Integer, String> lowerOperationIdMap,
      RequestType requestType) throws MsfException {
    try {
      logger.methodStart();
      List<RestRequestData> restRequestDataList = makeRequestDataListForSendToFc(sessionWrapper, clusterIdList,
          lowerOperationIdMap, updateRequestBody, MfcFcRequestUri.SLICE_UPDATE.getHttpMethod(),
          MfcFcRequestUri.SLICE_UPDATE.getUri(sliceType.getMessage(), sliceId), HttpStatus.ACCEPTED_202);

      List<RestResponseData> restResponseDataList = sendRequest(restRequestDataList, requestType);

      sessionWrapper.rollback();

      if (checkResponseAllSuccess(restResponseDataList)) {
        return new RestResponseBase(HttpStatus.OK_200, (String) null);
      } else {

        return createErrorResponse(restResponseDataList, null);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected void checkSlicePresence(Object sliceEntity, String sliceId) throws MsfException {
    try {
      logger.methodStart(new String[] { "sliceEntity", "sliceId" }, new Object[] { sliceEntity, sliceId });
      ParameterCheckUtil.checkNotNullRelatedResource(sliceEntity, new String[] { "sliceId" }, new Object[] { sliceId });

    } finally {
      logger.methodEnd();
    }
  }
}
