
package msf.mfcfc.core.operation.scenario.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.RollbackResult;

public class OperationRollbackEntity {

  @SerializedName("result")
  private String result;

  @SerializedName("occurred_time")
  private String occurredTime;

  @SerializedName("target_clusters")
  private List<OperationTargetClusterEntity> targetClusterList;

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public RollbackResult getResultEnum() {
    return RollbackResult.getEnumFromMessage(result);
  }

  public void setResultEnum(RollbackResult result) {
    this.result = result.getMessage();
  }

  public String getOccurredTime() {
    return occurredTime;
  }

  public void setOccurredTime(String occurredTime) {
    this.occurredTime = occurredTime;
  }

  public List<OperationTargetClusterEntity> getTargetClusterList() {
    return targetClusterList;
  }

  public void setTargetClusterList(List<OperationTargetClusterEntity> targetClusterList) {
    this.targetClusterList = targetClusterList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
