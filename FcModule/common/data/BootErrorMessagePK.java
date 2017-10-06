package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.ToStringBuilder;

@Embeddable
public class BootErrorMessagePK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "sw_cluster_id", insertable = false, updatable = false)
  private String swClusterId;

  @Column(name = "equipment_type_id", insertable = false, updatable = false)
  private Integer equipmentTypeId;

  @Column(name = "boot_error_msg")
  private String bootErrorMsg;

  public BootErrorMessagePK() {
  }

  public String getSwClusterId() {
    return this.swClusterId;
  }

  public void setSwClusterId(String swClusterId) {
    this.swClusterId = swClusterId;
  }

  public Integer getEquipmentTypeId() {
    return this.equipmentTypeId;
  }

  public void setEquipmentTypeId(Integer equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  public String getBootErrorMsg() {
    return this.bootErrorMsg;
  }

  public void setBootErrorMsg(String bootErrorMsg) {
    this.bootErrorMsg = bootErrorMsg;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof BootErrorMessagePK)) {
      return false;
    }
    BootErrorMessagePK castOther = (BootErrorMessagePK) other;
    return this.swClusterId.equals(castOther.swClusterId) && this.equipmentTypeId.equals(castOther.equipmentTypeId)
        && this.bootErrorMsg.equals(castOther.bootErrorMsg);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.swClusterId.hashCode();
    hash = hash * prime + this.equipmentTypeId.hashCode();
    hash = hash * prime + this.bootErrorMsg.hashCode();

    return hash;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}