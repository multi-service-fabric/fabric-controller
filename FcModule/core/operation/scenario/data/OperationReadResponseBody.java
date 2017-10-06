package msf.fc.core.operation.scenario.data;

import com.google.gson.annotations.SerializedName;

import msf.fc.common.constant.AsyncProcessStatus;
import msf.fc.core.operation.scenario.data.entity.RequestDetailEntity;
import msf.fc.core.operation.scenario.data.entity.ResponseDetailEntity;
import msf.fc.rest.common.AbstractResponseBody;

public class OperationReadResponseBody extends AbstractResponseBody {

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
  private RequestDetailEntity request;

  @SerializedName("response")
  private ResponseDetailEntity responseBody;

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

  public RequestDetailEntity getRequest() {
    return request;
  }

  public void setRequest(RequestDetailEntity request) {
    this.request = request;
  }

  public ResponseDetailEntity getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(ResponseDetailEntity responseBody) {
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
    return "OperationReadResponseBody [operationId=" + operationId + ", occurredTime=" + occurredTime
        + ", lastUpdateTime=" + lastUpdateTime + ", status=" + status + ", subStatus=" + subStatus + ", request="
        + request + ", responseBody=" + responseBody + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage
        + "]";
  }

}
