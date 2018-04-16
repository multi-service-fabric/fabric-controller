
package msf.mfc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "equipments")
@NamedQuery(name = "MfcEquipment.findAll", query = "SELECT m FROM MfcEquipment m")
public class MfcEquipment implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "equipment_type_info_id")
  private Integer equipmentTypeInfoId;

  public MfcEquipment() {
  }

  public Integer getEquipmentTypeInfoId() {
    return this.equipmentTypeInfoId;
  }

  public void setEquipmentTypeInfoId(Integer equipmentTypeInfoId) {
    this.equipmentTypeInfoId = equipmentTypeInfoId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
