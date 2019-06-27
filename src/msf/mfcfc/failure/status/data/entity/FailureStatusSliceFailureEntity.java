
package msf.mfcfc.failure.status.data.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.FailureStatus;
import msf.mfcfc.common.constant.SliceType;

public class FailureStatusSliceFailureEntity implements Serializable {

  @SerializedName("slice_type")
  private String sliceType;

  @SerializedName("slice_id")
  private String sliceId;

  @SerializedName("cp_ids")
  private Set<String> cpIdSet;

  @SerializedName("failure_status")
  private String failureStatus;

  @SerializedName("reachable_statuses")
  private List<FailureStatusReachableStatusFailureEntity> reachableStatusList;

  public FailureStatusSliceFailureEntity() {

  }

  public FailureStatusSliceFailureEntity(String sliceId, String sliceType, Set<String> cpIdSet, String failureStatus,
      List<FailureStatusReachableStatusFailureEntity> reachableStatusList) {
    super();
    this.sliceId = sliceId;
    this.sliceType = sliceType;
    this.cpIdSet = cpIdSet;
    this.failureStatus = failureStatus;
    this.reachableStatusList = reachableStatusList;
  }

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public String getSliceType() {
    return sliceType;
  }

  public void setSliceType(String sliceType) {
    this.sliceType = sliceType;
  }

  public Set<String> getCpIdSet() {
    return cpIdSet;
  }

  public void setCpIdSet(Set<String> cpIdSet) {
    this.cpIdSet = cpIdSet;
  }

  public String getFailureStatus() {
    return failureStatus;
  }

  public void setFailureStatus(String failureStatus) {
    this.failureStatus = failureStatus;
  }

  public List<FailureStatusReachableStatusFailureEntity> getReachableStatusList() {
    return reachableStatusList;
  }

  public void setReachableStatusList(List<FailureStatusReachableStatusFailureEntity> reachableStatusList) {
    this.reachableStatusList = reachableStatusList;
  }

  public SliceType getSliceTypeEnum() {
    return SliceType.getEnumFromMessage(sliceType);
  }

  public void setSliceTypeEnum(SliceType sliceType) {
    this.sliceType = sliceType.getMessage();
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
