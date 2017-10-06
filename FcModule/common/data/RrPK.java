package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.ToStringBuilder;

@Embeddable
public class RrPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "sw_cluster_id", insertable = false, updatable = false)
  private String swClusterId;

  @Column(name = "rr_node_id")
  private Integer rrNodeId;

  public RrPK() {
  }

  public String getSwClusterId() {
    return this.swClusterId;
  }

  public void setSwClusterId(String swClusterId) {
    this.swClusterId = swClusterId;
  }

  public Integer getRrNodeId() {
    return this.rrNodeId;
  }

  public void setRrNodeId(Integer rrNodeId) {
    this.rrNodeId = rrNodeId;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof RrPK)) {
      return false;
    }
    RrPK castOther = (RrPK) other;
    return this.swClusterId.equals(castOther.swClusterId) && this.rrNodeId.equals(castOther.rrNodeId);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.swClusterId.hashCode();
    hash = hash * prime + this.rrNodeId.hashCode();

    return hash;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}