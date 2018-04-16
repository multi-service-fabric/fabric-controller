
package msf.mfcfc.failure.status.data.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

import msf.mfcfc.common.constant.SliceUnitReachableStatus;

public class FailureStatusSliceClusterLinkReachableStatusEntity implements Serializable {

  @SerializedName("cluster_link_if_id")
  private String clusterLinkIfId;

  @SerializedName("opposite_cluster_link_if_id")
  private String oppositeClusterLinkIfId;

  @SerializedName("reachable_status")
  private String reachableStatus;

  public FailureStatusSliceClusterLinkReachableStatusEntity() {

  }

  public FailureStatusSliceClusterLinkReachableStatusEntity(String clusterLinkIfId, String oppositeClusterLinkIfId,
      String reachableStatus) {
    super();
    this.clusterLinkIfId = clusterLinkIfId;
    this.oppositeClusterLinkIfId = oppositeClusterLinkIfId;
    this.reachableStatus = reachableStatus;
  }

  public String getClusterLinkIfId() {
    return clusterLinkIfId;
  }

  public void setClusterLinkIfId(String clusterLinkIfId) {
    this.clusterLinkIfId = clusterLinkIfId;
  }

  public String getOppositeClusterLinkIfId() {
    return oppositeClusterLinkIfId;
  }

  public void setOppositeClusterLinkIfId(String oppositeClusterLinkIfId) {
    this.oppositeClusterLinkIfId = oppositeClusterLinkIfId;
  }

  public String getReachableStatus() {
    return reachableStatus;
  }

  public void setReachableStatus(String reachableStatus) {
    this.reachableStatus = reachableStatus;
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
