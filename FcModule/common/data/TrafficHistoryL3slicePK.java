package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;

@Embeddable
public class TrafficHistoryL3slicePK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "occurred_time")
  private java.util.Date occurredTime;

  @Column(name = "slice_id")
  private String sliceId;

  @Column(name = "start_cluster_id")
  private String startClusterId;

  @Column(name = "start_leaf_node_id")
  private Integer startLeafNodeId;

  @Column(name = "start_cp_id")
  private String startCpId;

  @Column(name = "end_cluster_id")
  private String endClusterId;

  @Column(name = "end_leaf_node_id")
  private Integer endLeafNodeId;

  @Column(name = "end_cp_id")
  private String endCpId;

  public TrafficHistoryL3slicePK() {
  }

  public java.util.Date getOccurredTime() {
    return this.occurredTime;
  }

  public void setOccurredTime(java.util.Date occurredTime) {
    this.occurredTime = occurredTime;
  }

  public String getSliceId() {
    return this.sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public String getStartClusterId() {
    return this.startClusterId;
  }

  public void setStartClusterId(String startClusterId) {
    this.startClusterId = startClusterId;
  }

  public Integer getStartLeafNodeId() {
    return this.startLeafNodeId;
  }

  public void setStartLeafNodeId(Integer startLeafNodeId) {
    this.startLeafNodeId = startLeafNodeId;
  }

  public String getStartCpId() {
    return this.startCpId;
  }

  public void setStartCpId(String startCpId) {
    this.startCpId = startCpId;
  }

  public String getEndClusterId() {
    return this.endClusterId;
  }

  public void setEndClusterId(String endClusterId) {
    this.endClusterId = endClusterId;
  }

  public Integer getEndLeafNodeId() {
    return this.endLeafNodeId;
  }

  public void setEndLeafNodeId(Integer endLeafNodeId) {
    this.endLeafNodeId = endLeafNodeId;
  }

  public String getEndCpId() {
    return this.endCpId;
  }

  public void setEndCpId(String endCpId) {
    this.endCpId = endCpId;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof TrafficHistoryL3slicePK)) {
      return false;
    }
    TrafficHistoryL3slicePK castOther = (TrafficHistoryL3slicePK) other;
    return this.occurredTime.equals(castOther.occurredTime) && this.sliceId.equals(castOther.sliceId)
        && this.startClusterId.equals(castOther.startClusterId)
        && this.startLeafNodeId.equals(castOther.startLeafNodeId) && this.startCpId.equals(castOther.startCpId)
        && this.endClusterId.equals(castOther.endClusterId) && this.endLeafNodeId.equals(castOther.endLeafNodeId)
        && this.endCpId.equals(castOther.endCpId);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.occurredTime.hashCode();
    hash = hash * prime + this.sliceId.hashCode();
    hash = hash * prime + this.startClusterId.hashCode();
    hash = hash * prime + this.startLeafNodeId.hashCode();
    hash = hash * prime + this.startCpId.hashCode();
    hash = hash * prime + this.endClusterId.hashCode();
    hash = hash * prime + this.endLeafNodeId.hashCode();
    hash = hash * prime + this.endCpId.hashCode();

    return hash;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}