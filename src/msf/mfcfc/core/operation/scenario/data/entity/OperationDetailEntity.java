
package msf.mfcfc.core.operation.scenario.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.AsyncProcessStatus;

public class OperationDetailEntity {

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
  private OperationRequestDetailEntity request;

  @SerializedName("response")
  private OperationResponseDetailEntity response;

  @SerializedName("target_clusters")
  private List<OperationTargetClusterDetailEntity> targetClusterList;

  @SerializedName("rollbacks")
  private OperationRollbackDetailEntity rollbacks;

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

  public AsyncProcessStatus getStatusEnum() {
    return AsyncProcessStatus.getEnumFromMessage(status);
  }

  public void setStatusEnum(AsyncProcessStatus status) {
    this.status = status.getMessage();
  }

  public String getSubStatus() {
    return subStatus;
  }

  public void setSubStatus(String subStatus) {
    this.subStatus = subStatus;
  }

  public OperationRequestDetailEntity getRequest() {
    return request;
  }

  public void setRequest(OperationRequestDetailEntity request) {
    this.request = request;
  }

  public OperationResponseDetailEntity getResponse() {
    return response;
  }

  public void setResponse(OperationResponseDetailEntity response) {
    this.response = response;
  }

  public List<OperationTargetClusterDetailEntity> getTargetClusterList() {
    return targetClusterList;
  }

  public void setTargetClusterList(List<OperationTargetClusterDetailEntity> targetClusterList) {
    this.targetClusterList = targetClusterList;
  }

  public OperationRollbackDetailEntity getRollbacks() {
    return rollbacks;
  }

  public void setRollbacks(OperationRollbackDetailEntity rollbacks) {
    this.rollbacks = rollbacks;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
