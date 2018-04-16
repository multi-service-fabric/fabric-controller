
package msf.mfcfc.failure.status.data.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.ClusterType;
import msf.mfcfc.common.constant.FailureStatus;

public class FailureStatusClusterFailureEntity implements Serializable {

  @SerializedName("cluster_id")
  private String clusterId;

  @SerializedName("type")
  private String type;

  @SerializedName("id")
  private String id;

  @SerializedName("failure_status")
  private String failureStatus;

  public String getClusterId() {
    return clusterId;
  }

  public void setClusterId(String clusterId) {
    this.clusterId = clusterId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFailureStatus() {
    return failureStatus;
  }

  public void setFailureStatus(String failureStatus) {
    this.failureStatus = failureStatus;
  }

  public ClusterType getClusterTypeEnum() {
    return ClusterType.getEnumFromMessage(type);
  }

  public void setClusterTypeEnum(ClusterType type) {
    this.type = type.getMessage();
  }

  public FailureStatus getFailureStatusEnum() {
    return FailureStatus.getEnumFromMessage(failureStatus);
  }

  public void setFailureStatusEnum(FailureStatus failureStatus) {
    this.failureStatus = failureStatus.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
