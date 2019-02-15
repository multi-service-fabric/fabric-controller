
package msf.mfcfc.core.scenario;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.constant.ErrorResponseDataConsistency;
import msf.mfcfc.common.constant.SystemInterfaceType;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.rest.common.AbstractResponseBody;
import msf.mfcfc.rest.common.JsonUtil;

/**
 * Error response class for REST.
 *
 * @author NTT
 *
 */
public class ErrorResponse extends RestResponseBase {

  private static final MsfLogger logger = MsfLogger.getInstance(ErrorResponse.class);

  /**
   * Constructor for error response generation <br>
   * Generates an internal or external error response from ErrorCode.
   *
   * @param errCode
   *          Error code
   * @param systemIfType
   *          System interface type
   * @param isConsistencyNotClear
   *          Whether the data consistency status is unclear (e.g., when an
   *          error occurred after sending a request to the lower system)
   */
  public ErrorResponse(ErrorCode errCode, SystemInterfaceType systemIfType, boolean isConsistencyNotClear) {
    try {
      logger.methodStart(new String[] { "errCode", "systemIfType" }, new Object[] { errCode, systemIfType });
      ErrorResponseBody responseBody = new ErrorResponseBody();

      responseBody.setErrorCode(errCode.getCode());
      switch (systemIfType) {
        case INTERNAL:

          break;
        case EXTERNAL:

          responseBody.setErrorMessage(errCode.getMessage());

          if (isConsistencyNotClear) {
            responseBody.setDataConsistencyEnum(ErrorResponseDataConsistency.UNKNOWN);
          } else {
            responseBody.setDataConsistencyEnum(errCode.getDataConsistency().getErrorResponseDataConsistency());
          }
          break;
        default:
          break;
      }

      String responseBodyJson = JsonUtil.toJson(responseBody);

      this.httpStatusCode = errCode.getStatusCode();
      this.responseBody = responseBodyJson;
    } finally {
      logger.methodEnd();
    }
  }

  /**
   * Constructor for error response generation <br>
   * Generates an internal or external error response from ErrorCode.
   *
   * @param errCode
   *          Error code
   * @param systemIfType
   *          System interface type
   */
  public ErrorResponse(ErrorCode errCode, SystemInterfaceType systemIfType) {
    this(errCode, systemIfType, false);
  }

  /**
   * Constructor for generating MFC error response.
   *
   * @param errCode
   *          Error code
   * @param errorResponseDataConsistency
   *          Data consistency status
   * @param targetClusterList
   *          All clusters information of the request target
   */
  public ErrorResponse(ErrorCode errCode, ErrorResponseDataConsistency errorResponseDataConsistency,
      List<TargetClusterEntity> targetClusterList) {
    try {
      logger.methodStart(new String[] { "errCode", "errorResponseDataConsistency", "targetClusterList" },
          new Object[] { errCode, errorResponseDataConsistency, targetClusterList });
      ErrorResponseBody responseBody = new ErrorResponseBody();
      responseBody.setErrorCode(errCode.getCode());
      responseBody.setErrorMessage(errCode.getMessage());
      responseBody.setDataConsistencyEnum(errorResponseDataConsistency);
      if (targetClusterList.size() != 0) {
        responseBody.setRequestTargetClusterList(targetClusterList);
      }

      String responseBodyJson = JsonUtil.toJson(responseBody);

      this.httpStatusCode = errCode.getStatusCode();
      this.responseBody = responseBodyJson;
    } finally {
      logger.methodEnd();
    }
  }

  class ErrorResponseBody extends AbstractResponseBody {

    @SerializedName("target_clusters")
    protected List<TargetClusterEntity> requestTargetClusterList = null;

    public List<TargetClusterEntity> getRequestTargetClusterList() {
      return requestTargetClusterList;
    }

    public void setRequestTargetClusterList(List<TargetClusterEntity> requestTargetClusterList) {
      this.requestTargetClusterList = requestTargetClusterList;
    }
  }
}
