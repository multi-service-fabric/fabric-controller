
package msf.mfc.traffic.traffics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.traffic.traffics.AbstractCpsTrafficScenarioBase;
import msf.mfcfc.traffic.traffics.data.CpTrafficReadListResponseBody;
import msf.mfcfc.traffic.traffics.data.CpTrafficRequest;
import msf.mfcfc.traffic.traffics.data.entity.CpTrafficEntity;

/**
 * Abstract class to implement the common process of CP traffic information
 * acquisition processing in traffic management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class MfcAbstractCpsTrafficScenarioBase<T extends RestRequestBase>
    extends AbstractCpsTrafficScenarioBase<T> {

  protected CpTrafficRequest request;

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractCpsTrafficScenarioBase.class);

  protected RestResponseBase sendTrafficRead(Integer clusterId) throws MsfException {

    try {
      logger.methodStart();

      String requestUri = MfcFcRequestUri.CP_TRAFFIC_READ.getUri(request.getSliceType(), request.getSliceId(),
          request.getCpId());

      return sendTrafficRequest(MfcFcRequestUri.CP_TRAFFIC_READ.getHttpMethod(), requestUri, clusterId);

    } finally {
      logger.methodEnd();
    }

  }

  protected RestResponseBase sendTrafficReadList(Set<Integer> clusterIdSet) throws MsfException {

    try {
      logger.methodStart();

      List<RestRequestData> requestDataList = new ArrayList<>();

      for (Integer clusterId : clusterIdSet) {

        String targetUri = MfcFcRequestUri.CP_TRAFFIC_READ_LIST.getUri(request.getSliceType(), request.getSliceId());

        RestRequestData restRequestData = new RestRequestData(clusterId,
            MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlAddress(),
            MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlPort(),
            MfcFcRequestUri.CP_TRAFFIC_READ_LIST.getHttpMethod(), targetUri, null, HttpStatus.OK_200);

        requestDataList.add(restRequestData);
      }

      List<RestResponseData> restResponseDataList = sendRequest(requestDataList, RequestType.REQUEST);

      return createCpTrafficReadListResponseBase(restResponseDataList);

    } finally {
      logger.methodEnd();
    }

  }

  protected RestResponseBase createRestResponse(AbstractResponseBody body, int statusCode) {

    try {
      logger.methodStart(new String[] { "body", "statusCode" },
          new Object[] { ToStringBuilder.reflectionToString(body), statusCode });

      RestResponseBase response = new RestResponseBase(statusCode, body);
      return response;
    } finally {
      logger.methodEnd();
    }
  }

  private RestResponseBase sendTrafficRequest(HttpMethod method, String uri, Integer clusterId) throws MsfException {
    try {
      logger.methodStart();

      String fcControlIpAddress = MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster()
          .getFcControlAddress();
      int fcControlPort = MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster()
          .getFcControlPort();

      RestResponseBase restResponseBase = RestClient.sendRequest(method, uri, null, fcControlIpAddress, fcControlPort);

      return restResponseBase;

    } finally {
      logger.methodEnd();
    }

  }

  private RestResponseBase createCpTrafficReadListResponseBase(List<RestResponseData> restResponseDataList)
      throws MsfException {
    try {

      logger.methodStart();

      if (!checkResponseAllSuccess(restResponseDataList)) {

        return createErrorResponse(restResponseDataList, null);

      }

      RestResponseBase resultResponseBase = new RestResponseBase();

      CpTrafficReadListResponseBody body = createCpTrafficReadListResponseBody(restResponseDataList);

      resultResponseBase.setHttpStatusCode(HttpStatus.OK_200);

      resultResponseBase.setResponseBody(JsonUtil.toJson(body));

      return resultResponseBase;

    } finally {
      logger.methodEnd();
    }

  }

  private CpTrafficReadListResponseBody createCpTrafficReadListResponseBody(List<RestResponseData> restResponseDataList)
      throws MsfException {
    try {
      logger.methodStart();

      CpTrafficReadListResponseBody resultBody = new CpTrafficReadListResponseBody();
      List<CpTrafficEntity> cpTrafficEntityList = new ArrayList<CpTrafficEntity>();

      for (RestResponseData temp : restResponseDataList) {

        String bodyStr = temp.getResponse().getResponseBody();

        CpTrafficReadListResponseBody body = JsonUtil.fromJson(bodyStr, CpTrafficReadListResponseBody.class,
            ErrorCode.UNDEFINED_ERROR);

        if (body.getCpTrafficList() != null) {
          cpTrafficEntityList.addAll(body.getCpTrafficList());
        }
      }

      resultBody.setCpTrafficList(cpTrafficEntityList);

      return resultBody;

    } finally {
      logger.methodEnd();
    }

  }

}
