
package msf.mfc.slice.cps;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.constant.RestFormatOption;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.scenario.RestRequestBase;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.slice.cps.AbstractCpScenarioBase;
import msf.mfcfc.slice.cps.l2cp.data.L2CpReadResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.L3CpReadResponseBody;

/**
 * Abstract class to implement the common process of CP-related processing in
 * slice management function.
 *
 * @author NTT
 *
 * @param <T>
 *          Request class that inherits the RestRequestBase class
 */
public abstract class MfcAbstractCpScenarioBase<T extends RestRequestBase> extends AbstractCpScenarioBase<T> {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractCpScenarioBase.class);

  protected RestResponseBase getCpDataFromFc(SliceType sliceType, String sliceId, String cpId, int clusterId)
      throws MsfException {
    try {
      logger.methodStart();
      RestResponseBase restResponseBase = RestClient.sendRequest(MfcFcRequestUri.CP_READ.getHttpMethod(),
          MfcFcRequestUri.CP_READ.getUri(sliceType.getMessage(), sliceId, cpId), null,
          MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlAddress(),
          MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlPort());
      if (SliceType.L2_SLICE.equals(sliceType)) {
        L2CpReadResponseBody body = JsonUtil.fromJson(restResponseBase.getResponseBody(), L2CpReadResponseBody.class,
            ErrorCode.UNDEFINED_ERROR);
        checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, body.getErrorCode(),
            ErrorCode.FC_CONTROL_ERROR);
      } else {
        L3CpReadResponseBody body = JsonUtil.fromJson(restResponseBase.getResponseBody(), L3CpReadResponseBody.class,
            ErrorCode.UNDEFINED_ERROR);
        checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, body.getErrorCode(),
            ErrorCode.FC_CONTROL_ERROR);
      }
      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  protected List<RestResponseData> getCpDataListFromFc(SliceType sliceType, Set<Integer> clusterIdSet, String sliceId)
      throws MsfException {
    try {
      logger.methodStart();
      List<RestRequestData> requestDataList = new ArrayList<>();
      for (Integer clusterId : clusterIdSet) {
        String targetUri = MfcFcRequestUri.CP_READ_LIST.getUri(sliceType.getMessage(), sliceId) + "?format="
            + RestFormatOption.DETAIL_LIST.getMessage();
        RestRequestData restRequestData = new RestRequestData(clusterId,
            MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlAddress(),
            MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlPort(),
            MfcFcRequestUri.CP_READ_LIST.getHttpMethod(), targetUri, null, HttpStatus.OK_200);
        requestDataList.add(restRequestData);
      }
      List<RestResponseData> restResponseDataList = sendRequest(requestDataList, RequestType.REQUEST);
      return restResponseDataList;
    } finally {
      logger.methodEnd();
    }
  }
}
