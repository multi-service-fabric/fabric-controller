
package msf.mfcfc.core.operation.scenario.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationEntity {

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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
