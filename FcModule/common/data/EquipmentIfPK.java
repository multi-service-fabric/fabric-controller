package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.ToStringBuilder;

@Embeddable
public class EquipmentIfPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "sw_cluster_id", insertable = false, updatable = false)
  private String swClusterId;

  @Column(name = "equipment_type_id", insertable = false, updatable = false)
  private Integer equipmentTypeId;

  @Column(name = "physical_if_id")
  private String physicalIfId;

  @Column(name = "speed_capability")
  private String speedCapability;

  public EquipmentIfPK() {
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

  public String getPhysicalIfId() {
    return this.physicalIfId;
  }

  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  public String getSpeedCapability() {
    return this.speedCapability;
  }

  public void setSpeedCapability(String speedCapability) {
    this.speedCapability = speedCapability;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof EquipmentIfPK)) {
      return false;
    }
    EquipmentIfPK castOther = (EquipmentIfPK) other;
    return this.swClusterId.equals(castOther.swClusterId) && this.equipmentTypeId.equals(castOther.equipmentTypeId)
        && this.physicalIfId.equals(castOther.physicalIfId) && this.speedCapability.equals(castOther.speedCapability);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.swClusterId.hashCode();
    hash = hash * prime + this.equipmentTypeId.hashCode();
    hash = hash * prime + this.physicalIfId.hashCode();
    hash = hash * prime + this.speedCapability.hashCode();

    return hash;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}