package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;

@Embeddable
public class TrafficHistoryL2slicePK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "occurred_time")
  private java.util.Date occurredTime;

  @Column(name = "start_cluster_id")
  private String startClusterId;

  @Column(name = "start_leaf_node_id")
  private Integer startLeafNodeId;

  @Column(name = "start_edge_point_id")
  private Integer startEdgePointId;

  @Column(name = "end_cluster_id")
  private String endClusterId;

  @Column(name = "end_leaf_node_id")
  private Integer endLeafNodeId;

  @Column(name = "end_edge_point_id")
  private Integer endEdgePointId;

  public TrafficHistoryL2slicePK() {
  }

  public java.util.Date getOccurredTime() {
    return this.occurredTime;
  }

  public void setOccurredTime(java.util.Date occurredTime) {
    this.occurredTime = occurredTime;
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

  public Integer getStartEdgePointId() {
    return this.startEdgePointId;
  }

  public void setStartEdgePointId(Integer startEdgePointId) {
    this.startEdgePointId = startEdgePointId;
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

  public Integer getEndEdgePointId() {
    return this.endEdgePointId;
  }

  public void setEndEdgePointId(Integer endEdgePointId) {
    this.endEdgePointId = endEdgePointId;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof TrafficHistoryL2slicePK)) {
      return false;
    }
    TrafficHistoryL2slicePK castOther = (TrafficHistoryL2slicePK) other;
    return this.occurredTime.equals(castOther.occurredTime) && this.startClusterId.equals(castOther.startClusterId)
        && this.startLeafNodeId.equals(castOther.startLeafNodeId)
        && this.startEdgePointId.equals(castOther.startEdgePointId) && this.endClusterId.equals(castOther.endClusterId)
        && this.endLeafNodeId.equals(castOther.endLeafNodeId) && this.endEdgePointId.equals(castOther.endEdgePointId);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.occurredTime.hashCode();
    hash = hash * prime + this.startClusterId.hashCode();
    hash = hash * prime + this.startLeafNodeId.hashCode();
    hash = hash * prime + this.startEdgePointId.hashCode();
    hash = hash * prime + this.endClusterId.hashCode();
    hash = hash * prime + this.endLeafNodeId.hashCode();
    hash = hash * prime + this.endEdgePointId.hashCode();

    return hash;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}