package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.SliceType;

@Entity
@Table(name = "vrf_ids")
@NamedQuery(name = "VrfId.findAll", query = "SELECT v FROM VrfId v")
public class VrfId implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "layer_type")
  private Integer layerType;

  @Column(name = "next_id")
  private Integer nextId;

  public VrfId() {
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

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}