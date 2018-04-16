
package msf.fc.common.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import msf.mfcfc.common.data.CpIdPK;

@Embeddable
public class FcCpIdPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "layer_type")
  private Integer layerType;

  @Column(name = "slice_id")
  private String sliceId;

  public FcCpIdPK() {
  }

  public FcCpIdPK(CpIdPK cpIdPk) {
    setCommonEntity(cpIdPk);
  }

  public Integer getLayerType() {
    return this.layerType;
  }

  public void setLayerType(Integer layerType) {
    this.layerType = layerType;
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
    if (!(other instanceof FcCpIdPK)) {
      return false;
    }
    FcCpIdPK castOther = (FcCpIdPK) other;
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

  public void setCommonEntity(CpIdPK cpIdPk) {
    setLayerType(cpIdPk.getLayerType());
    setSliceId(cpIdPk.getSliceId());
  }

  public CpIdPK getCommonEntity() {
    CpIdPK cpIdPk = new CpIdPK();
    cpIdPk.setLayerType(getLayerType());
    cpIdPk.setSliceId(getSliceId());
    return cpIdPk;
  }

}
