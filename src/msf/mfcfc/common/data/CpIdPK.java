
package msf.mfcfc.common.data;

import msf.mfcfc.common.constant.SliceType;

public class CpIdPK {

  private Integer layerType;

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
}
