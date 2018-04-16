
package msf.mfcfc.failure.status.data.entity;

import java.io.Serializable;
import java.util.List;

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
  private List<String> cpIdList;

  @SerializedName("failure_status")
  private String failureStatus;

  @SerializedName("reachable_statuses")
  private List<FailureStatusReachableStatusFailureEntity> reachableStatusList;

  public FailureStatusSliceFailureEntity() {

  }

  public FailureStatusSliceFailureEntity(String sliceId, String sliceType, List<String> cpIdList, String failureStatus,
      List<FailureStatusReachableStatusFailureEntity> reachableStatusList) {
    super();
    this.sliceId = sliceId;
    this.sliceType = sliceType;
    this.cpIdList = cpIdList;
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

  public List<String> getCpIdList() {
    return cpIdList;
  }

  public void setCpIdList(List<String> cpIdList) {
    this.cpIdList = cpIdList;
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
