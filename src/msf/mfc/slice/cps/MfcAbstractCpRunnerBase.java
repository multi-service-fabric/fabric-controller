
package msf.mfc.slice.cps;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import msf.mfc.common.config.MfcConfigManager;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.HttpMethod;
import msf.mfcfc.common.constant.MfcFcRequestUri;
import msf.mfcfc.common.constant.RequestType;
import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.core.operation.OperationData;
import msf.mfcfc.core.scenario.RestRequestData;
import msf.mfcfc.core.scenario.RestResponseBase;
import msf.mfcfc.core.scenario.RestResponseData;
import msf.mfcfc.db.SessionWrapper;
import msf.mfcfc.rest.common.JsonUtil;
import msf.mfcfc.rest.common.RestClient;
import msf.mfcfc.slice.cps.AbstractCpRunnerBase;
import msf.mfcfc.slice.cps.l2cp.data.L2CpReadResponseBody;
import msf.mfcfc.slice.cps.l2cp.data.entity.L2CpEntity;
import msf.mfcfc.slice.cps.l3cp.data.L3CpReadResponseBody;
import msf.mfcfc.slice.cps.l3cp.data.entity.L3CpEntity;

/**
 * Abstract class to implement the common process of asynchronous runner
 * processing in CP management.
 *
 * @author NTT
 *
 */
public abstract class MfcAbstractCpRunnerBase extends AbstractCpRunnerBase {

  private static final MsfLogger logger = MsfLogger.getInstance(MfcAbstractCpRunnerBase.class);

  protected CpDeleteRollbackData rollbackData = new CpDeleteRollbackData();

  protected RestResponseBase getCpDataFromFc(SliceType sliceType, String sliceId, String cpId, int clusterId)
      throws MsfException {
    try {
      logger.methodStart();
      RestResponseBase restResponseBase = RestClient.sendRequest(HttpMethod.GET,
          MfcFcRequestUri.CP_READ.getUri(sliceType.getMessage(), sliceId, cpId), null,
          MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlAddress(),
          MfcConfigManager.getInstance().getSystemConfSwClusterData(clusterId).getSwCluster().getFcControlPort());

      if (SliceType.L2_SLICE.equals(sliceType)) {
        L2CpReadResponseBody body = JsonUtil.fromJson(restResponseBase.getResponseBody(), L2CpReadResponseBody.class,
            ErrorCode.FC_CONTROL_ERROR);
        checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, body.getErrorCode(),
            ErrorCode.FC_CONTROL_ERROR);
      } else {
        L3CpReadResponseBody body = JsonUtil.fromJson(restResponseBase.getResponseBody(), L3CpReadResponseBody.class,
            ErrorCode.FC_CONTROL_ERROR);
        checkRestResponseHttpStatusCode(restResponseBase.getHttpStatusCode(), HttpStatus.OK_200, body.getErrorCode(),
            ErrorCode.FC_CONTROL_ERROR);
      }
      return restResponseBase;
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendAsyncRequestAndCreateResponse(SessionWrapper sessionWrapper, boolean isCommit,
      String sliceId, List<RestRequestData> requestDataList, RequestType requestType) throws MsfException {
    try {
      logger.methodStart();

      List<RestResponseData> responseDataList = sendRequest(requestDataList, requestType);

      if (checkResponseAllSuccess(responseDataList)) {

        if (sessionWrapper != null) {
          if (isCommit) {
            sessionWrapper.commit();
          } else {
            sessionWrapper.rollback();
          }
        }
        return new RestResponseBase(HttpStatus.OK_200, (String) null);
      } else {

        return createErrorResponse(responseDataList, null);
      }
    } finally {
      logger.methodEnd();
    }
  }

  protected RestResponseBase sendAsyncRequestAndCreateResponse(String sliceId, List<RestRequestData> requestDataList,
      RequestType requestType) throws MsfException {
    return sendAsyncRequestAndCreateResponse(null, false, sliceId, requestDataList, requestType);
  }

  /**
   * Class for saving the rollback data in CP deletion.
   *
   * @author NTT
   *
   */
  public class CpDeleteRollbackData implements OperationData {

    private List<L2CpEntity> l2CpEntityList = new ArrayList<>();

    private List<L3CpEntity> l3CpEntityList = new ArrayList<>();

    public void addL2CpEntity(L2CpEntity entity) {
      l2CpEntityList.add(entity);
    }

    public void addL3CpEntity(L3CpEntity entity) {
      l3CpEntityList.add(entity);
    }

    /**
     * Return the L2CP information of specified ID.
     *
     * @param sliceId
     *          Slice ID
     * @param cpId
     *          CP ID
     * @return L2CP information
     */
    public L2CpEntity getTargetL2CpEntity(String sliceId, String cpId) {
      for (L2CpEntity entity : l2CpEntityList) {
        if (entity.getSliceId().equals(sliceId) && entity.getCpId().equals(cpId)) {
          return entity;
        }
      }
      return null;
    }

    /**
     * Return the L3CP information of specified ID.
     *
     * @param sliceId
     *          Slice ID
     * @param cpId
     *          CP ID
     * @return L3CP information
     */
    public L3CpEntity getTargetL3CpEntity(String sliceId, String cpId) {
      for (L3CpEntity entity : l3CpEntityList) {
        if (entity.getSliceId().equals(sliceId) && entity.getCpId().equals(cpId)) {
          return entity;
        }
      }
      return null;
    }
  }
}
