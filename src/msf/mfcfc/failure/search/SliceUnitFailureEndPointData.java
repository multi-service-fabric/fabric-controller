
package msf.mfcfc.failure.search;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.FailureStatus;
import msf.mfcfc.common.constant.SliceType;

public class SliceUnitFailureEndPointData {

  public SliceUnitFailureEndPointData(SliceType sliceType, String sliceId, String endPointId,
      FailureStatus failureStatus) {
    this.sliceType = sliceType;
    this.sliceId = sliceId;
    this.endPointId = endPointId;
    this.failureStatus = failureStatus;
  }

  private SliceType sliceType = null;

  private String sliceId = null;

  private String endPointId = null;

  private FailureStatus failureStatus;

  public SliceType getSliceType() {
    return sliceType;
  }

  public void setSliceType(SliceType sliceType) {
    this.sliceType = sliceType;
  }

  public String getSliceId() {
    return sliceId;
  }

  public void setSliceId(String sliceId) {
    this.sliceId = sliceId;
  }

  public String getEndPointId() {
    return endPointId;
  }

  public void setEndPointId(String endPointId) {
    this.endPointId = endPointId;
  }

  public FailureStatus getFailureStatus() {
    return failureStatus;
  }

  public void setFailureStatus(FailureStatus failureStatus) {
    this.failureStatus = failureStatus;
  }

  public boolean isCp() {
    return (sliceType != null && sliceId != null);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (super.equals(obj)) {
      return true;
    } else if (obj instanceof SliceUnitFailureEndPointData) {
      SliceUnitFailureEndPointData target = (SliceUnitFailureEndPointData) obj;
      if ((getSliceId() != null && getSliceId().equals(target.getSliceId())
          || (getSliceId() == null && target.getSliceId() == null)) && getSliceType() == target.getSliceType()
          && getEndPointId().equals(target.getEndPointId())) {
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
    hash = hash * prime + (this.endPointId == null ? 0 : this.endPointId.hashCode());
    hash = hash * prime + (this.failureStatus == null ? 0 : this.failureStatus.hashCode());

    return hash;
  }
}
