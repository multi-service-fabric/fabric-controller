
package msf.mfcfc.core.operation.scenario.data.entity;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class OperationRollbackDetailEntity {

  @SerializedName("result")
  private String result;

  @SerializedName("occurred_time")
  private String occurredTime;

  @SerializedName("target_clusters")
  private List<OperationTargetClusterDetailEntity> targetClusterList;

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getOccurredTime() {
    return occurredTime;
  }

  public void setOccurredTime(String occurredTime) {
    this.occurredTime = occurredTime;
  }

  public List<OperationTargetClusterDetailEntity> getTargetClusterList() {
    return targetClusterList;
  }

  public void setTargetClusterList(List<OperationTargetClusterDetailEntity> targetClusterList) {
    this.targetClusterList = targetClusterList;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
