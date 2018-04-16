
package msf.mfcfc.core.operation.scenario.data;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.AsyncProcessStatus;
import msf.mfcfc.core.operation.scenario.data.entity.OperationRequestEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationResponseEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationRollbackEntity;
import msf.mfcfc.core.operation.scenario.data.entity.OperationTargetClusterEntity;
import msf.mfcfc.rest.common.AbstractResponseBody;

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
  private OperationRequestEntity request;

  @SerializedName("response")
  private OperationResponseEntity responseBody;

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

  public OperationResponseEntity getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(OperationResponseEntity responseBody) {
    this.responseBody = responseBody;
  }

  public AsyncProcessStatus getStatusEnum() {
    return AsyncProcessStatus.getEnumFromMessage(status);
  }

  public void setStatusEnum(AsyncProcessStatus status) {
    this.status = status.getMessage();
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
