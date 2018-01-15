
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import msf.mfcfc.common.exception.MsfException;
import msf.mfcfc.db.SessionWrapper;

@Entity
@Table(name = "leaf_nodes")
@NamedQuery(name = "FcLeafNode.findAll", query = "SELECT f FROM FcLeafNode f")
public class FcLeafNode implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "node_info_id")
  private Long nodeInfoId;

  @Column(name = "leaf_type")
  private Integer leafType;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "node_info_id")
  private FcNode node;

  public FcLeafNode() {
  }

  public Long getNodeInfoId() {
    return this.nodeInfoId;
  }

  public void setNodeInfoId(Long nodeInfoId) {
    this.nodeInfoId = nodeInfoId;
  }

  public Integer getLeafType() {
    return this.leafType;
  }

  public void setLeafType(Integer leafType) {
    this.leafType = leafType;
  }

  public FcNode getNode() throws MsfException {
    return SessionWrapper.getLazyLoadData(this.node);
  }

  public void setNode(FcNode node) {
    this.node = node;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).setExcludeFieldNames(new String[] { "node" }).toString();
  }

}