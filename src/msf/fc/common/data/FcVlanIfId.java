
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.data.VlanIfId;

@Entity
@Table(name = "vlan_if_ids")
@NamedQuery(name = "FcVlanIfId.findAll", query = "SELECT f FROM FcVlanIfId f")
public class FcVlanIfId implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "node_info_id")
  private Integer nodeInfoId;

  @Column(name = "next_id")
  private Integer nextId;

  public FcVlanIfId() {
  }

  public FcVlanIfId(VlanIfId vlanIfId) {
    setCommonEntity(vlanIfId);
  }

  public Integer getNodeInfoId() {
    return this.nodeInfoId;
  }

  public void setNodeInfoId(Integer nodeInfoId) {
    this.nodeInfoId = nodeInfoId;
  }

  public Integer getNextId() {
    return this.nextId;
  }

  public void setNextId(Integer nextId) {
    this.nextId = nextId;
  }

  public void setCommonEntity(VlanIfId vlanIfId) {
    setNextId(vlanIfId.getNextId());
    setNodeInfoId(vlanIfId.getNodeInfoId());
  }

  public VlanIfId getCommonEntity() {
    VlanIfId vlanIfId = new VlanIfId();
    vlanIfId.setNextId(getNextId());
    vlanIfId.setNodeInfoId(getNodeInfoId());
    return vlanIfId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
