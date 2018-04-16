
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.data.VrfId;

@Entity
@Table(name = "vrf_ids")
@NamedQuery(name = "FcVrfId.findAll", query = "SELECT f FROM FcVrfId f")
public class FcVrfId implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "layer_type")
  private Integer layerType;

  @Column(name = "next_id")
  private Integer nextId;

  public FcVrfId() {
  }

  public FcVrfId(VrfId vrfId) {
    setCommonEntity(vrfId);
  }

  public Integer getLayerType() {
    return this.layerType;
  }

  public void setLayerType(Integer layerType) {
    this.layerType = layerType;
  }

  public SliceType getLayerTypeEnum() {
    return SliceType.getEnumFromCode(this.layerType);
  }

  public void setLayerTypeEnum(SliceType layerType) {
    this.layerType = layerType.getCode();
  }

  public Integer getNextId() {
    return this.nextId;
  }

  public void setNextId(Integer nextId) {
    this.nextId = nextId;
  }

  public void setCommonEntity(VrfId vrfId) {
    setLayerType(vrfId.getLayerType());
    setNextId(vrfId.getNextId());
  }

  public VrfId getCommonEntity() {
    VrfId vrfId = new VrfId();
    vrfId.setLayerType(layerType);
    vrfId.setNextId(nextId);
    return vrfId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
