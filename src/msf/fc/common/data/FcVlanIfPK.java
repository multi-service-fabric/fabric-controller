
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FcVlanIfPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "node_info_id")
  private Long nodeInfoId;

  @Column(name = "vlan_if_id")
  private Integer vlanIfId;

  public FcVlanIfPK() {
  }

  public Long getNodeInfoId() {
    return this.nodeInfoId;
  }

  public void setNodeInfoId(Long nodeInfoId) {
    this.nodeInfoId = nodeInfoId;
  }

  public Integer getVlanIfId() {
    return this.vlanIfId;
  }

  public void setVlanIfId(Integer vlanIfId) {
    this.vlanIfId = vlanIfId;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof FcVlanIfPK)) {
      return false;
    }
    FcVlanIfPK castOther = (FcVlanIfPK) other;
    return this.nodeInfoId.equals(castOther.nodeInfoId) && this.vlanIfId.equals(castOther.vlanIfId);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.nodeInfoId.hashCode();
    hash = hash * prime + this.vlanIfId.hashCode();

    return hash;
  }
}
