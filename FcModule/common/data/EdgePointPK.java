package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.ToStringBuilder;

@Embeddable
public class EdgePointPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "sw_cluster_id", insertable = false, updatable = false)
  private String swClusterId;

  @Column(name = "edge_point_id")
  private Integer edgePointId;

  public EdgePointPK() {
  }

  public String getSwClusterId() {
    return this.swClusterId;
  }

  public void setSwClusterId(String swClusterId) {
    this.swClusterId = swClusterId;
  }

  public Integer getEdgePointId() {
    return this.edgePointId;
  }

  public void setEdgePointId(Integer edgePointId) {
    this.edgePointId = edgePointId;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof EdgePointPK)) {
      return false;
    }
    EdgePointPK castOther = (EdgePointPK) other;
    return this.swClusterId.equals(castOther.swClusterId) && this.edgePointId.equals(castOther.edgePointId);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.swClusterId.hashCode();
    hash = hash * prime + this.edgePointId.hashCode();

    return hash;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}