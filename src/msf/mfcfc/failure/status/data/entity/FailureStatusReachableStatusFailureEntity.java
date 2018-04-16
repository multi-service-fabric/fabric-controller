
package msf.mfcfc.failure.status.data.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.SliceUnitReachableOppositeType;
import msf.mfcfc.common.constant.SliceUnitReachableStatus;

public class FailureStatusReachableStatusFailureEntity implements Serializable {

  @SerializedName("cp_id")
  private String cpId;

  @SerializedName("opposite_type")
  private String oppositeType;

  @SerializedName("opposite_id")
  private String oppositeId;

  @SerializedName("reachable_status")
  private String reachableStatus;

  public FailureStatusReachableStatusFailureEntity() {

  }

  public FailureStatusReachableStatusFailureEntity(String cpId, String oppositeType, String oppositeId,
      String reachableStatus) {
    super();
    this.cpId = cpId;
    this.oppositeType = oppositeType;
    this.oppositeId = oppositeId;
    this.reachableStatus = reachableStatus;
  }

  public String getCpId() {
    return cpId;
  }

  public void setCpId(String cpId) {
    this.cpId = cpId;
  }

  public String getOppositeType() {
    return oppositeType;
  }

  public void setOppositeType(String oppositeType) {
    this.oppositeType = oppositeType;
  }

  public String getOppositeId() {
    return oppositeId;
  }

  public void setOppositeId(String oppositeId) {
    this.oppositeId = oppositeId;
  }

  public String getReachableStatus() {
    return reachableStatus;
  }

  public void setReachableStatus(String reachableStatus) {
    this.reachableStatus = reachableStatus;
  }

  public SliceUnitReachableOppositeType getOppositeTypeEnum() {
    return SliceUnitReachableOppositeType.getEnumFromMessage(oppositeType);
  }

  public void setOppositeTypeEnum(SliceUnitReachableOppositeType oppositeType) {
    this.oppositeType = oppositeType.getMessage();
  }

  public SliceUnitReachableStatus getReachableStatusEnum() {
    return SliceUnitReachableStatus.getEnumFromMessage(reachableStatus);
  }

  public void setReachableStatusEnum(SliceUnitReachableStatus reachableStatus) {
    this.reachableStatus = reachableStatus.getMessage();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
