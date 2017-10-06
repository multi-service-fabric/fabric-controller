package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.fc.common.constant.SliceType;

@Embeddable
public class CpIdPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "layer_type")
  private Integer layerType;

  @Column(name = "slice_id")
  private String sliceId;

  public CpIdPK() {
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

  public String getSliceId() {
    return this.sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CpIdPK)) {
      return false;
    }
    CpIdPK castOther = (CpIdPK) other;
    return this.layerType.equals(castOther.layerType) && this.sliceId.equals(castOther.sliceId);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.layerType.hashCode();
    hash = hash * prime + this.sliceId.hashCode();

    return hash;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}