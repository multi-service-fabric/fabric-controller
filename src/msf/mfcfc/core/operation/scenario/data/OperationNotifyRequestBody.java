
package msf.mfcfc.core.operation.scenario.data;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.common.constant.ErrorCode;
import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.common.log.MsfLogger;
import msf.mfcfc.common.util.ParameterCheckUtil;
import msf.mfcfc.core.operation.scenario.data.entity.OperationRequestEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationResponseEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationRollbackEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationTargetClusterEntity;
import msf.mfcfc.rest.common.RestRequestValidator;

public class OperationNotifyRequestBody implements RestRequestValidator {

  private static final MsfLogger logger = MsfLogger.getInstance(OperationNotifyRequestBody.class);

  @SerializedName("operation_id")
  private String operationId;

  @SerializedName("occurred_time")
  private String occurredTime;

  @SerializedName("last_update_time")
  private String lastUpdateTime;

  @SerializedName("status")
  private String status;

  @SerializedName("sub_status")
  private String subStatus;

  @SerializedName("request")
  private OperationRequestEntity request;

  @SerializedName("response")
  private OperationResponseEntity response;

  @SerializedName("target_clusters")
  private List<OperationTargetClusterEntity> targetClusterList;

  @SerializedName("rollbacks")
  private OperationRollbackEntity rollbacks;

  public String getOperationId() {
    return operationId;
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  public String getOccurredTime() {
    return occurredTime;
  }

  public void setOccurredTime(String occurredTime) {
    this.occurredTime = occurredTime;
  }

  public String getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(String lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getSubStatus() {
    return subStatus;
  }

  public void setSubStatus(String subStatus) {
    this.subStatus = subStatus;
  }

  public OperationRequestEntity getRequest() {
    return request;
  }

  public void setRequest(OperationRequestEntity request) {
    this.request = request;
  }

  public OperationResponseEntity getResponse() {
    return response;
  }

  public void setResponse(OperationResponseEntity response) {
    this.response = response;
  }

  public List<OperationTargetClusterEntity> getTargetClusterList() {
    return targetClusterList;
  }

  public void setTargetClusterList(List<OperationTargetClusterEntity> targetClusterList) {
    this.targetClusterList = targetClusterList;
  }

  public OperationRollbackEntity getRollbacks() {
    return rollbacks;
  }

  public void setRollbacks(OperationRollbackEntity rollbacks) {
    this.rollbacks = rollbacks;
  }

  public AsyncProcessStatus getStatusEnum() {
    return AsyncProcessStatus.getEnumFromMessage(status);
  }

  public void setStatusEnum(AsyncProcessStatus status) {
    this.status = status.getMessage();
  }

  @Override
  public void validate() throws MsfException {
    try {
      logger.methodStart();

      ParameterCheckUtil.checkNotNullAndLength(operationId);

      ParameterCheckUtil.checkDatetime(occurredTime);

      ParameterCheckUtil.checkDatetime(lastUpdateTime);

      ParameterCheckUtil.checkNotNull(getStatusEnum());

      ParameterCheckUtil.checkNotNull(request);

      switch (getStatusEnum()) {
        case COMPLETED:
        case FAILED:

          ParameterCheckUtil.checkNotNull(response);
          validateResponse(response);
          break;
        case CANCELED:
        case RUNNING:
        case WAITING:
          break;
        default:

          throw new IllegalArgumentException(MessageFormat.format("status={0}", getStatusEnum()));
      }

      validateRequest(request);
      if (targetClusterList != null) {
        validateTargetClusterList();
      }
      if (rollbacks != null) {
        validateRollbacks();
      }
    } finally {
      logger.methodEnd();
    }
  }

  private void validateRequest(OperationRequestEntity tempRequest) throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(tempRequest.getUri());

    ParameterCheckUtil.checkNotNull(tempRequest.getMethodEnum());

    ParameterCheckUtil.checkNotNull(tempRequest.getBody());
  }

  private void validateResponse(OperationResponseEntity tempResponse) throws MsfException {

    ParameterCheckUtil.checkNotNull(tempResponse.getStatusCode());

    ParameterCheckUtil.checkNotNull(tempResponse.getBody());

  }

  private void validateTargetClusterList() throws MsfException {

    for (OperationTargetClusterEntity tempTargerCluster : targetClusterList) {
      validateTargetCluster(tempTargerCluster);
    }
  }

  private void validateTargetCluster(OperationTargetClusterEntity tempTargerCluster) throws MsfException {

    ParameterCheckUtil.checkNumericId(tempTargerCluster.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

    ParameterCheckUtil.checkNotNull(tempTargerCluster.getRequest());

    validateRequest(tempTargerCluster.getRequest());
  }

  private void validateRollbacks() throws MsfException {

    ParameterCheckUtil.checkNotNull(rollbacks.getResultEnum());

    ParameterCheckUtil.checkNotNullAndLength(rollbacks.getOccurredTime());

    if (rollbacks.getTargetClusterList() != null) {
      validateRollbacksTargetClusterList();
    }
  }

  private void validateRollbacksTargetClusterList() throws MsfException {

    for (OperationTargetClusterEntity tempTagetCluster : rollbacks.getTargetClusterList()) {
      validateRollbacksTargetCluster(tempTagetCluster);
    }
  }

  private void validateRollbacksTargetCluster(OperationTargetClusterEntity tempTagetCluster) throws MsfException {

    ParameterCheckUtil.checkNumericId(tempTagetCluster.getClusterId(), ErrorCode.PARAMETER_VALUE_ERROR);

    ParameterCheckUtil.checkNotNull(tempTagetCluster.getRequest());

    validateRollbacksRequest(tempTagetCluster.getRequest());
  }

  private void validateRollbacksRequest(OperationRequestEntity tempRequest) throws MsfException {

    ParameterCheckUtil.checkNotNullAndLength(tempRequest.getUri());

    ParameterCheckUtil.checkNotNull(tempRequest.getMethodEnum());

  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
