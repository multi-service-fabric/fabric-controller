package msf.fc.core.operation.scenario.data.entity;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.AsyncProcessStatus;

public class AsyncRequestEntity {

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
  private RequestEntity request;

  @SerializedName("response")
  private ResponseEntity responseBody;

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

  public RequestEntity getRequest() {
    return request;
  }

  public void setRequest(RequestEntity request) {
    this.request = request;
  }

  public ResponseEntity getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(ResponseEntity responseBody) {
    this.responseBody = responseBody;
  }

  public AsyncProcessStatus getStatusEnum() {
    return AsyncProcessStatus.getEnumFromMessage(status);
  }

  public void setStatusEnum(AsyncProcessStatus status) {
    this.status = status.getMessage();
  }

  @Override
  public String toString() {
    return "AsyncRequestEntity [operationId=" + operationId + ", occurredTime=" + occurredTime + ", lastUpdateTime="
        + lastUpdateTime + ", status=" + status + ", subStatus=" + subStatus + ", request=" + request
        + ", responseBody=" + responseBody + "]";
  }

}
