
package msf.mfcfc.failure.search;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.SliceType;

/**
 * Class to hold CP information on a specific slice.
 *
 * @author NTT
 *
 */
public class CpData {

  private String sliceId;

  private SliceType sliceType;

  private String cpId;

  /**
   * Constructor.
   *
   * @param sliceId
   *          Slice ID
   * @param sliceType
   *          Slice type
   * @param cpId
   *          CP ID
   */
  public CpData(String sliceId, SliceType sliceType, String cpId) {
    this.sliceId = sliceId;
    this.sliceType = sliceType;
    this.cpId = cpId;
  }

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public SliceType getSliceType() {
    return sliceType;
  }

  public void setSliceType(SliceType sliceType) {
    this.sliceType = sliceType;
  }

  public String getCpId() {
    return cpId;
  }

  public void setCpId(String cpId) {
    this.cpId = cpId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (super.equals(obj)) {
      return true;
    } else if (obj instanceof CpData) {
      CpData target = (CpData) obj;
      if ((getSliceId() != null && getSliceId().equals(target.getSliceId())
          || (getSliceId() == null && target.getSliceId() == null)) && getSliceType() == target.getSliceType()
          && getCpId().equals(target.getCpId())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + (this.sliceType == null ? 0 : this.sliceType.hashCode());
    hash = hash * prime + (this.sliceId == null ? 0 : this.sliceId.hashCode());
    hash = hash * prime + (this.sliceType == null ? 0 : this.sliceType.hashCode());

    return hash;
  }
}
