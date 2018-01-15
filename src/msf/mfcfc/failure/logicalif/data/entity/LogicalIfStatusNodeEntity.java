package msf.mfcfc.failure.logicalif.data.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.FailureStatus;


public class LogicalIfStatusNodeEntity {

  
  @SerializedName("node_id")
  private String nodeId;

  
  @SerializedName("failure_status")
  private String failureStatus;

  
  public String getNodeId() {
    return nodeId;
  }

  
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  
  public String getFailureStatus() {
    return failureStatus;
  }

  
  public void setFailureStatus(String failureStatus) {
    this.failureStatus = failureStatus;
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
