
package msf.mfcfc.failure.search;

import org.apache.commons.lang.builder.ToStringBuilder;

import msf.mfcfc.common.constant.FailureStatus;
import msf.mfcfc.common.constant.SliceType;

/**
 * Class to store each endpoint information which is needed to calculate the
 * reachability in failure information of a slice.
 *
 * @author NTT
 *
 */
public class SliceUnitFailureEndPointData implements Comparable<SliceUnitFailureEndPointData> {

  /**
   * Constructor.
   *
   * @param sliceType
   *          Slice type
   * @param sliceId
   *          Slice ID
   * @param endPointId
   *          Endpoint ID
   * @param failureStatus
   *          Failure status
   */
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

  /**
   * Returns whether this endpoint is a CP (i.e. slice type, slice ID have been
   * set).
   *
   * @return true; if this is a CP, false; otherwise (i.e. this is an
   *         Inter-cluster link).
   */
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

  @Override
  public int compareTo(SliceUnitFailureEndPointData other) {

    if (this.sliceType != null && other.sliceType != null) {
      int sliceTypeCompare = this.sliceType.compareTo(other.sliceType);
      if (sliceTypeCompare != 0) {
        return sliceTypeCompare;
      }
    } else if (this.sliceType != null || other.sliceType != null) {
      if (this.sliceType == null) {
        return -1;
      } else {
        return 1;
      }
    }

    if (this.sliceId != null && other.sliceId != null) {
      int sliceIdCompare = this.sliceId.compareTo(other.sliceId);
      if (sliceIdCompare != 0) {
        return sliceIdCompare;
      }
    } else if (this.sliceId != null || other.sliceId != null) {
      if (this.sliceId == null) {
        return -1;
      } else {
        return 1;
      }
    }

    if (this.endPointId != null && other.endPointId != null) {
      int endPointIdCompare = this.endPointId.compareTo(other.endPointId);
      if (endPointIdCompare != 0) {
        return endPointIdCompare;
      }
    } else if (this.endPointId != null || other.endPointId != null) {
      if (this.endPointId == null) {
        return -1;
      } else {
        return 1;
      }
    }

    if (this.failureStatus != null && other.failureStatus != null) {
      return this.failureStatus.compareTo(other.failureStatus);
    } else if (this.failureStatus != null || other.failureStatus != null) {
      if (this.failureStatus == null) {
        return -1;
      } else {
        return 1;
      }
    }
    return 0;
  }
}
