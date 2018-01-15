
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.SliceType;
import msf.mfcfc.common.data.SliceId;

@Entity
@Table(name = "slice_ids")
@NamedQuery(name = "FcSliceId.findAll", query = "SELECT f FROM FcSliceId f")
public class FcSliceId implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "layer_type")
  private Integer layerType;

  @Column(name = "next_id")
  private Integer nextId;

  public FcSliceId() {
  }

  public FcSliceId(SliceId sliceId) {
    setCommonEntity(sliceId);
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

  public void setCommonEntity(SliceId sliceId) {
    setLayerType(sliceId.getLayerType());
    setNextId(sliceId.getNextId());
  }

  public SliceId getCommonEntity() {
    SliceId sliceId = new SliceId();
    sliceId.setLayerType(getLayerType());
    sliceId.setNextId(getNextId());
    return sliceId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}