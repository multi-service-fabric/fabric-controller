package msf.fc.common.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.fc.common.exception.MsfException;
import msf.fc.db.SessionWrapper;

@Entity
@Table(name = "rrs")
@NamedQuery(name = "Rr.findAll", query = "SELECT r FROM Rr r")
public class Rr implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RrPK id;

  @Column(name = "rr_router_id")
  private String rrRouterId;

  @ManyToMany
  @JoinTable(name = "bgp_connections", joinColumns = {
      @JoinColumn(name = "rr_node_id", referencedColumnName = "rr_node_id"),
      @JoinColumn(name = "sw_cluster_id", referencedColumnName = "sw_cluster_id") }, inverseJoinColumns = {
          @JoinColumn(name = "node_info_id") })
  private List<Node> nodes;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sw_cluster_id", insertable = false, updatable = false)
  private SwCluster swCluster;

  public Rr() {
  }

  public RrPK getId() {
    return this.id;
  }

  public void setId(RrPK id) {
    this.id = id;
  }

  public String getRrRouterId() {
    return this.rrRouterId;
  }

  public void setRrRouterId(String rrRouterId) {
    this.rrRouterId = rrRouterId;
  }

  public List<Node> getNodes() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.nodes);
  }

  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
  }

  public SwCluster getSwCluster() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.swCluster);
  }

  public void setSwCluster(SwCluster swCluster) {
    this.swCluster = swCluster;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this)
        .setExcludeFieldNames(new String[] { "nodes", "swCluster" })
        .toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Rr other = (Rr) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

}